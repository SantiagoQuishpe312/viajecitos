package ec.edu.monster.modelo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreditoFactura {private Integer creditoId;
    private Integer facturaId; // solo ID para evitar referencias cíclicas
    private Integer numeroCuotas;
    private BigDecimal montoFinanciado;
    private String tipoAmortizacion;
    private BigDecimal tasaInteres;
    private LocalDate fechaRegistro;

}
