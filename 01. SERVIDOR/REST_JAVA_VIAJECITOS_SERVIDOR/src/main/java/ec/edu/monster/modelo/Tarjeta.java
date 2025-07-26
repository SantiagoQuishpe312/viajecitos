package ec.edu.monster.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TARJETA")
@Data
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 👈 Asegúrate de tener esto
    @Column(name = "TARJETA_ID")  // Debe coincidir con el nombre exacto en la DB
    private Long tarjetaId;

    @Column(name = "TARJETA_CVV")
    private String tarjetaCvv;

    @Column(name = "TARJETA_FECHA_CAD")
    private Date tarjetaFechaCad;

    @Column(name = "TARJETA_FECHA_REGISTRO")
    private Date tarjetaFechaRegistro;

    @Column(name = "TARJETA_NUMERO")
    private String tarjetaNumero;

    @Column(name = "TARJETA_PROV")
    private String tarjetaProv;

    @Column(name = "TARJETA_SALDO")
    private BigDecimal tarjetaSaldo;

    @Column(name = "TARJETA_TIPO")
    private String tarjetaTipo;

    @OneToMany(mappedBy = "tarjeta" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClienteTarjeta> clienteTarjetas;
}
