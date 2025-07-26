package ec.edu.monster.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "CLIENTE_TARJETA")
@Data
public class ClienteTarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 👈 Asegúrate de tener esto
    @Column(name = "CLIENTE_TARJETA_ID")  // <- Asegúrate que coincida con la base
    private Long clienteTarjetaId;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "TARJETA_ID")
    private Tarjeta tarjeta;

    @Column(name = "CLIENTE_TARJETA_FECHA")
    private Date clienteTarjetaFecha;}
