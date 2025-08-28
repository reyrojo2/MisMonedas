<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="head.jsp" />
  <title>Mi Perfil</title>
</head>
<body>
<div class="container-fluid">
  <div class="row">

    <%
      // Solo para resaltar el menú activo en navbar
      request.setAttribute("activePage", "perfil");
      String username = (String) request.getAttribute("username");
      String fullName = (String) request.getAttribute("fullName");
      String email    = (String) request.getAttribute("email");
    %>

    <jsp:include page="navbar.jsp" />

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Mi Perfil</h1>
      </div>

      <div class="card mb-4">
        <div class="card-header">Información del Usuario</div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <p><strong>Usuario:</strong> <%= username %></p>
              <p><strong>Contraseña:</strong> **********</p>
              <p><strong>Nombre completo:</strong> <%= fullName %></p>
              <p><strong>Email:</strong> <%= email %></p>
            </div>
          </div>
        </div>
      </div>
    </main>

  </div>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>
