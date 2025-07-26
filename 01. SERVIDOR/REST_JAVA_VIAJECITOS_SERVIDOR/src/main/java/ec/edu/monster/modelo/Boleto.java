package ec.edu.monster.modelo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "BOLETO")
@Data
public class Boleto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOLETO_ID")
    private Integer boletoId;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "VUELO_ID")
    private Vuelo vuelo;

    @ManyToOne (optional = true)
    @JoinColumn(name = "factura_id", nullable = true)
    private Factura factura;

    @OneToMany(mappedBy = "boleto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asiento> asientos;

    @Column(name = "BOLETO_CANTIDAD")
    private Integer boletoCantidad;

    @Column(name = "BOLETO_ESTADO")
    private String boletoEstado;

    @Column(name = "BOLETO_FECHA_COMPRA")
    private LocalDateTime boletoFechaCompra;

    @PrePersist
    public void prePersist() {
        this.boletoFechaCompra = LocalDateTime.now();
    }
}
