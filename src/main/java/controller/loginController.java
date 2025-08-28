package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import service.AuthService;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/loginController")
public class loginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService auth;

    @Override
    public void init() throws ServletException {
        this.auth = new AuthService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("txtUser");
        String password = request.getParameter("txtPass");

        Optional<User> optUser = auth.authenticate(username, password);

        if (optUser.isPresent()) {
            // Guarda SOLO el DTO del usuario (sin hash)
            User u = optUser.get();
            u.setPasswordHash(null);

            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", u);
            session.setAttribute("loggedFullName", u.getFullName());

            response.sendRedirect(request.getContextPath() + "/DashboardController");
        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // --- 1) Autenticación básica (mejor aún si usas un Filter) ---
        HttpSession session = req.getSession(false);
        model.User loggedInUser = (session != null) ? (model.User) session.getAttribute("loggedInUser") : null;
        if (loggedInUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        String tipo   = req.getParameter("tipo");

        // --- Logout ---
        if ("logout".equals(action)) {
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // --- Mi perfil ---
        if ("perfil".equals(tipo)) {
            // Deja listo lo que la vista necesita mostrar (presentación)
            req.setAttribute("activePage", "perfil");
            req.setAttribute("username", loggedInUser.getUsername());
            req.setAttribute("fullName", loggedInUser.getFullName());
            req.setAttribute("email",    loggedInUser.getEmail());
            req.getRequestDispatcher("/guiPerfil.jsp").forward(req, resp);
            return;
        }
        
        // --- Si no coincide ninguna ruta conocida ---
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Ruta no encontrada");
    }
}