<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro de Ingreso</title>
    <jsp:include page="head.jsp" />
</head>
<body>
    <div class="container-fluid">
        <div class="row">
        <%
            request.setAttribute("activePage", "ingresos");
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> categorias = (List<Map<String,Object>>) request.getAttribute("categorias");
            String ctx = request.getContextPath();
        %>

            <!-- Navbar + Sidebar -->
            <jsp:include page="navbar.jsp" />
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Gestión de Ingresos</h1>
                    <a class="btn btn-info"
			           href="<%= request.getContextPath() %>/montosController?tipo=ingreso">
			          <i class="bi bi-plus-circle"></i> Ver Reporte de ingresos
			        </a>
                </div>

                <div class="bg-white p-4 rounded shadow mt-3">
                    <h3 class="text-center mb-4 text-secondary">Registrar Nuevo Ingreso</h3>
                    <form action="montosController?tipo=ingreso&accion=guardar" method="post" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="txtMontoIngreso" class="form-label fw-semibold text-secondary">Monto del Ingreso (S/)</label>
                            <input type="number" step="0.01" class="form-control" id="txtMontoIngreso" name="txtMontoIngreso" placeholder="Ej: 150.75" required min="0.01">
                            <div class="invalid-feedback">
                                Por favor, ingrese un monto válido (mayor a 0).
                            </div>
                        </div>

						<div class="mb-3">
						  <label for="cbCategoriaIngreso" class="form-label fw-semibold text-secondary">Categoría</label>
						  <select class="form-select" id="cbCategoriaIngreso" name="cbCategoriaIngreso" required>
						    <option value="" disabled selected>Seleccione una categoría</option>
							  <%
							    if (categorias != null) {
							      for (Map<String,Object> c : categorias) {
							        Integer id = (Integer) c.get("id");
							        String  nombre = (String) c.get("nombre");
							  %>
							        <option value="<%= id %>"><%= nombre %></option>
							  <%
							      }
							    }
							  %>
						  </select>
						  <div class="invalid-feedback">Por favor, seleccione una categoría.</div>
						</div>

                        <div class="mb-3">
                            <label for="txtFechaIngreso" class="form-label fw-semibold text-secondary">Fecha del Ingreso</label>
                            <input type="date" class="form-control" id="txtFechaIngreso" name="txtFechaIngreso" required>
                            <div class="invalid-feedback">
                                Por favor, seleccione la fecha del ingreso.
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="txtDescripcionIngreso" class="form-label fw-semibold text-secondary">Descripción Detallada</label>
                            <textarea class="form-control" id="txtDescripcionIngreso" name="txtDescripcionIngreso" rows="3" placeholder="Ej: Pago de nómina mensual, Venta de producto X, etc." required></textarea>
                            <div class="invalid-feedback">
                                Por favor, ingrese una descripción.
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">Registrar Ingreso</button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>