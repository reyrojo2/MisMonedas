<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Reporte de Ingresos</title>
  <jsp:include page="head.jsp" />
</head>
<body>
<div class="container-fluid">
  <div class="row">

    <%-- Activar ítem del navbar --%>
    <%
      request.setAttribute("activePage", "ingresos");
    %>

    <jsp:include page="navbar.jsp" />

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">

      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2 mb-0">Reporte de Ingresos</h1>
        <a class="btn btn-success"
           href="<%= request.getContextPath() %>/montosController?tipo=ingreso&accion=form">
          <i class="bi bi-plus-circle"></i> Nuevo Ingreso
        </a>
      </div>

      <%
        // Datos enviados por el controlador (ya desacoplados)
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> ingresos =
            (List<Map<String,Object>>) request.getAttribute("ingresos");

        // Formateadores (presentación)
        DecimalFormat df = new DecimalFormat("#,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Double totalIngresos = (Double) request.getAttribute("totalIngresos");
        if (totalIngresos != null) {
      %>
        <div class="alert alert-success">
          <h5 class="mb-0">
            Total de Ingresos:
            <span class="fw-semibold">S/ <%= df.format(totalIngresos) %></span>
          </h5>
        </div>
      <% } %>

      <% if (ingresos != null && !ingresos.isEmpty()) { %>
        <div class="card">
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-striped table-hover table-sm align-middle">
                <thead class="table-dark">
                  <tr>
                    <th>#</th>
                    <th>Fecha</th>
                    <th>Categoría</th>
                    <th>Descripción</th>
                    <th class="text-end">Monto (S/)</th>
                  </tr>
                </thead>
                <tbody>
                <%
                  for (int i = 0; i < ingresos.size(); i++) {
                    Map<String,Object> row = ingresos.get(i);
                    java.util.Date fecha = (java.util.Date) row.get("fecha"); // java.sql.Date ok
                    String categoria     = (String) row.get("categoria");
                    String descripcion   = (String) row.get("descripcion");
                    Double monto         = (row.get("monto") instanceof Number)
                                           ? ((Number) row.get("monto")).doubleValue()
                                           : null;
                %>
                  <tr>
                    <td><%= (i + 1) %></td>
                    <td><%= (fecha != null) ? sdf.format(fecha) : "" %></td>
                    <td><%= (categoria != null) ? categoria : "" %></td>
                    <td><%= (descripcion != null) ? descripcion : "" %></td>
                    <td class="fw-bold text-success text-end">
                      S/ <%= (monto != null) ? df.format(monto) : "0.00" %>
                    </td>
                  </tr>
                <% } %>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      <% } else { %>
        <div class="alert alert-info d-flex justify-content-between align-items-center" role="alert">
          <div>No hay ingresos registrados.</div>
          <a class="btn btn-outline-success btn-sm"
             href="<%= request.getContextPath() %>/montosController?tipo=ingreso&accion=form">
            Registrar primer ingreso
          </a>
        </div>
      <% } %>

    </main>
  </div>
</div>

<jsp:include page="footer.jsp" />
<jsp:include page="flash-messages.jsp" />
</body>
</html>
