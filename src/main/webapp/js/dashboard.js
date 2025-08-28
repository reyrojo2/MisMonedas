// dashboard.js (compat ES5)
(function () {
  // Evita correr 2 veces
  if (window.__dashboard_init_done__) return;
  window.__dashboard_init_done__ = true;

  function safeParseDashboardJSON() {
    var node = document.getElementById('dashboard-data');
    if (!node) return null;
    try {
      var txt = node.textContent || node.innerText || '{}';
      return JSON.parse(txt);
    } catch (e) {
      console.error('No se pudo parsear #dashboard-data:', e);
      return null;
    }
  }

  function pick(obj, path, fallback) {
    try {
      var cur = obj, i, key;
      for (i = 0; i < path.length; i++) {
        key = path[i];
        if (cur != null && Object.prototype.hasOwnProperty.call(cur, key)) cur = cur[key];
        else return fallback;
      }
      return (cur !== undefined && cur !== null) ? cur : fallback;
    } catch (e) { return fallback; }
  }

  var vm = safeParseDashboardJSON();
  var ingresoLabelsData, ingresoSeriesData, egresoLabelsData, egresoSeriesData;

  if (vm) {
    ingresoLabelsData = pick(vm, ['ingresoLabels'], pick(vm, ['charts','ingresos','labels'], []));
    ingresoSeriesData = pick(vm, ['ingresoData'],   pick(vm, ['charts','ingresos','data'],   []));
    egresoLabelsData  = pick(vm, ['egresoLabels'],  pick(vm, ['charts','egresos','labels'],  []));
    egresoSeriesData  = pick(vm, ['egresoData'],    pick(vm, ['charts','egresos','data'],    []));
  } else {
    ingresoLabelsData = (typeof ingresoLabels !== 'undefined') ? ingresoLabels : [];
    ingresoSeriesData = (typeof ingresoData   !== 'undefined') ? ingresoData   : [];
    egresoLabelsData  = (typeof egresoLabels  !== 'undefined') ? egresoLabels  : [];
    egresoSeriesData  = (typeof egresoData    !== 'undefined') ? egresoData    : [];
  }

  function buildBarChart(canvasEl, labels, data, colors) {
    if (!(canvasEl instanceof HTMLCanvasElement)) { console.warn('Canvas no encontrado'); return null; }
    if (!window.Chart) { console.error('Chart.js no está cargado'); return null; }

    // 🔥 Destruye un chart previo en este canvas si existe
    var prev = (typeof Chart.getChart === 'function') ? Chart.getChart(canvasEl) : (canvasEl.__chartInstance || null);
    if (prev) { try { prev.destroy(); } catch(e) {} }

    var ctx = canvasEl.getContext('2d');
    var inst = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels || [],
        datasets: [{
          label: 'Monto',
          data: data || [],
          backgroundColor: (colors && colors.bg) || 'rgba(75, 192, 192, 0.6)',
          borderColor:     (colors && colors.bd) || 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        scales: { y: { beginAtZero: true } }
      }
    });

    // Compat: guarda referencia en el canvas
    canvasEl.__chartInstance = inst;
    return inst;
  }

  var ingresosCanvas = document.getElementById('ingresosChart');
  var egresosCanvas  = document.getElementById('egresosChart');

  buildBarChart(
    ingresosCanvas,
    ingresoLabelsData,
    ingresoSeriesData,
    { bg: 'rgba(75, 192, 192, 0.6)', bd: 'rgba(75, 192, 192, 1)' }
  );

  buildBarChart(
    egresosCanvas,
    egresoLabelsData,
    egresoSeriesData,
    { bg: 'rgba(255, 99, 132, 0.6)', bd: 'rgba(255, 99, 132, 1)' }
  );
})();
