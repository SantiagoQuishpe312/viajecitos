package ec.edu.monster.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREDITO_BANCARIO")
@Data
public class CreditoBancario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;

    @Column(name = "MONTO_APROBADO", nullable = false)
    private BigDecimal montoAprobado;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoCredito estado = EstadoCredito.APROBADO;

    @Column(name = "FECHA_APROBACION", nullable = false)
    private LocalDateTime fechaAprobacion;

    @Column(name = "PLAZO_MESES", nullable = false)
    private Integer plazoMeses;

    @Column(name = "TASA_INTERES")
    private BigDecimal tasaInteres = new BigDecimal("16.5");

    @PrePersist
    public void prePersist() {
        this.fechaAprobacion = LocalDateTime.now();
    }

    public enum EstadoCredito {
        APROBADO, RECHAZADO, CANCELADO
    }
}
