<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <jsp:include page="head.jsp" />
  <title>Reporte Metas de Ahorro</title>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <% request.setAttribute("activePage", "metas"); %>
    <jsp:include page="navbar.jsp" />

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap 
                  align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2 mb-0">Listado de Metas de Ahorro</h1>
	        <a class="btn btn-danger"
	           href="<%= request.getContextPath() %>/MetasAhorroController?tipo=meta&accion=form">
	          <i class="bi bi-plus-circle"></i> Nueva Meta
	        </a>
      </div>

      <div class="card">
        <div class="card-header">Metas Registradas</div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-striped table-sm">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nombre</th>
                  <th>Monto Objetivo</th>
                  <th>Monto Actual</th>
                  <th>Progreso</th>
                  <th>Fecha Inicio</th>
                  <th>Fecha Fin</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                <%
                  @SuppressWarnings("unchecked")
                  List<Map<String,Object>> metasVm = 
                     (List<Map<String,Object>>) request.getAttribute("metasVm");

                  if (metasVm != null && !metasVm.isEmpty()) {
                    for (Map<String,Object> meta : metasVm) {
                      String id           = String.valueOf(meta.get("id"));
                      String nombre       = String.valueOf(meta.get("nombre"));
                      String objetivoStr  = String.valueOf(meta.get("montoObjetivo"));
                      String actualStr    = String.valueOf(meta.get("montoActual"));
                      String progresoStr  = String.valueOf(meta.get("progresoPct"));
                      String fechaIniStr  = String.valueOf(meta.get("fechaInicio"));
                      String fechaFinStr  = String.valueOf(meta.get("fechaFin"));
                %>
                <tr>
                  <td><%= id %></td>
                  <td><%= nombre %></td>
                  <td><%= objetivoStr %></td>
                  <td><%= actualStr %></td>
                  <td>
                    <div class="progress">
                      <div class="progress-bar" role="progressbar"
                           style="width: <%= progresoStr %>%;" 
                           aria-valuenow="<%= progresoStr %>" aria-valuemin="0" aria-valuemax="100">
                        <%= progresoStr %>%
                      </div>
                    </div>
                  </td>
                  <td><%= fechaIniStr %></td>
                  <td><%= fechaFinStr %></td>
                  <td>
                      <a href="<%= request.getContextPath() %>/MetasAhorroController?accion=edit&id=<%= id %>" 
     						class="btn btn-warning btn-sm">Editar</a>
					  <button type="button" class="btn btn-danger btn-sm"
					          onclick="confirmarEliminarMeta(<%= id %>, '<%= request.getContextPath() %>', 'tipo=meta')">
					    Eliminar
					  </button>
                  </td>
                </tr>
                <%
                    }
                  } else {
                %>
                <tr>
                  <td colspan="8" class="text-center">
                    No hay metas de ahorro registradas.
                  </td>
                </tr>
                <%
                  }
                %>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>
  </div>
</div>

<jsp:include page="footer.jsp" />
<jsp:include page="flash-messages.jsp" />
</body>
</html>
