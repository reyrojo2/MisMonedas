package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Ingreso;

import java.io.IOException;

@WebServlet("/IngresoController")
public class IngresoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public IngresoController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// No se manejan peticiones GET para este controlador en este ejemplo
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Ingreso objIngreso = new Ingreso();
		objIngreso.setDescripcion(request.getParameter("txtDescripcionIngreso"));
		objIngreso.setMonto(Double.parseDouble(request.getParameter("txtMontoIngreso")));
		objIngreso.setFecha(request.getParameter("txtFechaIngreso"));
		objIngreso.setCategoria(request.getParameter("cbCategoriaIngreso")); // Capturar la categoría

		request.setAttribute("ingreso", objIngreso);
		request.getRequestDispatcher("guiIngresoReporte.jsp").forward(request, response);
	}
}
