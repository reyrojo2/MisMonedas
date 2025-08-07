<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro de Egreso</title>
    <jsp:include page="head.jsp" />
</head>
<body>
    <div class="container-fluid">
        <div class="row">
        <%
		    request.setAttribute("activePage", "egresos");
		%>
            <!-- Navbar + Sidebar -->
            <jsp:include page="navbar.jsp" />

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Gestión de Egresos</h1>
                </div>

                <div class="bg-white p-4 rounded shadow mt-3">
                    <h3 class="text-center mb-4 text-secondary">Registrar Nuevo Egreso</h3>
                    <form action="EgresoController" method="post" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="txtMontoEgreso" class="form-label fw-semibold text-secondary">Monto del Egreso (S/)</label>
                            <input type="number" step="0.01" class="form-control" id="txtMontoEgreso" name="txtMontoEgreso" placeholder="Ej: 50.00" required min="0.01">
                            <div class="invalid-feedback">
                                Por favor, ingrese un monto válido (mayor a 0).
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="cbCategoriaEgreso" class="form-label fw-semibold text-secondary">Categoría</label>
                            <select class="form-select" id="cbCategoriaEgreso" name="cbCategoriaEgreso" required>
                                <option value="" disabled selected>Seleccione una categoría</option>
                                <option value="Alimentos">Alimentos</option>
                                <option value="Transporte">Transporte</option>
                                <option value="Vivienda">Vivienda</option>
                                <option value="Entretenimiento">Entretenimiento</option>
                                <option value="Servicios">Servicios (Luz, Agua, Internet)</option>
                                <option value="Educacion">Educación</option>
                                <option value="Salud">Salud</option>
                                <option value="Ropa">Ropa</option>
                                <option value="Deudas">Pago de Deudas</option>
                                <option value="Otros">Otros</option>
                            </select>
                            <div class="invalid-feedback">
                                Por favor, seleccione una categoría.
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="txtFechaEgreso" class="form-label fw-semibold text-secondary">Fecha del Egreso</label>
                            <input type="date" class="form-control" id="txtFechaEgreso" name="txtFechaEgreso" required>
                            <div class="invalid-feedback">
                                Por favor, seleccione la fecha del egreso.
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="txtDescripcionEgreso" class="form-label fw-semibold text-secondary">Descripción Detallada</label>
                            <textarea class="form-control" id="txtDescripcionEgreso" name="txtDescripcionEgreso" rows="3" placeholder="Ej: Compra de víveres, Pasaje de bus, Alquiler mensual, etc." required></textarea>
                            <div class="invalid-feedback">
                                Por favor, ingrese una descripción.
                            </div>
                        </div>
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-danger btn-lg">Registrar Egreso</button>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
</body>
</html>