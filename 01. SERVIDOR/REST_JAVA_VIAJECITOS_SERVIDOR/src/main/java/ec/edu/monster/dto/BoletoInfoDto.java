package ec.edu.monster.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoletoInfoDto {
    private BoletoDto boleto;
    private CiudadDto cuidadOrigen;
    private CiudadDto ciudadDestino;
    private VueloDto vuelo;
    private List<AsientoDto> asientos;
}
