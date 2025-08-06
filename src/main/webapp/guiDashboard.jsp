<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp"></jsp:include>
  <body>
    
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

<div class="container-fluid">
  <div class="row">
	    <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse p-0">
	  <div class="d-flex flex-column justify-content-between" style="min-height: 100vh;">
	    
	    <!-- Menú superior -->
	    <div class="pt-5 px-2 flex-grow-1">
	      <ul class="nav flex-column">
	        <li class="nav-item">
	          <a class="nav-link active" href="#"><span data-feather="home"></span>Principal</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link" href="#"><span data-feather="file"></span>Ingresos</a>
	        </li>
	        <li class="nav-item">
	          <a class="nav-link" href="#"><span data-feather="shopping-cart"></span>Egresos</a>
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
    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Gastos semanales</h1>
        <div class="btn-toolbar mb-2 mb-md-0">
          <div class="btn-group me-2">
            <button type="button" class="btn btn-sm btn-success">Exportar</button>
          </div>
          <button type="button" class="btn btn-sm btn-success dropdown-toggle">
            <span data-feather="calendar"></span>
            Esta semana
          </button>
        </div>
      </div>

      <canvas class="my-4 w-100" id="myChart" width="900" height="380"></canvas>

      <h2>Detalle de gastos</h2>
      <div class="table-responsive">
        <table class="table table-striped table-sm">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Header</th>
              <th scope="col">Header</th>
              <th scope="col">Header</th>
              <th scope="col">Header</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>1,001</td>
              <td>random</td>
              <td>data</td>
              <td>placeholder</td>
              <td>text</td>
            </tr>
            <tr>
              <td>1,002</td>
              <td>placeholder</td>
              <td>irrelevant</td>
              <td>visual</td>
              <td>layout</td>
            </tr>
            <tr>
              <td>1,003</td>
              <td>data</td>
              <td>rich</td>
              <td>dashboard</td>
              <td>tabular</td>
            </tr>
            <tr>
              <td>1,003</td>
              <td>information</td>
              <td>placeholder</td>
              <td>illustrative</td>
              <td>data</td>
            </tr>
            <tr>
              <td>1,004</td>
              <td>text</td>
              <td>random</td>
              <td>layout</td>
              <td>dashboard</td>
            </tr>
            <tr>
              <td>1,005</td>
              <td>dashboard</td>
              <td>irrelevant</td>
              <td>text</td>
              <td>placeholder</td>
            </tr>
            <tr>
              <td>1,006</td>
              <td>dashboard</td>
              <td>illustrative</td>
              <td>rich</td>
              <td>data</td>
            </tr>
            <tr>
              <td>1,007</td>
              <td>placeholder</td>
              <td>tabular</td>
              <td>information</td>
              <td>irrelevant</td>
            </tr>
            <tr>
              <td>1,008</td>
              <td>random</td>
              <td>data</td>
              <td>placeholder</td>
              <td>text</td>
            </tr>
            <tr>
              <td>1,009</td>
              <td>placeholder</td>
              <td>irrelevant</td>
              <td>visual</td>
              <td>layout</td>
            </tr>
            <tr>
              <td>1,010</td>
              <td>data</td>
              <td>rich</td>
              <td>dashboard</td>
              <td>tabular</td>
            </tr>
            <tr>
              <td>1,011</td>
              <td>information</td>
              <td>placeholder</td>
              <td>illustrative</td>
              <td>data</td>
            </tr>
            <tr>
              <td>1,012</td>
              <td>text</td>
              <td>placeholder</td>
              <td>layout</td>
              <td>dashboard</td>
            </tr>
            <tr>
              <td>1,013</td>
              <td>dashboard</td>
              <td>irrelevant</td>
              <td>text</td>
              <td>visual</td>
            </tr>
            <tr>
              <td>1,014</td>
              <td>dashboard</td>
              <td>illustrative</td>
              <td>rich</td>
              <td>data</td>
            </tr>
            <tr>
              <td>1,015</td>
              <td>random</td>
              <td>tabular</td>
              <td>information</td>
              <td>text</td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</div>

	<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>
