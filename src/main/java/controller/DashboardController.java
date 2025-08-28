package controller;

import dao.EgresoDAOImpl;
import dao.IngresoDAOImpl;
import dao.PresupuestoDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import modelvm.DashboardViewModel;
import service.DashboardService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

@WebServlet("/DashboardController")
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        this.dashboardService = new DashboardService(
                new IngresoDAOImpl(),
                new EgresoDAOImpl(),
                new PresupuestoDAOImpl()
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("loggedInUser") : null;
        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // --- 1) Leer parámetros ---
            String startParam = request.getParameter("startDate"); // yyyy-MM-dd
            String endParam   = request.getParameter("endDate");   // yyyy-MM-dd
            String monthParam = request.getParameter("month");     // yyyy-MM

            LocalDate startDate;
            LocalDate endDate;

            if (monthParam != null && !monthParam.isEmpty()) {
                YearMonth ym = YearMonth.parse(monthParam); // "yyyy-MM"
                startDate = ym.atDay(1);
                endDate   = ym.atEndOfMonth();
            } else if (startParam != null && !startParam.isEmpty() &&
                       endParam   != null && !endParam.isEmpty()) {
                startDate = LocalDate.parse(startParam); // "yyyy-MM-dd"
                endDate   = LocalDate.parse(endParam);
            } else {
                YearMonth now = YearMonth.now();
                startDate = now.atDay(1);
                endDate   = now.atEndOfMonth();
            }

            // --- 2) Convertir a java.util.Date para el Service ---
            java.util.Date start = java.sql.Date.valueOf(startDate);
            java.util.Date end   = java.sql.Date.valueOf(endDate);

            // --- 3) Construir VM filtrado por fechas ---
            DashboardViewModel vm = dashboardService.buildForUser(
                    loggedInUser.getId(),
                    start,
                    end
            );

            // --- 4) Pasar a la vista ---
            request.setAttribute("dashboard", vm);
            request.setAttribute("activePage", "dashboard");
            String dashboardJson = new com.google.gson.Gson().toJson(vm);
            request.setAttribute("dashboardJson", dashboardJson);

            request.getRequestDispatcher("/guiDashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar los datos del dashboard: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
