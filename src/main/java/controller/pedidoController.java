package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import model.Pedido;

@WebServlet("/pedidoController")
public class pedidoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public pedidoController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Pedido objPed = new Pedido();
		objPed.setDireccion(request.getParameter("txtDireccion"));
		objPed.setCelular(request.getParameter("txtCelular"));
		objPed.setTamano(request.getParameter("rbTamano"));
		objPed.setTipo(request.getParameter("cbTipo"));
	    int cantidad = Integer.parseInt(request.getParameter("txtCantidad"));
	    objPed.setCantidad(cantidad);
		objPed.setComentarios(request.getParameter("txtComentario"));
		
		
		request.setAttribute("pedido", objPed);
		request.getRequestDispatcher("guiPedidoReporte.jsp").forward(request, response);
	}
	

}
