<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
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
          <h4 class="mb-3 text-center">Crear cuenta</h4>

          <form class="needs-validation" action="registroController" method="post" novalidate>

            <div class="form-floating mb-3">
              <input class="form-control" type="text" name="txtUser" id="txtUser" placeholder="Usuario" required>
              <label for="txtUser">Usuario</label>
              <div class="invalid-feedback">Ingrese el usuario</div>
            </div>

            <div class="form-floating mb-3">
              <input class="form-control" type="password" name="txtPass" id="txtPass" placeholder="Clave" required>
              <label for="txtPass">Contraseña</label>
              <div class="invalid-feedback">Ingrese la clave</div>
            </div>

            <div class="form-floating mb-3">
              <input class="form-control" type="text" name="txtName" id="txtName" placeholder="Nombre completo" required>
              <label for="txtName">Nombre completo</label>
              <div class="invalid-feedback">Ingrese su nombre</div>
            </div>

            <div class="form-floating mb-4">
              <input class="form-control" type="email" name="txtEmail" id="txtEmail" placeholder="correo@dominio.com" required>
              <label for="txtEmail">Correo</label>
              <div class="invalid-feedback">Ingrese un correo válido</div>
            </div>

            <div class="mb-3">
              <input class="btn btn-primary w-100" type="submit" value="REGISTRAR">
            </div>

            <div class="text-center">
              <a href="login.jsp">¿Ya tienes cuenta? Inicia sesión</a>
            </div>
          </form>

        </div>
      </div>
    </div>
  </div>
</div>
</body>
<jsp:include page="footer.jsp"></jsp:include>
</html>
