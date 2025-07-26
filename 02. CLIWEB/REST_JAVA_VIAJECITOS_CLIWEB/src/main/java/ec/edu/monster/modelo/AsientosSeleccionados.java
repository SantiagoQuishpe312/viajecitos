package ec.edu.monster.modelo;

import lombok.Data;
import java.util.List;

@Data
public class AsientosSeleccionados {
    private Integer vueloId;
    private List<Integer> asientos;
    private String accion;  // "seguir" o "pagar"
    private String clientePorAsientoJson; // nuevo

}
