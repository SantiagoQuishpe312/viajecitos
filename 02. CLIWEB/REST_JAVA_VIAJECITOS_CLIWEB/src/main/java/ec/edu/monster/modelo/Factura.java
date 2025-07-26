package ec.edu.monster.modelo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Factura {
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
