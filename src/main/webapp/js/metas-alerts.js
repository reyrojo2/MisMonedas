// === Metas: SweetAlert helpers ===

// Confirmación de eliminación (modal centrado)
function confirmarEliminarMeta(id, contextPath, extraQuery) {
  // extraQuery es opcional, por si necesitas enviar algo como "?tipo=meta"
  const qs = extraQuery ? (extraQuery.startsWith('?') ? extraQuery : '?' + extraQuery) : '';
  Swal.fire({
    title: "¿Quieres quedarte sin metas?",
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
      form.action = contextPath + "/MetasAhorroController" + qs;
      form.innerHTML = `
        <input type="hidden" name="accion" value="delete">
        <input type="hidden" name="id" value="${id}">
      `;
      document.body.appendChild(form);
      form.submit();
    }
  });
}

// Modal de éxito (bloqueante con botón OK)
function mostrarExitoMeta(mensaje) {
  Swal.fire({
    icon: "success",
    title: "¡Éxito!",
    text: mensaje,
    confirmButtonColor: "#3085d6"
  });
}

// Modal de error
function mostrarErrorMeta(mensaje) {
  Swal.fire({
    icon: "error",
    title: "Oops...",
    text: mensaje,
    confirmButtonColor: "#d33"
  });
}

// Toast de éxito (no bloqueante) para registrar/actualizar
function mostrarToastExitoMeta(mensaje) {
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
