package ec.edu.monster.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACCION_BANCARIA")
@Data
public class TransaccionBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO", nullable = false)
    private TipoTransaccion tipo;

    @Column(name = "MONTO", nullable = false)
    private BigDecimal monto;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }

    public enum TipoTransaccion {
        DEPOSITO, RETIRO
    }
}
