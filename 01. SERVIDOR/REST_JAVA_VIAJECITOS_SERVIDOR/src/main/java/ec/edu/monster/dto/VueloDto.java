package ec.edu.monster.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VueloDto {
    private Integer vueloId;

    private String ciudadOrigenCodigo;
    private String ciudadOrigenNombre;
    private String ciudadOrigenPais;
    private String ciudadOrigenAeropuerto;
    private String ciudadOrigenLatitud;
    private String ciudadOrigenLongitud;

    private String ciudadDestinoCodigo;
    private String ciudadDestinoNombre;
    private String ciudadDestinoPais;
    private String ciudadDestinoAeropuerto;
    private String ciudadDestinoLatitud;
    private String ciudadDestinoLongitud;

    private Integer vueloCapacidad;
    private String vueloEstado;
    private LocalDateTime vueloFechaSalida;
    private BigDecimal vueloValor;
    private Long totalAsientos;
    private Long asientosDisponibles;
}
