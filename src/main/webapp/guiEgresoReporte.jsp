<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Egreso" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reporte de Egreso</title>
    <jsp:include page="head.jsp" />
    <style>
        .report-card {
            margin-top: 30px;
            border: 1px solid #f7d6d6;
            border-left: 5px solid #dc3545; /* Rojo para egresos */
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        }
        .report-card .card-header {
            background-color: #ffe9e9;
            color: #dc3545;
            font-weight: bold;
            border-bottom: 1px solid #ffb3b3;
        }
        .report-card .card-body p {
            margin-bottom: 8px;
        }
        .report-card .card-body strong {
            color: #333;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Reporte de Egreso</h1>
                </div>

                <%
                    Egreso egreso = (Egreso) request.getAttribute("egreso");
                    if (egreso != null) {
                %>
                <div class="card report-card">
                    <div class="card-header">
                        Detalles del Egreso Registrado
                    </div>
                    <div class="card-body">
                        <p class="card-text"><strong>Monto:</strong> $<%= String.format("%.2f", egreso.getMonto()) %></p>
                        <p class="card-text"><strong>Categoría:</strong> <%= egreso.getCategoria() %></p>
                        <p class="card-text"><strong>Fecha:</strong> <%= egreso.getFecha() %></p>
                        <p class="card-text"><strong>Descripción:</strong> <%= egreso.getDescripcion() %></p>
                    </div>
                </div>
                <%
                    } else {
                %>
                <div class="alert alert-warning mt-3" role="alert">
                    No se encontró información de egreso para mostrar.
                </div>
                <%
                    }
                %>
                <a href="guiDashboard.jsp" onclick="loadInitialDashboardContent(); return false;" class="btn btn-secondary mt-3">Volver al Dashboard</a>
            </main>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
    <!-- Asegúrate de que loadInitialDashboardContent esté disponible en el ámbito global o en dashboard.js -->
    <script src="js/dashboard.js"></script>
</body>
</html>