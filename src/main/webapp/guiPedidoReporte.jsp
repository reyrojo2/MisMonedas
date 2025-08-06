<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Pedido" %>
<!DOCTYPE html>
<html>
	<jsp:include page="head.jsp"></jsp:include>
		<body style="padding-bottom: 50px;">
		
		<%
			Pedido pedido = (Pedido) request.getAttribute("pedido");
		
		%>
				
		<div class="container">
			<div class="row justify-content-center mt-5">
				<div class="col-lg-4 col-md-6 col-sm-12">
					<div class="card shadow mt-4" style="background-color: rgba(255, 255, 255, 0.9); border-radius: 15px;">
						<div class="card-body">
		
							<h1 class="text-center text-primary">Pizza Raúl</h1>
		
							<div>
								<p class="card text-center">
									¡Disfruta de una experiencia deliciosa!<br>
									www.pizzaraul.com.pe<br>
									RUC 2042943427
								</p>
							</div>
		
							<!-- TABLA DE RESUMEN -->
							<table class="table table-bordered mt-3">
								<tbody>
									<tr>
										<th>Dirección</th>
										<td>${pedido.direccion}</td>
									</tr>
									<tr>
										<th>Celular</th>
										<td>${pedido.celular}</td>
									</tr>
									<tr>
										<th>Tamaño</th>
										<td>${pedido.tamano}</td>
									</tr>
									<tr>
										<th>Tipo de Masa</th>
										<td>${pedido.tipo}</td>
									</tr>
									<tr>
										<th>Cantidad</th>
										<td>${pedido.cantidad}</td>
									</tr>
									<tr>
										<th>Total a Pagar</th>
										<td>S/ ${pedido.total}</td>
									</tr>
									<tr>
										<th>Comentarios</th>
										<td>${pedido.comentarios}</td>
									</tr>
								</tbody>
							</table>
		
							<!-- Botón -->
							<div class="mb-3 text-center">
								<a href="guiPedido.jsp" class="btn btn-primary w-100">Otro pedido</a>
							</div>
		
						</div> <!-- card-body -->
					</div> <!-- card -->
				</div> <!-- col -->
			</div> <!-- row -->
		</div> <!-- container -->

	
	<jsp:include page="footer.jsp"></jsp:include>
		<script src="js/pz.js"></script>
	</body>
</html>
