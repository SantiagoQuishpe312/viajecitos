package ec.edu.monster.modelo;

import lombok.Data;

@Data
public class Asiento {
    private Integer asientoId;
    private Integer vueloId;
    private Integer fila;
    private String columna;
    private String tipo;    // 'VENTANA', 'PASILLO', 'CENTRO'
    private String estado;  // 'DISPONIBLE', 'RESERVADO', 'PAGADO'
    private Integer boletoId;
}

