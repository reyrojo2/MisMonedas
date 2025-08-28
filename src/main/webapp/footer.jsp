<footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="js/validation.js"></script>
<script id="dashboard-data" type="application/json"><%= (String) request.getAttribute("dashboardJson") %></script>
<script src="js/dashboard.js"></script>
<script src="<%= request.getContextPath() %>/js/presupuesto-alerts.js"></script>
<script src="<%= request.getContextPath() %>/js/metas-alerts.js"></script>
</footer>