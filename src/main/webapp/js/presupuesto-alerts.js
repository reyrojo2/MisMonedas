// Confirmación de eliminación (modal centrado)
function confirmarEliminar(id, contextPath) {
  Swal.fire({
    title: "¿No quieres tener un presupuesto?",
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
      form.action = contextPath + "/PresupuestoController";
      form.innerHTML = `
        <input type="hidden" name="accion" value="delete">
        <input type="hidden" name="id" value="${id}">
      `;
      document.body.appendChild(form);
      form.submit();
    }
  });
}

// Modal de éxito (si quieres bloquear con botón OK)
function mostrarExito(mensaje) {
  Swal.fire({
    icon: "success",
    title: "¡Éxito!",
    text: mensaje,
    confirmButtonColor: "#3085d6"
  });
}

// Modal de error
function mostrarError(mensaje) {
  Swal.fire({
    icon: "error",
    title: "Oops...",
    text: mensaje,
    confirmButtonColor: "#d33"
  });
}

// Toast de éxito (para registrar/actualizar sin interrumpir)
function mostrarToastExito(mensaje) {
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
