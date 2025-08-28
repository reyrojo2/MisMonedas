package modelvm;

import java.util.*;
import java.util.function.Function;

/**
 * Utilidad para proyectar listas de entidades a filas (Map<String,Object>)
 * para ser consumidas por la capa de vista sin acoplar las entidades del dominio.
 */
public final class ViewRows {

    private ViewRows() {
        // Evitar instanciación
    }

    /**
     * Convierte una lista de entidades a una lista de mapas usando un rowMapper.
     *
     * @param source lista de entidades del dominio
     * @param rowMapper función que transforma una entidad en un Map<String,Object>
     * @return lista de mapas, lista vacía si la entrada es nula o vacía
     */
    public static <T> List<Map<String,Object>> map(List<T> source,
                                                   Function<T, Map<String,Object>> rowMapper) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String,Object>> out = new ArrayList<>(source.size());
        for (T t : source) {
            Map<String,Object> row = rowMapper.apply(t);
            out.add(row != null ? row : Collections.emptyMap());
        }
        return out;
    }

    /**
     * Builder fluido para crear filas de manera legible en los rowMapper.
     *
     * Ejemplo:
     *   return ViewRows.row()
     *                  .put("fecha", entidad.getFecha())
     *                  .put("categoria", entidad.getCategoria())
     *                  .build();
     */
    public static Builder row() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String,Object> m = new LinkedHashMap<>();

        public Builder put(String k, Object v) {
            m.put(k, v);
            return this;
        }

        public Map<String,Object> build() {
            return m;
        }
    }
}
