package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Login;

import java.io.IOException;


@WebServlet("/loginController")
public class loginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public loginController() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Login objLog = new Login();
		objLog.setUser(request.getParameter("txtUser"));
		objLog.setPass(request.getParameter("txtPass"));
		
	    if (objLog.acceder()) {
	        request.setAttribute("login", objLog);
	        request.getRequestDispatcher("guiDashboard.jsp").forward(request, response);
	    } else {
	        request.setAttribute("error", "Usuario o contraseña incorrectos");
	        request.getRequestDispatcher("login.jsp").forward(request, response);
	    }
	}

}
