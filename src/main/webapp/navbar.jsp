<header class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap py-2 shadow">
  	<a class="navbar-brand d-flex align-items-center col-md-3 col-lg-2 me-0 px-3 py-0 bg-dark" href="#">
	  <img src="../PROYECTOLP1/img/logo.png" alt="Logo" style="height: 30px;" class="me-2">
	  <span>MisMonedas</span>
	</a>
  <div class="navbar-nav">
    <div class="nav-item text-nowrap">
    </div>
  </div>
</header>
<nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse p-0">
	  <div class="d-flex flex-column justify-content-between" style="min-height: 100vh;">
	    
	    <!-- Menú superior -->
	    <div class="pt-5 px-2 flex-grow-1">
	      <ul class="nav flex-column">
	        <li class="nav-item">
			<%
			    String activePage = (String) request.getAttribute("activePage");
			    if (activePage == null) activePage = "";
			%>
	          <a class="nav-link <%= "dashboard".equals(activePage) ? "active" : "" %>" href="guiDashboard.jsp">
	          <span data-feather="home"></span>Principal</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link <%= "ingresos".equals(activePage) ? "active" : "" %>" href="guiIngreso.jsp">
	          <span data-feather="file"></span>Ingresos</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link <%= "egresos".equals(activePage) ? "active" : "" %>" href="guiEgreso.jsp">
	          <span data-feather="shopping-cart"></span>Egresos</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link" href="#"><span data-feather="users"></span>Metas de ahorro</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link" href="#"><span data-feather="bar-chart-2"></span>Presupuestos</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link" href="#"><span data-feather="layers"></span>Mi perfil</a>
	        </li>
	      </ul>
	
	      <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
	        <span>REPORTES GUARDADOS</span>
	        <a class="link-secondary" href="#"><span data-feather="plus-circle"></span></a>
	      </h6>
	      <ul class="nav flex-column mb-2 px-2">
	        <li class="nav-item"><a class="nav-link" href="#"><span data-feather="file-text"></span> Este mes</a></li>
	        <li class="nav-item"><a class="nav-link" href="#"><span data-feather="file-text"></span> Meses previos</a></li>
	      </ul>
	    </div>
	
	    <!-- Botón Sign Out al fondo siempre visible -->
	    <div class="w-100">
	      <a class="btn btn-danger w-100 rounded-0" href="login.jsp">Sign out</a>
	    </div>
	
	  </div>
	</nav>
   