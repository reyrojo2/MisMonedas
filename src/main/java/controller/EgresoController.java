package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Egreso;

import java.io.IOException;

@WebServlet("/EgresoController")
public class EgresoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EgresoController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// No se manejan peticiones GET para este controlador en este ejemplo
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Egreso objEgreso = new Egreso();
		objEgreso.setDescripcion(request.getParameter("txtDescripcionEgreso"));
		objEgreso.setMonto(Double.parseDouble(request.getParameter("txtMontoEgreso")));
		objEgreso.setFecha(request.getParameter("txtFechaEgreso"));
		objEgreso.setCategoria(request.getParameter("cbCategoriaEgreso")); // Capturar la categoría

		request.setAttribute("egreso", objEgreso);
		request.getRequestDispatcher("guiEgresoReporte.jsp").forward(request, response);
	}
}
