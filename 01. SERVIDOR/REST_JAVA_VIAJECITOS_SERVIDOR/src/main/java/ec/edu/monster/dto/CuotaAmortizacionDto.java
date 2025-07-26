package ec.edu.monster.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuotaAmortizacionDto {
    private Integer numeroCuota;
    private BigDecimal valorCuota;
    private BigDecimal interesPagado;
    private BigDecimal capitalPagado;
    private BigDecimal saldoPendiente;
}
