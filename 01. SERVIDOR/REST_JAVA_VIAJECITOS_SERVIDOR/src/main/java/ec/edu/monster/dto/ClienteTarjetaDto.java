package ec.edu.monster.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ClienteTarjetaDto {
    private Long clienteTarjetaId;
    private Integer clienteId;   // solo el ID para evitar ciclos
    private Integer tarjetaId;   // solo el ID para evitar ciclos
    private Date clienteTarjetaFecha;
}
