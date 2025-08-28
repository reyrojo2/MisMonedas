package controller;

import interfaces.MontosDao;
import dao.IngresoDAOImpl;
import dao.EgresoDAOImpl;
import dao.CategoriaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Ingreso;
import model.Egreso;
import model.User;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * Controlador para gestionar INGRESOS y EGRESOS con un solo servlet.
 * - Usa una estrategia (Strategy) por tipo para evitar duplicar código.
 * - Asegura sesión activa antes de operar (seguridad).
 * - Carga categorías globales por tipo para poblar los formularios.
 * - Implementa PRG (Post/Redirect/Get) al guardar para evitar reenvíos.
 */
@WebServlet("/montosController")
public class MontosController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** Enum interno para distinguir el tipo de movimiento solicitado por la UI. */
    private enum Tipo { INGRESO, EGRESO }

    /**
     * Strategy: agrupa toda la lógica específica por tipo en una sola estructura:
     * - tipoStr: cadena que llega desde la vista ("ingreso" | "egreso")
     * - dao: DAO concreto para persistir/consultar
     * - binder: función que arma la entidad T a partir del HttpServletRequest
     * - listView / formView: JSPs a utilizar
     * - listAttr: nombre del atributo request donde se dejan las filas a listar
     * - rowMapper: cómo transformar T -> Map para que la JSP itere de forma simple
     */
    private static class Strategy<T> {
        final String tipoStr; // "ingreso" | "egreso" 
        final MontosDao<T> dao;
        final Function<HttpServletRequest, T> binder;
        final String listView;
        final String formView;
        final String listAttr;
        final Function<T, Map<String,Object>> rowMapper;

        Strategy(String tipoStr, 
                 MontosDao<T> dao,
                 Function<HttpServletRequest, T> binder,
                 String listView,
                 String formView,
                 String listAttr,
                 Function<T, Map<String,Object>> rowMapper) {
            this.tipoStr = tipoStr; 
            this.dao = dao;
            this.binder = binder;
            this.listView = listView;
            this.formView = formView;
            this.listAttr = listAttr;
            this.rowMapper = rowMapper;
        }
    }

    /** Estrategias por tipo cargadas en init(); acceso O(1) por enum. */
    private final Map<Tipo, Strategy<?>> strategies = new EnumMap<>(Tipo.class);
    /** DAO para leer categorías globales (id+nombre) según tipo. */
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    /** --- Validación de sesión activa --- */
    private User getLoggedUser(HttpServletRequest req) {
        // Centraliza cómo se lee el usuario de sesión
        return (User) req.getSession().getAttribute("loggedInUser");
    }
    private int requireUserIdOrRedirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Si no hay usuario, redirige a login y devuelve -1 como marcador de corte
        User u = getLoggedUser(req);
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return -1;
        }
        return u.getId();
    }

    @Override
    public void init() {
        // Estrategia para INGRESOS
        Strategy<Ingreso> stratIngreso = new Strategy<>(
            "ingreso", 
            (MontosDao<Ingreso>) new IngresoDAOImpl(), // DAO específico de ingresos
            req -> {
                // Binder: extrae parámetros del form y arma la entidad de dominio
                String montoStr    = req.getParameter("txtMontoIngreso");
                String categoria   = req.getParameter("cbCategoriaIngreso"); // viene id o nombre, el DAO resolverá
                String fechaStr    = req.getParameter("txtFechaIngreso");
                String descripcion = req.getParameter("txtDescripcionIngreso");
                double monto = Double.parseDouble(montoStr);
                java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
                Ingreso i = new Ingreso();
                i.setMonto(monto);
                i.setCategoria(categoria);
                i.setFecha(java.sql.Date.valueOf(fecha));
                i.setDescripcion(descripcion);
                return i;
            },
            "guiIngresoReporte.jsp", // vista de listado
            "guiIngreso.jsp",        // vista de formulario
            "ingresos",              // nombre del atributo para la lista en request
            ing -> modelvm.ViewRows.row() // Mapeo a filas de vista (Map) para simplificar la JSP
                  .put("fecha", ing.getFecha())
                  .put("categoria", ing.getCategoria())
                  .put("descripcion", ing.getDescripcion())
                  .put("monto", ing.getMonto())
                  .build()
        );

        // Estrategia para EGRESOS
        Strategy<Egreso> stratEgreso = new Strategy<>(
            "egreso", 
            (MontosDao<Egreso>) new EgresoDAOImpl(), // DAO específico de egresos
            req -> {
                // Binder: idem pero para egresos
                String montoStr    = req.getParameter("txtMontoEgreso");
                String categoria   = req.getParameter("cbCategoriaEgreso"); // viene id o nombre, el DAO resolverá
                String fechaStr    = req.getParameter("txtFechaEgreso");
                String descripcion = req.getParameter("txtDescripcionEgreso");
                double monto = Double.parseDouble(montoStr);
                java.time.LocalDate fecha = java.time.LocalDate.parse(fechaStr);
                Egreso e = new Egreso();
                e.setMonto(monto);
                e.setCategoria(categoria);
                e.setFecha(java.sql.Date.valueOf(fecha));
                e.setDescripcion(descripcion);
                return e;
            },
            "guiEgresoReporte.jsp",
            "guiEgreso.jsp",
            "egresos",
            eg -> modelvm.ViewRows.row()
                  .put("fecha", eg.getFecha())
                  .put("categoria", eg.getCategoria())
                  .put("descripcion", eg.getDescripcion())
                  .put("monto", eg.getMonto())
                  .build()
        );

        // Registra las estrategias por tipo (clave enum) para su posterior uso
        strategies.put(Tipo.INGRESO, stratIngreso);
        strategies.put(Tipo.EGRESO,  stratEgreso);
    }

    /** Parsea el parámetro 'tipo' de la request y lo mapea al enum Tipo. */
    private Tipo parseTipo(HttpServletRequest req) {
        String t = Optional.ofNullable(req.getParameter("tipo")).orElse("").trim().toLowerCase();
        if ("ingreso".equals(t)) return Tipo.INGRESO;
        if ("egreso".equals(t))  return Tipo.EGRESO;
        // Forzamos error temprano si la UI envía un tipo no soportado
        throw new IllegalArgumentException("Parámetro 'tipo' inválido. Use ingreso o egreso.");
    }

    /** Obtiene la Strategy concreta según el 'tipo' recibido en la request. */
    @SuppressWarnings("unchecked")
    private <T> Strategy<T> strategy(HttpServletRequest req) {
        return (Strategy<T>) strategies.get(parseTipo(req));
    }

    /**
     * Consulta entidades por usuario, las transforma a filas de vista y hace forward al JSP de reporte.
     * También calcula el total por tipo para mostrar en la cabecera del reporte.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void listarYForward(HttpServletRequest request, HttpServletResponse response, int userId)
            throws Exception {
        var strat = strategy(request);
        var tipo  = parseTipo(request);

        // Consulta todos los movimientos del usuario para el tipo actual
        var entidades = strat.dao.findByUserId(userId);

        // Convierte las entidades a Maps listos para la vista (evita lógica en la JSP)
        List<Map<String,Object>> filas = modelvm.ViewRows.map((List) entidades,
                (Function) ((Strategy) strat).rowMapper);

        // Atributo con el nombre dinámico según tipo (ingresos | egresos)
        request.setAttribute(strat.listAttr, filas);

        // Calcula total de montos para mostrar en el resumen del reporte
        double total = strat.dao.sumByUserId(userId);
        if (tipo == Tipo.INGRESO) {
            request.setAttribute("totalIngresos", total);
            request.setAttribute("activePage", "ingresos"); // indica a la navbar qué sección resaltar
        } else {
            request.setAttribute("totalEgresos", total);
            request.setAttribute("activePage", "egresos");
        }

        // Forward a la vista de reporte correspondiente al tipo actual
        request.getRequestDispatcher(strat.listView).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obliga a tener usuario autenticado; si no, redirige y corta el flujo
        int userId = requireUserIdOrRedirect(request, response);
        if (userId < 0) return;

        // Acción por defecto: listar
        String accion = Optional.ofNullable(request.getParameter("accion")).orElse("listar");

        try {
            var strat = strategy(request);
            switch (accion) {
                case "form" -> {
                    // Cargar categorías según tipo ("ingreso" | "egreso") para el combo del formulario
                	List<Map<String,Object>> categorias = categoriaDAO.listarPorTipo(strat.tipoStr);
                    request.setAttribute("categorias", categorias);
                    // Define la página activa para el menú (usado por la navbar)
                    request.setAttribute("activePage", "ingreso".equals(strat.tipoStr) ? "ingresos" : "egresos"); // navbar
                    // Muestra el formulario del tipo actual
                    request.getRequestDispatcher(strat.formView).forward(request, response);
                }
                case "listar" -> listarYForward(request, response, userId);
                default -> throw new IllegalArgumentException("Acción no soportada: " + accion);
            }

        } catch (Exception e) {
            // Manejo genérico de errores: log + vista de error
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Asegura que los parámetros del form en UTF-8 se interpreten correctamente
        request.setCharacterEncoding("UTF-8");

        // Revalida usuario en POST
        int userId = requireUserIdOrRedirect(request, response);
        if (userId < 0) return;

        // Acción por defecto: guardar (alta)
        String accion = Optional.ofNullable(request.getParameter("accion")).orElse("guardar");

        try {
            var strat = strategy(request);
            var tipo  = parseTipo(request);
            HttpSession session = request.getSession(); 

            switch (accion) {
                case "guardar" -> {
                    // Construye la entidad desde el request usando el binder de la estrategia
                    Object entity = strat.binder.apply(request);
                    // Setea el userId en la entidad concreta (según tipo) antes de persistir
                    if (tipo == Tipo.INGRESO) {
                        ((Ingreso) entity).setUserId(userId);
                    } else {
                        ((Egreso) entity).setUserId(userId);
                    }

                    // Intenta guardar; si ok, redirige para evitar reenvíos (PRG)
                    if (strat.dao.save(entity)) {
                    	session.setAttribute("flash_success",
                                ("ingreso".equals(strat.tipoStr) ? "Ingreso" : "Egreso") + " registrado con éxito");
                        String url = request.getContextPath()
                                + "/montosController?tipo=" + request.getParameter("tipo");
                        response.sendRedirect(url);
                    } else {
                        // Si falla, vuelve al form con mensaje de error y recarga categorías
                        request.setAttribute("error", "No se pudo registrar el registro.");
                        //volver a cargar categorías para re-render del form con error
                        List<Map<String,Object>> categorias = categoriaDAO.listarPorTipo(strat.tipoStr);
                        request.setAttribute("categorias", categorias);
                        request.setAttribute("activePage", "ingreso".equals(strat.tipoStr) ? "ingresos" : "egresos");
                        request.getRequestDispatcher(strat.formView).forward(request, response);
                    }
                }
                case "listar" -> listarYForward(request, response, userId);
                default -> throw new IllegalArgumentException("Acción no soportada: " + accion);
            }

        } catch (RuntimeException re) {
            // Errores de negocio/validación: muestra error en pantalla
            re.printStackTrace();
            request.setAttribute("error", "Error procesando la solicitud: " + re.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            // Cualquier otra excepción no controlada
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
