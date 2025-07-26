package ec.edu.monster.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FacturaDto {
    private Integer facturaId;
    private Integer clienteId;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    private String estado;
    private String metodoPago;
    private String amortizacion;
    private Integer cuotas;
}
