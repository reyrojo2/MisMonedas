<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Reporte de Egresos</title>
  <jsp:include page="head.jsp" />
</head>
<body>
<div class="container-fluid">
  <div class="row">

    <%-- Activar ítem del navbar --%>
    <%
      request.setAttribute("activePage", "egresos");
    %>

    <jsp:include page="navbar.jsp" />

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">

      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2 mb-0">Reporte de Egresos</h1>
        <a class="btn btn-danger"
           href="<%= request.getContextPath() %>/montosController?tipo=egreso&accion=form">
          <i class="bi bi-plus-circle"></i> Nuevo Egreso
        </a>
      </div>

      <%
        // Datos enviados por el controlador (ya desacoplados)
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> egresos =
            (List<Map<String,Object>>) request.getAttribute("egresos");

        // Formateadores (presentación)
        DecimalFormat df = new DecimalFormat("#,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Double totalEgresos = (Double) request.getAttribute("totalEgresos");
        if (totalEgresos != null) {
      %>
        <div class="alert alert-danger">
          <h5 class="mb-0">
            Total de Egresos:
            <span class="fw-semibold">S/ <%= df.format(totalEgresos) %></span>
          </h5>
        </div>
      <% } %>

      <% if (egresos != null && !egresos.isEmpty()) { %>
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
                  for (int i = 0; i < egresos.size(); i++) {
                    Map<String,Object> row = egresos.get(i);
                    java.util.Date fecha = (java.util.Date) row.get("fecha");      // java.sql.Date ok
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
                    <td class="fw-bold text-danger text-end">
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
          <div>No hay egresos registrados.</div>
          <a class="btn btn-outline-danger btn-sm"
             href="<%= request.getContextPath() %>/montosController?tipo=egreso&accion=form">
            Registrar primer egreso
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
