package ec.edu.monster.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "FACTURA")
@Data
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 👈 Asegúrate de tener esto

    @Column(name = "FACTURA_ID") // 🔥 Clave para que funcione correctamente
    private Integer facturaId;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;

    @Column(name = "FECHA")
    private LocalDateTime fecha;

    @Column(name = "SUBTOTAL")
    private BigDecimal subtotal;

    @Column(name = "IVA")
    private BigDecimal iva;

    @Column(name = "TOTAL")
    private BigDecimal total;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "METODO_PAGO")
    private String metodoPago;

    @OneToMany(mappedBy = "factura",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Boleto> boletos;

    @OneToOne(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CreditoFactura creditoFactura;
}
