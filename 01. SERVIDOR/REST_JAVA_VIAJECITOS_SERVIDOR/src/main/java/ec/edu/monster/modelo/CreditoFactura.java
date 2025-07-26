package ec.edu.monster.modelo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CREDITO_FACTURA")
@Data
public class CreditoFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDITO_ID")
    private Integer creditoId;

    @OneToOne
    @JoinColumn(name = "FACTURA_ID", unique = true)
    private Factura factura;

    @Column(name = "NUMERO_CUOTAS")
    private Integer numeroCuotas;

    @Column(name = "MONTO_FINANCIADO")
    private BigDecimal montoFinanciado;

    @Column(name = "TIPO_AMORTIZACION")
    private String tipoAmortizacion;
    @Column(name = "TASA_INTERES")
    private BigDecimal tasaInteres;
    @Column(name = "FECHA_REGISTRO")
    private LocalDate fechaRegistro;
}
