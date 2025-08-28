<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Registro de Presupuesto</title>
  <jsp:include page="head.jsp" />
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <%
      request.setAttribute("activePage", "presupuesto");
      @SuppressWarnings("unchecked")
      List<Map<String,Object>> categorias = (List<Map<String,Object>>) request.getAttribute("categorias");
      modelvm.PresupuestoVM presuEdit = (modelvm.PresupuestoVM) request.getAttribute("presuEdit");
      String accionForm = (presuEdit != null) ? "actualizar" : "guardar";
      String tituloForm = (presuEdit != null) ? "Editar Presupuesto" : "Registrar Nuevo Presupuesto";
      String textoBoton = (presuEdit != null) ? "Actualizar" : "Guardar";
      String categoriaSelNombre = (String) request.getAttribute("categoriaSelNombre");
      String periodoSel = (String) request.getAttribute("periodoSel");
      if (presuEdit != null) { periodoSel = presuEdit.getPeriodo(); }
    %>

    <jsp:include page="navbar.jsp" />

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Gestión de Presupuestos</h1>
        <a class="btn btn-info" href="<%= request.getContextPath() %>/PresupuestoController?view=reporte">
          <i class="bi bi-table"></i> Ver reporte de presupuestos
        </a>
      </div>

      <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
      %>
      <div class="alert alert-danger" role="alert"><%= error %></div>
      <% } %>

      <div class="bg-white p-4 rounded shadow mt-3">
        <h3 class="text-center mb-4 text-secondary"><%= tituloForm %></h3>

        <!-- Usa accionForm; incluye id cuando se edita -->
        <form action="<%= request.getContextPath() %>/PresupuestoController" method="post"
              class="needs-validation" novalidate>
          <input type="hidden" name="accion" value="<%= accionForm %>">
          <% if (presuEdit != null) { %>
            <input type="hidden" name="id" value="<%= presuEdit.getId() %>">
          <% } %>

          <!-- Categoría -->
          <div class="mb-3">
            <label for="cbCategoriaPresupuesto" class="form-label fw-semibold text-secondary">Categoría</label>
            <select class="form-select" id="cbCategoriaPresupuesto" name="cbCategoriaPresupuesto" required>
              <option value="" disabled <%= (presuEdit==null ? "selected" : "") %>>Seleccione una categoría</option>
              <%
                if (categorias != null) {
                  for (Map<String,Object> c : categorias) {
                    String idCat = String.valueOf(c.get("id"));
                    String nom   = String.valueOf(c.get("nombre"));

                    // Marca selected por ID si lo tienes; si no, por nombre
                    boolean selected = false;
                    if (presuEdit != null) {
                      // si tu PresupuestoVM no tiene categoriaId, compara por nombre
                      if (categoriaSelNombre != null) {
                        selected = nom.equals(categoriaSelNombre);
                      } else {
                        selected = nom.equals(presuEdit.getCategoria());
                      }
                    }
              %>
                <option value="<%= idCat %>" <%= selected ? "selected" : "" %>><%= nom %></option>
              <%
                  }
                }
              %>
            </select>
            <div class="invalid-feedback">Por favor, seleccione una categoría.</div>
          </div>

          <!-- Monto Presupuestado -->
          <div class="mb-3">
            <label for="txtMontoPresupuestado" class="form-label fw-semibold text-secondary">Monto Presupuestado (S/)</label>
            <input type="number" step="0.01" min="0.01" class="form-control" id="txtMontoPresupuestado"
                   name="txtMontoPresupuestado"
                   value="<%= (presuEdit != null) ? presuEdit.getMontoPresupuestadoRaw() : "" %>"
                   placeholder="Ej: 500.00" required>
            <div class="invalid-feedback">Por favor, ingrese un monto válido (mayor a 0).</div>
          </div>

          <!-- Período -->
          <div class="mb-3">
            <label class="form-label">Período</label>
            <select name="cbPeriodoPresupuesto" class="form-select" required>
              <%
                String[] opciones = {"Mensual","Semanal","Anual","Único"};
                for (String op : opciones) {
              %>
                <option value="<%= op %>" <%= (op.equals(periodoSel) ? "selected" : "") %>><%= op %></option>
              <%
                }
              %>
            </select>
          </div>

          <div class="d-grid gap-2">
            <button type="submit" class="btn btn-primary btn-lg"><%= textoBoton %></button>
          </div>
        </form>
      </div>
    </main>
  </div>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>
