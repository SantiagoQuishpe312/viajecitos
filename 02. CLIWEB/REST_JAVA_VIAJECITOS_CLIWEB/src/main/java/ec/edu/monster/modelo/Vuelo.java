package ec.edu.monster.modelo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Vuelo {
    private Integer vueloId;
    private String ciudadOrigenCodigo;  // solo el código de la ciudad origen
    private String ciudadDestinoCodigo; // solo el código de la ciudad destino
    private Integer vueloCapacidad;
    private String vueloEstado;
    private LocalDateTime vueloFechaSalida;
    private BigDecimal vueloValor;
    private Integer asientosDisponibles;
    private Integer totalAsientos;
    private String ciudadOrigenNombre;
    private String ciudadDestinoNombre;

}
