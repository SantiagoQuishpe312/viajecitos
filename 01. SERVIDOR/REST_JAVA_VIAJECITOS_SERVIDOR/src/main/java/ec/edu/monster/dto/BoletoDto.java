package ec.edu.monster.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoletoDto {
    private Integer boletoId;
    private Integer clienteId;
    private Integer vueloId;
    private Integer facturaId;
    private Integer boletoCantidad;
    private String boletoEstado;
    private LocalDateTime boletoFechaCompra;

}
