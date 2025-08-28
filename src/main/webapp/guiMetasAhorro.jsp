<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="head.jsp" />
  <title>Registrar Meta de Ahorro</title>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <% request.setAttribute("activePage", "metas"); %>
    <jsp:include page="navbar.jsp" />

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap 
                  align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Nueva Meta de Ahorro</h1>
          	<a class="btn btn-info"
				href="<%= request.getContextPath() %>/MetasAhorroController?tipo=meta&action=listar">
				<i class="bi bi-plus-circle"></i> Ver Metas
			</a>
      </div>

      <div class="card mb-4">
        <div class="card-header">Gestionar Metas de Ahorro</div>
        <div class="card-body">
			<%
			  model.MetasAhorro metaEdit = (model.MetasAhorro) request.getAttribute("metaEdit");
			  String accionForm = (metaEdit != null) ? "update" : "save";
			%>
          <form action="MetasAhorroController?accion=<%= accionForm %>" method="post" class="needs-validation" novalidate>
              <% if (metaEdit != null) { %>
			    <input type="hidden" name="id" value="<%= metaEdit.getId() %>">
			  <% } %>

            <div class="form-group">
              <label for="txtNombreMeta">Nombre de la Meta</label>
              <input type="text" class="form-control" id="txtNombreMeta" name="txtNombreMeta" value="<%= (metaEdit != null && metaEdit.getNombre()!=null) ? metaEdit.getNombre() : "" %>" required>
              <div class="invalid-feedback">Por favor, ingrese el nombre de la meta.</div>
            </div>

            <div class="form-group">
              <label for="txtMontoObjetivo">Monto Objetivo</label>
              <input type="number" step="0.01" class="form-control" id="txtMontoObjetivo" name="txtMontoObjetivo" value="<%= (metaEdit != null) ? String.valueOf(metaEdit.getMontoObjetivo()) : "" %>" required>
              <div class="invalid-feedback">Por favor, ingrese el monto objetivo.</div>
            </div>

            <div class="form-group">
              <label for="txtMontoActual">Monto Actual</label>
              <input type="number" step="0.01" class="form-control" id="txtMontoActual" name="txtMontoActual" value="<%= (metaEdit != null) ? String.valueOf(metaEdit.getMontoActual()) : "0" %>" required>
              <div class="invalid-feedback">Por favor, ingrese el monto actual.</div>
            </div>

            <div class="form-group">
              <label for="txtFechaInicio">Fecha de Inicio</label>
              <input type="date" class="form-control" id="txtFechaInicio" name="txtFechaInicio" value="<%= (request.getAttribute("metaEditFechaInicio") != null) ? request.getAttribute("metaEditFechaInicio") : "" %>" required>
              <div class="invalid-feedback">Por favor, ingrese la fecha de inicio.</div>
            </div>

            <div class="form-group">
              <label for="txtFechaFin">Fecha de Fin</label>
              <input type="date" class="form-control" id="txtFechaFin" name="txtFechaFin" value="<%= (request.getAttribute("metaEditFechaFin") != null) ? request.getAttribute("metaEditFechaFin") : "" %>" required>
              <div class="invalid-feedback">Por favor, ingrese la fecha de fin.</div>
            </div>

            <button type="submit" class="btn btn-primary mt-3"><%= (metaEdit != null) ? "Actualizar Meta" : "Guardar Meta" %></button>
          </form>
        </div>
      </div>
    </main>
  </div>
</div>

<jsp:include page="footer.jsp" />
<script src="js/validation.js"></script>
</body>
</html>
