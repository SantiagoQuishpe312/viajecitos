package ec.edu.monster.dto;

import lombok.Data;

@Data
public class AsientoDto {
    private Integer asientoId;
    private String nombreCliente;
    private Integer clienteId;
    private Integer vueloId;
    private Integer fila;
    private String columna;
    private String tipo;    // 'VENTANA', 'PASILLO', 'CENTRO'
    private String estado;  // 'DISPONIBLE', 'RESERVADO', 'PAGADO'
    private Integer boletoId;

}
