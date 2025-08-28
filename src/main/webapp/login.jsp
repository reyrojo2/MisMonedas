<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<!-- INICIO DEL HEAD -->
<jsp:include page="head.jsp"></jsp:include>
<!-- FIN DEL HEAD -->

<body id=login>
	<div class="container d-flex flex-column">
		<div class="row align-items-center justify-content-center g-0 min-vh-100">
			<div class="col-12 col-md-8 col-lg-6 col-xxl-4 py-8 py-xl-0">
				<div class="card smooth-shadow-md">
					<div class="card-body p-6">

						<%
						    String error = (String) request.getAttribute("error");
						    if (error != null) {
						%>
						    <div class="alert alert-danger text-center"><%= error %></div>
						<%
						    }
						%>
						<div class="mb-1 text-center">
								<img name="logo" src="../PROYECTOLP1/img/logo.png" class="img-fluid" style="max-width: 150px;" alt="Image">
						</div>
						<p class="mb-4">Por favor, ingrese sus credenciales.</p>
						<form class="needs-validation" action="loginController" method="post" novalidate>
							
							<!-- Usuario -->
							<div class="form-floating mb-3">
							<label for="txtUser">Usuario</label>
								<input class="form-control" type="text" name="txtUser" id="txtUser" placeholder="Usuario" required autofocus>
								<div class="invalid-tooltip">
									Ingrese el usuario
								</div>
							</div>

							<!-- Contraseña -->
							<div class="form-floating mb-3">
								<input class="form-control" type="password" name="txtPass" id="txtPass" placeholder="Password" required>
								<label for="txtPass">Contraseña</label>
								<div class="invalid-feedback">
									Ingrese la clave
								</div>
							</div>
							<div class="text-center mb-3">
							  <a href="registro.jsp">¿No tienes cuenta? Regístrate</a>
							</div>

							<!-- Botón -->
							<div class="mb-3">
								<input class="btn btn-success w-100" type="submit" value="INGRESAR">
							</div>
						</form>

					</div> <!-- card-body -->
				</div> <!-- card -->
			</div> <!-- col -->
		</div> <!-- row -->
	</div> <!-- container -->
</body>

<!-- INICIO DEL FOOTER -->
<jsp:include page="footer.jsp"></jsp:include>
<!-- FIN DEL FOOTER -->

</html>
