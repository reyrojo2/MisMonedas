<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="head.jsp" />
  <title>Reporte de Presupuestos</title>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <% request.setAttribute("activePage", "presupuesto"); %>
    <jsp:include page="navbar.jsp" />

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap 
                  align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2 mb-0">Reporte de Presupuestos</h1>
        <a class="btn btn-primary"
           href="<%= request.getContextPath() %>/PresupuestoController?view=form">
          <i class="bi bi-plus-circle"></i> Nuevo Presupuesto
        </a>
      </div>

      <%
        String error = (String) request.getAttribute("error");
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> rows =
           (List<Map<String,Object>>) request.getAttribute("rows");

        String totalPresupFmt = (String) request.getAttribute("totalPresupFmt");
        String totalGastFmt   = (String) request.getAttribute("totalGastFmt");
        String saldoFmt       = (String) request.getAttribute("saldoFmt");
        Boolean saldoPos      = (Boolean) request.getAttribute("saldoEsPositivo");
        if (saldoPos == null) saldoPos = Boolean.TRUE;
      %>

      <% if (error != null) { %>
        <div class="alert alert-danger" role="alert"><%= error %></div>
      <% } %>

      <% if (rows != null && !rows.isEmpty()) { %>
        <div class="row g-3">
          <div class="col-md-4">
            <div class="alert alert-primary mb-0">
              <div class="d-flex justify-content-between align-items-center">
                <span class="fw-semibold">Total Presupuestado</span>
                <span class="fw-bold">S/ <%= totalPresupFmt %></span>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="alert alert-warning mb-0">
              <div class="d-flex justify-content-between align-items-center">
                <span class="fw-semibold">Total Gastado</span>
                <span class="fw-bold">S/ <%= totalGastFmt %></span>
              </div>
            </div>
          </div>
          <div class="col-md-4">
            <div class="alert <%= (saldoPos ? "alert-success" : "alert-danger") %> mb-0">
              <div class="d-flex justify-content-between align-items-center">
                <span class="fw-semibold"><%= (saldoPos ? "Saldo Disponible" : "Exceso de Gasto") %></span>
                <span class="fw-bold">S/ <%= saldoFmt %></span>
              </div>
            </div>
          </div>
        </div>

        <div class="card mt-3">
          <div class="card-header">Presupuestos Registrados</div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-striped table-hover table-sm align-middle">
                <thead class="table-dark">
                  <tr>
                    <th>ID</th>
                    <th>Categoría</th>
                    <th>Período</th>
                    <th class="text-end">Presupuestado (S/)</th>
                    <th class="text-end">Gastado (S/)</th>
                    <th style="min-width:140px;">Progreso</th>
                    <th class="text-end">Diferencia (S/)</th>
                    <th class="text-center">Estado</th>
                    <th class="text-center">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                <%
                  for (Map<String,Object> row : rows) {
                    int id               = ((Number) row.get("id")).intValue();
                    String categoria     = String.valueOf(row.get("categoria"));
                    String periodo       = String.valueOf(row.get("periodo"));
                    String presupFmt     = String.valueOf(row.get("montoPresupFmt"));
                    String gastFmt       = String.valueOf(row.get("montoGastFmt"));
                    String diffFmt       = String.valueOf(row.get("diffFmt"));
                    String progresoStr   = String.valueOf(row.get("progresoPct")); // solo número
                    String estado        = String.valueOf(row.get("estado"));
                    boolean sobre        = (row.get("sobre") instanceof Boolean) ? (Boolean) row.get("sobre") : false;

                    String pRaw = String.valueOf(row.get("montoPresupRaw")); // para el modal
                    String gRaw = String.valueOf(row.get("montoGastRaw"));
                %>
                  <tr class="<%= sobre ? "table-warning" : "" %>">
                    <td><%= id %></td>
                    <td><%= categoria %></td>
                    <td><%= periodo %></td>
                    <td class="text-end">S/ <%= presupFmt %></td>
                    <td class="text-end <%= sobre ? "text-danger fw-semibold" : "" %>">S/ <%= gastFmt %></td>
                    <td>
                      <div class="progress">
                        <div class="progress-bar <%= sobre ? "bg-danger" : "" %>" role="progressbar"
                             style="width: <%= progresoStr %>%;" 
                             aria-valuenow="<%= progresoStr %>" aria-valuemin="0" aria-valuemax="100">
                          <%= progresoStr %>%
                        </div>
                      </div>
                    </td>
                    <td class="text-end <%= diffFmt.startsWith("-") ? "text-danger fw-semibold" : "text-success fw-semibold" %>">
                      S/ <%= diffFmt %>
                    </td>
                    <td class="text-center">
                      <span class="badge <%= sobre ? "bg-danger" : "bg-success" %>"><%= estado %></span>
                    </td>
					<td class="text-center">
					  <!-- Editar -->
					  <a href="<%= request.getContextPath() %>/PresupuestoController?view=editar&id=<%= id %>"
					     class="btn btn-warning btn-sm">Editar</a>
					
					  <!-- Eliminar con SweetAlert2 -->
					<button type="button" class="btn btn-danger btn-sm"
					        onclick="confirmarEliminar(<%= id %>, '<%= request.getContextPath() %>')">Eliminar</button>

					</td>
                  </tr>
                <%
                  } // for rows
                %>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      <% } else { %>
        <div class="alert alert-info d-flex justify-content-between align-items-center" role="alert">
          <div>No hay presupuestos registrados.</div>
          <a class="btn btn-outline-primary btn-sm"
             href="<%= request.getContextPath() %>/PresupuestoController">
            Registrar primer presupuesto
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
