package ec.edu.monster.modelo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Boleto {
    private Integer boletoId;
    private Integer clienteId;
    private Integer vueloId;
    private Integer facturaId;
    private Integer boletoCantidad;
    private String boletoEstado;
    private LocalDateTime boletoFechaCompra;
}
