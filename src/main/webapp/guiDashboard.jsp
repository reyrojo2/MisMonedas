<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Dashboard - MisMonedas</title>
  <jsp:include page="head.jsp" />
</head>
<body>
  <!-- Navbar -->
  <jsp:include page="navbar.jsp" />

  <%
    // Recuperar el ViewModel preparado por el DashboardController
    modelvm.DashboardViewModel vm =
      (modelvm.DashboardViewModel) request.getAttribute("dashboard");
    if (vm == null) {
  %>
    <div class="container py-4">
      <div class="alert alert-danger">No hay datos del dashboard.</div>
    </div>
  <%
      return;
    }
  %>

  <!-- Contenido -->
  <div class="container-fluid">
    <div class="row">
      <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
        <h1 class="mt-3">Dashboard</h1>
		<%
		  String startParam = request.getParameter("startDate");
		  String endParam   = request.getParameter("endDate");
		  String monthParam = request.getParameter("month");
		%>
		
		<form class="row g-2 align-items-end mb-4" method="get" action="<%= request.getContextPath() %>/DashboardController">
		  <div class="col-auto">
		    <label for="month" class="form-label">Filtrar por mes</label>
		    <input type="month" class="form-control" id="month" name="month"
		           value="<%= (monthParam != null ? monthParam : "") %>">
		  </div>
		
		  <div class="col-auto">
		    <label for="startDate" class="form-label">Desde</label>
		    <input type="date" class="form-control" id="startDate" name="startDate"
		           value="<%= (startParam != null ? startParam : "") %>">
		  </div>
		
		  <div class="col-auto">
		    <label for="endDate" class="form-label">Hasta</label>
		    <input type="date" class="form-control" id="endDate" name="endDate"
		           value="<%= (endParam != null ? endParam : "") %>">
		  </div>
		
		  <div class="col-auto">
		    <button type="submit" class="btn btn-primary">Aplicar</button>
		    <a class="btn btn-outline-danger" href="<%= request.getContextPath() %>/DashboardController">Limpiar</a>
		  </div>
		
		  <div class="col-12">
		    <small class="text-muted d-block mt-2">
		      Consejo: si seleccionas <code>un mes</code>, se ignora la <code>fecha de inicio</code>/<code>fecha final</code> y usa todo ese mes.
		    </small>
		  </div>
		</form>
		<!-- Totales -->
		<div class="row mt-4 g-3 align-items-stretch">
		
		  <!-- TOTAL INGRESOS -->
		  <div class="col-12 col-md-4">
		    <div class="card border-0 shadow-sm h-100 rounded-3">
		      <div class="card-header text-center text-white bg-teal text-uppercase fw-bold fs-5">
		        Total Ingresos
		      </div>
		      <div class="card-body text-center">
		        <div class="display-6 fw-bold text-dark">
		          S/ <%= vm.getTotalIngresos() %>
		        </div>
		      </div>
		    </div>
		  </div>
		
		  <!-- TOTAL GASTOS -->
		  <div class="col-12 col-md-4">
		    <div class="card border-0 shadow-sm h-100 rounded-3">
		      <div class="card-header text-center text-white bg-danger text-uppercase fw-bold fs-5">
		        Total Gastos
		      </div>
		      <div class="card-body text-center">
		        <div class="display-6 fw-bold text-dark">
		          S/ <%= vm.getTotalEgresos() %>
		        </div>
		      </div>
		    </div>
		  </div>
		
		  <!-- BALANCE / GANANCIA -->
		  <div class="col-12 col-md-4">
		    <div class="card border-0 shadow-sm h-100 rounded-3">
		      <div class="card-header text-center text-dark bg-warning text-uppercase fw-bold fs-5">
		        Ganancia
		      </div>
		      <div class="card-body text-center">
		        <div class="display-6 fw-bold text-dark">
		          S/ <%= (vm.getTotalIngresos() - vm.getTotalEgresos()) %>
		        </div>
		      </div>
		    </div>
		  </div>
		
		</div>


        <!-- Gráficos -->
        <div class="row mt-3">
          <div class="col-md-6">
            <h4>Ingresos por Categoría</h4>
            <canvas id="ingresosChart" class="dashboard-chart"></canvas>
          </div>
          <div class="col-md-6">
            <h4>Egresos por Categoría</h4>
            <canvas id="egresosChart" class="dashboard-chart"></canvas>
          </div>
        </div>

        <!-- Resumen de Presupuestos -->
        <div class="row mt-4">
          <div class="col-12">
            <h4>Resumen de Presupuestos</h4>
            <table class="table">
              <thead>
                <tr>
                  <th>Categoría</th>
                  <th>Monto Presupuestado</th>
                  <th>Monto Gastado</th>
                  <th>Periodo</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
              <%
                java.util.List<modelvm.PresupuestoVM> pres = vm.getPresupuestos();
                if (pres != null && !pres.isEmpty()) {
                  for (modelvm.PresupuestoVM p : pres) {
              %>
                <tr>
                  <td><%= p.getCategoria() %></td>
                  <td><%= p.getMontoPresupuestado() %></td>
                  <td><%= p.getMontoGastado() %></td>
                  <td><%= p.getPeriodo() %></td>
                  <td><%= p.getEstado() %></td>
                </tr>
              <%
                  }
                } else {
              %>
                <tr>
                  <td colspan="5" class="text-center">No hay presupuestos registrados.</td>
                </tr>
              <%
                }
              %>
              </tbody>
            </table>
          </div>
        </div>


      </main>
    </div>
  </div>

  <jsp:include page="footer.jsp" />
</body>
</html>
