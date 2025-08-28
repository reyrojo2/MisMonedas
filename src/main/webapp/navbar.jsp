<header class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap py-2 shadow">
  	<a class="navbar-brand d-flex align-items-center col-md-3 col-lg-2 me-0 px-3 py-0 bg-dark" href="#">
	  <img src="../PROYECTOLP1/img/logo.png" alt="Logo" style="height: 30px;" class="me-2">
	  <span>MisMonedas</span>
	</a>
  <div class="navbar-nav ms-auto me-3">
    <div class="nav-item text-nowrap text-white">
      Bienvenido, 
      ${sessionScope.loggedFullName != null ? sessionScope.loggedFullName : 'Invitado'}
    </div>
  </div>
</header>
<nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-dark sidebar collapse p-0">
	  <div class="d-flex flex-column justify-content-between" style="min-height: 100vh;">
	    
	    <!-- Menú superior -->
	    <div class="pt-5 px-2 flex-grow-1">
	      <ul class="nav flex-column">
	        <li class="nav-item">
			<%
			    String activePage = (String) request.getAttribute("activePage");
			    if (activePage == null) activePage = "";
			%>
	          <a class="nav-link text-white <%= "dashboard".equals(activePage) ? "active" : "" %>" href="DashboardController">
	          <span data-feather="home"></span>Principal</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link text-white <%= "ingresos".equals(activePage) ? "active" : "" %>" href="montosController?tipo=ingreso">
	          <span data-feather="file"></span>Ingresos</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link text-white <%= "egresos".equals(activePage) ? "active" : "" %>" href="montosController?tipo=egreso">
	          <span data-feather="shopping-cart"></span>Egresos</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link text-white <%= "metas".equals(activePage) ? "active" : "" %>" href="MetasAhorroController">
	          <span data-feather="file"></span>Metas de Ahorro</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link text-white <%= "presupuesto".equals(activePage) ? "active" : "" %>" href="PresupuestoController?view=reporte">
	          <span data-feather="file"></span>Presupuestos</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link text-white <%= "perfil".equals(activePage) ? "active" : "" %>" href="loginController?tipo=perfil">
	          <span data-feather="file"></span>Mi perfil</a>
	        </li>
	      </ul>
	    </div>
	
	    <!-- Botón Sign Out al fondo siempre visible -->
	    <div class="w-100">
	      <a class="btn btn-danger w-100 rounded-0" href="${pageContext.request.contextPath}/loginController?action=logout">Sign out</a>
	    </div>
	
	  </div>
	</nav>
   