package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import security.PasswordUtil;

import java.io.IOException;

import dao.UserDAOImpl;
import interfaces.UserDao;


@WebServlet("/registroController")
public class registroController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("txtUser");
        String password = request.getParameter("txtPass");
        String fullName = request.getParameter("txtName");
        String email    = request.getParameter("txtEmail");

        if (userDao.existsByEmail(email)) {
            request.setAttribute("error", "El correo " + email + " ya está registrado. Intenta con otro.");
            
            request.setAttribute("val_user", username);
            request.setAttribute("val_name", fullName);
            
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Hash de la contraseña
        String hashed = PasswordUtil.hash(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(hashed);
        newUser.setFullName(fullName);
        newUser.setEmail(email);

        if (userDao.save(newUser)) {
            response.sendRedirect("login.jsp?msg=Usuario registrado con éxito");
        } else {
            request.setAttribute("error", "No se pudo registrar el usuario");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
