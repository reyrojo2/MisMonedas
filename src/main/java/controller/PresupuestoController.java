// src/main/java/controller/PresupuestoController.java
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dao.CategoriaDAO;
import dao.PresupuestoDAOImpl;
import interfaces.PresupuestoDao;
import model.User;
import service.PresupuestoService;
import modelvm.PresupuestoVM;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@WebServlet("/PresupuestoController")
public class PresupuestoController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PresupuestoService service;
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

    @Override
    public void init() {
        PresupuestoDao dao = new PresupuestoDAOImpl();
        service = new PresupuestoService(dao);
    }

    private User requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) { response.sendRedirect("login.jsp"); return null; }
        Object u = session.getAttribute("loggedInUser");
        if (!(u instanceof User)) { response.sendRedirect("login.jsp"); return null; }
        return (User) u;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = requireUser(request, response);
        if (user == null) return;

        String view = request.getParameter("view");
        String error = (String) request.getAttribute("error");
        if (error != null) request.setAttribute("error", error);
        // ----------- NUEVO (form vacío) -----------
        if ("form".equals(view)) {
            CategoriaDAO catDao = new CategoriaDAO();
            List<Map<String,Object>> categorias = catDao.listarPorTipo("egreso");

            request.setAttribute("activePage", "presupuesto");
            request.setAttribute("modo", "nuevo");
            request.setAttribute("categorias", categorias);

            // Puedes pasar valores default si quieres, ej:
            request.setAttribute("periodoSel", "Mensual"); // opcional

            request.getRequestDispatcher("guiPresupuesto.jsp").forward(request, response);
            return;
        }
        
        // ----------- REPORTE -----------
        if ("reporte".equals(view)) {
            request.setAttribute("activePage", "presupuesto");

            List<PresupuestoVM> vms = service.listarVMPorUsuario(user.getId());

            double totalPresup = 0.0;
            double totalGast   = 0.0;
            for (PresupuestoVM p : vms) {
                totalPresup += p.getMontoPresupuestado();
                totalGast   += p.getMontoGastado();
            }
            double saldo = totalPresup - totalGast;

            List<Map<String, Object>> rows = toRowsConCalculos(vms);

            request.setAttribute("rows", rows);
            request.setAttribute("totalPresupFmt", MONEY.format(totalPresup));
            request.setAttribute("totalGastFmt",   MONEY.format(totalGast));
            request.setAttribute("saldoFmt",       MONEY.format(Math.abs(saldo)));
            request.setAttribute("saldoEsPositivo", saldo >= 0);

            request.getRequestDispatcher("guiPresupuestoReporte.jsp").forward(request, response);
            return; // IMPORTANTE
        }

        // ----------- EDITAR (form precargado) -----------
        if ("editar".equals(view)) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                response.sendRedirect("PresupuestoController?view=reporte");
                return;
            }

            PresupuestoVM vm = service.obtenerVMPorId(user.getId(), id);
            if (vm == null) {
                request.setAttribute("error", "No se encontró el presupuesto especificado.");
                response.sendRedirect("PresupuestoController?view=reporte");
                return;
            }

            CategoriaDAO catDao = new CategoriaDAO();
            List<Map<String,Object>> categorias = catDao.listarPorTipo("egreso");

            request.setAttribute("activePage", "presupuesto");
            request.setAttribute("modo", "editar");
            request.setAttribute("presuEdit", vm);              // <<--- CLAVE: bean completo
            request.setAttribute("categorias", categorias);

            // Si tu DAO no devuelve categoria_id, al menos envía el nombre para marcar selected por nombre
            request.setAttribute("categoriaSelNombre", vm.getCategoria());
            request.setAttribute("periodoSel", vm.getPeriodo());

            request.getRequestDispatcher("guiPresupuesto.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = requireUser(request, response);
        if (user == null) return;

        String action = request.getParameter("accion");
        HttpSession session = request.getSession();
        String error = null;

        try {
            if ("guardar".equals(action)) {
                service.crear(
                        user.getId(),
                        request.getParameter("cbCategoriaPresupuesto"),
                        request.getParameter("txtMontoPresupuestado"),
                        request.getParameter("cbPeriodoPresupuesto")
                );
                session.setAttribute("flash_success", "Presupuesto registrado con éxito.");
                response.sendRedirect("PresupuestoController?view=reporte");
                return;

            } else if ("actualizar".equals(action)) {
                String id = request.getParameter("id");

                service.actualizarPorId( // Debe existir en Service/DAO
                        user.getId(),
                        id,
                        request.getParameter("cbCategoriaPresupuesto"),
                        request.getParameter("txtMontoPresupuestado"),
                        request.getParameter("cbPeriodoPresupuesto")
                );
                session.setAttribute("flash_success", "Presupuesto actualizado correctamente.");
                response.sendRedirect("PresupuestoController?view=reporte");
                return;

            } else if ("delete".equals(action)) {
                service.eliminar(request.getParameter("id"));
                response.sendRedirect("PresupuestoController?view=reporte");
                return;

            } else {
                error = "Acción no reconocida.";
            }
        } catch (IllegalArgumentException iae) {
            error = iae.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            error = (ex.getMessage() != null ? ex.getMessage() : "Error al procesar la solicitud.");
        }

        request.setAttribute("error", error);
        doGet(request, response);
    }

    // ----- Helpers de aplanado para la vista (sin model/modelvm en JSP) -----
    private List<Map<String, Object>> toRowsConCalculos(List<PresupuestoVM> lista) {
        List<Map<String, Object>> out = new ArrayList<>();
        if (lista == null) return out;

        for (PresupuestoVM p : lista) {
            double presup = p.getMontoPresupuestado();
            double gast   = p.getMontoGastado();
            double diff   = presup - gast;
            boolean sobre = gast > presup;

            double progreso = (presup > 0) ? (gast / presup) * 100.0 : 0.0;
            if (progreso < 0) progreso = 0;
            if (progreso > 100) progreso = 100;

            Map<String, Object> r = new HashMap<>();
            r.put("id", p.getId());
            r.put("categoria", esc(p.getCategoria()));
            r.put("periodo", esc(p.getPeriodo()));

            r.put("montoPresupFmt", MONEY.format(presup));
            r.put("montoGastFmt",   MONEY.format(gast));
            r.put("diffFmt",        MONEY.format(diff));

            r.put("progresoPct", String.format(java.util.Locale.US, "%.0f", progreso));
            r.put("estado",  esc(p.getEstado()));
            r.put("sobre",   sobre);

            r.put("montoPresupRaw", String.format(java.util.Locale.US, "%.2f", presup));

            out.add(r);
        }
        return out;
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;")
                .replace("\"","&quot;")
                .replace("'","&#39;");
    }
}
