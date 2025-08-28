// === Montos (Ingresos / Egresos): SweetAlert helpers ===

// Confirmación de eliminación (modal centrado)
function confirmarEliminarMonto(id, tipo, contextPath) {
  Swal.fire({
    title: "¿Estás seguro?",
    text: "¡No podrás revertir esta acción!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "Sí, eliminar",
    cancelButtonText: "Cancelar"
  }).then((result) => {
    if (result.isConfirmed) {
      const form = document.createElement("form");
      form.method = "post";
      // ojo: necesitamos pasar tipo=ingreso|egreso
      form.action = contextPath + "/montosController?tipo=" + tipo;
      form.innerHTML = `
        <input type="hidden" name="accion" value="delete">
        <input type="hidden" name="id" value="${id}">
      `;
      document.body.appendChild(form);
      form.submit();
    }
  });
}

// Toast de éxito (para registrar/actualizar sin interrumpir)
function mostrarToastExitoMonto(mensaje) {
  const Toast = Swal.mixin({
    toast: true,
    position: "top-end",
    showConfirmButton: false,
    timer: 2500,
    timerProgressBar: true
  });
  Toast.fire({
    icon: "success",
    title: mensaje
  });
}

// Modal de error
function mostrarErrorMonto(mensaje) {
  Swal.fire({
    icon: "error",
    title: "Oops...",
    text: mensaje,
    confirmButtonColor: "#d33"
  });
}
