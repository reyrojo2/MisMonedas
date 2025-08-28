<%
String flashOk = (String) session.getAttribute("flash_success");
if (flashOk != null) {
  session.removeAttribute("flash_success");
  String msgOk = flashOk.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","");
%>
<script>mostrarToastExitoMeta("<%= msgOk %>");</script>
<% } %>

<%
  String flashErr = (String) session.getAttribute("flash_error");
  if (flashErr != null) {
    session.removeAttribute("flash_error");
    String msgErr = flashErr.replace("\\","\\\\")
                            .replace("\"","\\\"")
                            .replace("\n","\\n")
                            .replace("\r","");
%>
<script>
  if (typeof mostrarError === "function") {
    mostrarError("<%= msgErr %>");
  }
</script>
<% } %>
