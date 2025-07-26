package ec.edu.monster.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreditoBancarioDto {
    private Integer id;
    private Integer clienteId;
    private BigDecimal montoAprobado;
    private String estado; // 'APROBADO', 'RECHAZADO', 'CANCELADO'
    private LocalDateTime fechaAprobacion;
    private Integer plazoMeses;
    private BigDecimal tasaInteres;
}
