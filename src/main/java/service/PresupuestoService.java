package service;

import interfaces.PresupuestoDao;
import model.Presupuesto;
import modelvm.PresupuestoVM;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PresupuestoService {
    private final PresupuestoDao dao;

    public PresupuestoService(PresupuestoDao dao) {
        this.dao = Objects.requireNonNull(dao);
    }

    public List<Presupuesto> listarPorUsuario(int userId) {
        return dao.findByUserId(userId);
    }

    // VM con estado a partir de monto_gastado (calculado por la VIEW)
    public List<PresupuestoVM> listarVMPorUsuario(int userId) {
        List<Presupuesto> base = dao.findByUserId(userId);
        List<PresupuestoVM> out = new ArrayList<>();
        if (base == null) return out;
        for (Presupuesto p : base) {
            String estado = (p.getMontoGastado() > p.getMontoPresupuestado()) ? "Sobrepasado" : "OK";
            out.add(new PresupuestoVM(p, estado));
        }
        return out;
    }

    public void crear(int userId, String categoriaParam, String montoStr, String periodo) {
        Presupuesto p = new Presupuesto();
        p.setUserId(userId);
        p.setCategoria(categoriaParam);
        p.setMontoPresupuestado(Double.parseDouble(montoStr.replace(",", ".")));
        p.setPeriodo(periodo);
        // NO seteamos periodoInicio aquí; lo calculará el DAO
        if (!dao.save(p)) throw new RuntimeException("No se pudo crear el presupuesto.");
    }

    public void actualizar(int userId, String categoriaParam, String montoStr, String periodo) {
        Presupuesto p = new Presupuesto();
        p.setUserId(userId);
        p.setCategoria(categoriaParam);
        p.setMontoPresupuestado(Double.parseDouble(montoStr.replace(",", ".")));
        p.setPeriodo(periodo);
        if (!dao.update(p)) throw new RuntimeException("No se pudo actualizar el presupuesto.");
    }
    
    public void eliminar(String idStr) throws Exception {
        int id = parseId(idStr);
        if (!dao.delete(id)) {
            throw new Exception("No se pudo eliminar el presupuesto.");
        }
    }
    
 // === NUEVO: obtener un VM por id (validando pertenencia del usuario) ===
    public PresupuestoVM obtenerVMPorId(int userId, String idStr) {
        int id = parseId(idStr);
        Presupuesto p = dao.findById(id).orElse(null);   // <-- Optional
        if (p == null || p.getUserId() != userId) return null;
        String estado = (p.getMontoGastado() > p.getMontoPresupuestado()) ? "Sobrepasado" : "OK";
        return new PresupuestoVM(p, estado);
    }

    // === NUEVO: actualización robusta por ID ===
    public void actualizarPorId(int userId, String idStr, String categoriaParam, String montoStr, String periodo) {
        int id = parseId(idStr);
        Presupuesto existente = dao.findById(id).orElse(null);  // <-- Optional
        if (existente == null) throw new IllegalArgumentException("No existe el presupuesto.");
        if (existente.getUserId() != userId) throw new IllegalArgumentException("No autorizado.");

        // Actualiza campos
        existente.setCategoria(categoriaParam);                       // aquí el form manda ID (string numérico)
        existente.setMontoPresupuestado(Double.parseDouble(montoStr.replace(",", ".")));
        existente.setPeriodo(periodo);

        if (!dao.updateById(existente)) {                             // <-- usa el nuevo método
            throw new RuntimeException("No se pudo actualizar el presupuesto.");
        }
    }


    private static int parseId(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { throw new IllegalArgumentException("ID inválido."); }
    }
}
