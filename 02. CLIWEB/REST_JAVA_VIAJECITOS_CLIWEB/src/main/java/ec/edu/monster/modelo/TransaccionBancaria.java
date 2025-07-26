package ec.edu.monster.modelo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class TransaccionBancaria {
    private Integer id;
    private Integer clienteId;
    private String tipo; // 'DEPOSITO' o 'RETIRO'
    private BigDecimal monto;
    private LocalDateTime fecha;
}
