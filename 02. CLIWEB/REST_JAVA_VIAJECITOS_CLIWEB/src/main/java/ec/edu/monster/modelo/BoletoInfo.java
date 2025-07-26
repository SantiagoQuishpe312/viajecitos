package ec.edu.monster.modelo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoletoInfo {
    private Boleto boleto;
    private Ciudad cuidadOrigen;
    private Ciudad ciudadDestino;
    private Vuelo vuelo;
    private List<Asiento> asientos;
}
