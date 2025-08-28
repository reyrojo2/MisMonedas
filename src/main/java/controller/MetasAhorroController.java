package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.MetasAhorro;
import model.User;
import service.MetasAhorroService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.MetasAhorroDAOImpl;
import interfaces.MetasAhorroDao;

/**
 * Controlador HTTP (Servlet) para gestionar las Metas de Ahorro.
 * - Restringe el acceso a usuarios autenticados (usa "loggedInUser" en sesión).
 * - Maneja navegación (formulario, listado, edición) y acciones (crear/actualizar/eliminar).
 * - Usa forwards para renderizar JSPs y redirects para cerrar acciones (PRG pattern).
 */
@WebServlet("/MetasAhorroController")
public class MetasAhorroController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** Rutas de vistas JSP (formulario y reporte/listado) */
    private static final String JSP_FORM     = "guiMetasAhorro.jsp";
    private static final String JSP_REPORTE  = "guiMetasAhorroReporte.jsp";

    /** DAO para acceso a datos de metas de ahorro */
    private MetasAhorroDao metaDao;
    @Override
    public void init() {
        // Inicializa la implementación del DAO que opera con la base de datos
        metaDao = new MetasAhorroDAOImpl();
        // Instancia el servicio (no se guarda referencia). Puede usarse para inicializaciones laterales/estáticas.
        new MetasAhorroService();
    }

    // ========================= GET =========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la sesión y verifica usuario autenticado
        HttpSession session = request.getSession();
        User usuario = (User) session.getAttribute("loggedInUser");
        if (usuario == null) {
            // Si no hay usuario en sesión, redirige a login
            response.sendRedirect("loginController");
            return;
        }

        // Lee la acción; por defecto "listar" si viene vacía
        String accion = request.getParameter("accion");
        if (accion == null || accion.isBlank()) accion = "listar";

        try {
            switch (accion.toLowerCase()) {
                case "form": {
                    // Muestra el formulario vacío para crear una meta
                    request.setAttribute("activePage", "metas"); // resalta pestaña activa en la UI
                    request.getRequestDispatcher(JSP_FORM).forward(request, response);
                    break;
                }
                case "listar": {
                    // Carga las metas del usuario y las envía a la vista de reporte
                    cargarYEnviarListado(usuario, request, response);
                    break;
                }
                case "edit": { // mostrar formulario con data existente
                    // Valida que venga el id de la meta a editar
                    String idStr = request.getParameter("id");
                    if (idStr == null || idStr.isBlank()) {
                        response.sendRedirect("MetasAhorroController?accion=listar");
                        return;
                    }
                    int id = Integer.parseInt(idStr);

                    // Busca la meta por id, acotada al usuario (seguridad a nivel de datos)
                    MetasAhorro meta = metaDao.findById(id, usuario.getId());
                    if (meta == null) {
                        // Si no existe (o no pertenece al usuario), vuelve al listado
                        response.sendRedirect("MetasAhorroController?accion=listar");
                        return;
                    }

                    // Prepara valores en formato ISO para inputs type="date" del formulario de edición
                    SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaIniVal = (meta.getFechaInicio() != null) ? iso.format(meta.getFechaInicio()) : "";
                    String fechaFinVal = (meta.getFechaFin() != null) ? iso.format(meta.getFechaFin()) : "";

                    // Atributos que la JSP leerá para precargar el formulario
                    request.setAttribute("metaEdit", meta);
                    request.setAttribute("metaEditFechaInicio", fechaIniVal);
                    request.setAttribute("metaEditFechaFin", fechaFinVal);

                    // Forward al formulario (modo edición)
                    request.getRequestDispatcher(JSP_FORM).forward(request, response);
                    break;
                }
                case "delete": {
                    // Elimina una meta por id (si viene) acotado al usuario autenticado
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isBlank()) {
                        int id = Integer.parseInt(idStr);
                        metaDao.delete(id, usuario.getId());
                    }
                    // Redirige al listado para evitar re-envío del formulario (PRG)
                    response.sendRedirect("MetasAhorroController?accion=listar");
                    break;
                }
                default:
                    // Acción desconocida -> HTTP 400
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no soportada");
            }
        } catch (Exception e) {
            // En caso de excepción, registra traza, muestra error y vuelve a cargar el listado
            e.printStackTrace();
            request.setAttribute("error", "Error procesando la acción: " + e.getMessage());
            cargarYEnviarListado(usuario, request, response);
        }
    }

    // ========================= POST =========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Revalida sesión en POST (evita operaciones sin autenticación)
        HttpSession session = request.getSession();
        User usuario = (User) session.getAttribute("loggedInUser");
        if (usuario == null) {
            response.sendRedirect("LoginController");
            return;
        }

        // Lee la acción; por defecto "listar"
        String accion = request.getParameter("accion");
        if (accion == null || accion.isBlank()) accion = "listar";

        try {
            switch (accion.toLowerCase()) {
                case "form":
                    // Simplemente muestra el formulario (sin lógica de negocio)
                    request.getRequestDispatcher(JSP_FORM).forward(request, response);
                    break;

                case "listar":
                    // Mismo comportamiento que en GET: mostrar el reporte
                    cargarYEnviarListado(usuario, request, response);
                    break;

                case "save": {
                    // Vincula parámetros del request a la entidad
                    MetasAhorro meta = bindFromRequest(request, usuario.getId());
                    if (meta == null) {
                        // Si faltan datos o hay error de parseo, vuelve al formulario con mensaje
                        request.setAttribute("error", "Todos los campos son obligatorios o con formato inválido");
                        request.getRequestDispatcher(JSP_FORM).forward(request, response);
                        return;
                    }
                    // Persiste la meta y redirige al listado (PRG para evitar doble envío)
                    if (metaDao.save(meta)) {
                    	session.setAttribute("flash_success", "Meta de ahorro guardada con éxito"); 
                        response.sendRedirect("MetasAhorroController?accion=listar");
                    } else {
                        // Si falla el guardado, permanece en el form mostrando error
                        request.setAttribute("error", "No se pudo crear la meta de ahorro");
                        request.getRequestDispatcher(JSP_FORM).forward(request, response);
                    }
                    break;
                }

                case "update": {
                    // Valida id a actualizar
                    String idStr = request.getParameter("id");
                    if (idStr == null || idStr.isBlank()) {
                        request.setAttribute("error", "ID de meta no provisto");
                        request.getRequestDispatcher(JSP_FORM).forward(request, response);
                        return;
                    }
                    int id = Integer.parseInt(idStr);

                    // Verifica existencia y pertenencia de la meta al usuario
                    MetasAhorro existente = metaDao.findById(id, usuario.getId());
                    if (existente == null) {
                        request.setAttribute("error", "Meta no encontrada");
                        request.getRequestDispatcher(JSP_FORM).forward(request, response);
                        return;
                    }

                    // Re-bindea desde el request; si falla vuelve con datos originales y error
                    MetasAhorro meta = bindFromRequest(request, usuario.getId());
                    if (meta == null) {
                        // Prepara nuevamente los valores de fechas para los inputs del form
                        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
                        request.setAttribute("metaEdit", existente);
                        request.setAttribute("metaEditFechaInicio", existente.getFechaInicio() != null ? iso.format(existente.getFechaInicio()) : "");
                        request.setAttribute("metaEditFechaFin",    existente.getFechaFin()    != null ? iso.format(existente.getFechaFin())    : "");
                        request.setAttribute("error", "Todos los campos son obligatorios o con formato inválido");
                        request.getRequestDispatcher(JSP_FORM).forward(request, response);
                        return;
                    }
                    // Asigna el id de la meta a actualizar
                    meta.setId(id);

                    // Actualiza y redirige al listado; si falla, permanece en el form con error
                    if (metaDao.update(meta, usuario.getId())) {
                    	session.setAttribute("flash_success", "Meta de ahorro actualizada con éxito"); 
                        response.sendRedirect("MetasAhorroController?accion=listar");
                    } else {
                        // Reconstituye valores para mantener el form poblado tras el error
                        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
                        request.setAttribute("metaEdit", meta);
                        request.setAttribute("metaEditFechaInicio", meta.getFechaInicio() != null ? iso.format(meta.getFechaInicio()) : "");
                        request.setAttribute("metaEditFechaFin",    meta.getFechaFin()    != null ? iso.format(meta.getFechaFin())    : "");
                        request.setAttribute("error", "No se pudo actualizar la meta de ahorro");
                        request.getRequestDispatcher(JSP_FORM).forward(request, response);
                    }
                    break;
                }

                case "delete": { // (opcional por POST)
                    // Elimina y redirige al listado; soporta borrado vía POST
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isBlank()) {
                        int id = Integer.parseInt(idStr);
                        metaDao.delete(id, usuario.getId());
                    }
                    response.sendRedirect("MetasAhorroController?accion=listar");
                    break;
                }

                default:
                    // Acción no reconocida
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no soportada");
            }

        } catch (NumberFormatException e) {
            // Errores de parseo numérico (por ejemplo, montos)
            e.printStackTrace();
            request.setAttribute("error", "Error en formato de números: " + e.getMessage());
            request.getRequestDispatcher(JSP_FORM).forward(request, response);

        } catch (Exception e) {
            // Cualquier otra excepción se notifica al usuario y se retorna al formulario
            e.printStackTrace();
            request.setAttribute("error", "Error procesando la acción: " + e.getMessage());
            request.getRequestDispatcher(JSP_FORM).forward(request, response);
        }
    }

    // ========================= Helpers =========================

    /**
     * Carga las metas del usuario, arma un ViewModel amigable para la JSP
     * (con formatos de números/fechas y porcentaje de progreso) y hace forward al reporte.
     */
    private void cargarYEnviarListado(User usuario, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Consulta las metas del usuario autenticado
        List<MetasAhorro> metas = metaDao.findByUserId(usuario.getId());

        // Formateadores para valores monetarios y fechas en la vista
        var df  = new java.text.DecimalFormat("#,##0.00");
        var sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // Contenedor de filas que la JSP iterará (evita lógica de formato en la JSP)
        var metasVm = new java.util.ArrayList<java.util.Map<String,Object>>();

        // Transforma cada entidad a un mapa listo para mostrar
        if (metas != null) {
            for (MetasAhorro m : metas) {
                metasVm.add(buildVm(m, df, sdf));
            }
        }

        // Marca la sección activa en la UI y envía el modelo a la JSP de reporte
        request.setAttribute("activePage", "metas");
        request.setAttribute("metasVm", metasVm);
        request.getRequestDispatcher(JSP_REPORTE).forward(request, response);
    }

    /**
     * Construye una fila de la tabla de reporte a partir de la entidad.
     * Incluye formateo de montos, fechas y cálculo de % de progreso.
     */
    private java.util.Map<String,Object> buildVm(MetasAhorro m,
                                                 java.text.DecimalFormat df,
                                                 java.text.SimpleDateFormat sdf) {
        java.util.Map<String,Object> row = new java.util.HashMap<>();
        row.put("id", m.getId());
        row.put("nombre", m.getNombre() == null ? "N/A" : m.getNombre());
        row.put("montoObjetivo", df.format(m.getMontoObjetivo()));
        row.put("montoActual",  df.format(m.getMontoActual()));

        // Calcula el porcentaje de avance hacia la meta
        double progreso = 0.0;
        if (m.getMontoObjetivo() > 0) {
            progreso = (m.getMontoActual() / m.getMontoObjetivo()) * 100.0;
        }
        row.put("progresoPct", String.format("%.1f", progreso));

        // Formatea fechas o muestra "N/A" si no existen
        row.put("fechaInicio", m.getFechaInicio() == null ? "N/A" : sdf.format(m.getFechaInicio()));
        row.put("fechaFin",    m.getFechaFin()    == null ? "N/A" : sdf.format(m.getFechaFin()));
        return row;
    }

    /**
     * Vincula los campos del request a una entidad MetasAhorro.
     * Retorna null si algún obligatorio falta o hay error de parseo (para validar en el controlador).
     */
    private MetasAhorro bindFromRequest(HttpServletRequest request, int userId) {
        String nombre            = request.getParameter("txtNombreMeta");
        String montoObjetivoStr  = request.getParameter("txtMontoObjetivo");
        String montoActualStr    = request.getParameter("txtMontoActual");
        String fechaInicioStr    = request.getParameter("txtFechaInicio");
        String fechaFinStr       = request.getParameter("txtFechaFin");

        // Validación de obligatorios (simple, a nivel de controlador)
        if (nombre == null || nombre.trim().isEmpty() ||
            montoObjetivoStr == null || montoObjetivoStr.trim().isEmpty() ||
            montoActualStr == null   || montoActualStr.trim().isEmpty()   ||
            fechaInicioStr == null   || fechaInicioStr.trim().isEmpty()   ||
            fechaFinStr == null      || fechaFinStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Parseo básico de montos y fechas (formato ISO para inputs type="date")
            double montoObjetivo = Double.parseDouble(montoObjetivoStr);
            double montoActual   = Double.parseDouble(montoActualStr);
            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date fechaInicio = new java.sql.Date(sdfIn.parse(fechaInicioStr).getTime());
            java.sql.Date fechaFin    = new java.sql.Date(sdfIn.parse(fechaFinStr).getTime());

            // Construcción de la entidad lista para persistir
            MetasAhorro meta = new MetasAhorro();
            meta.setNombre(nombre);
            meta.setMontoObjetivo(montoObjetivo);
            meta.setMontoActual(montoActual);
            meta.setFechaInicio(fechaInicio);
            meta.setFechaFin(fechaFin);
            meta.setUserId(userId);
            return meta;
        } catch (Exception e) {
            // Cualquier error de parseo hace fallar la vinculación para informar al usuario
            return null;
        }
    }
}
