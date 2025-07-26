package ec.edu.monster.modelo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ASIENTO")
@Data
public class Asiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASIENTO_ID")
    private Integer asientoId;

    @ManyToOne
    @JoinColumn(name = "VUELO_ID", nullable = false)
    private Vuelo vuelo;

    @Column(name = "FILA", nullable = false)
    private Integer fila;

    @Column(name = "COLUMNA", nullable = false, length = 1)
    private String columna;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO", nullable = false)
    private TipoAsiento tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoAsiento estado = EstadoAsiento.DISPONIBLE;

    @ManyToOne
    @JoinColumn(name = "BOLETO_ID")
    private Boleto boleto;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;


    public enum TipoAsiento {
        VENTANA, PASILLO, CENTRO
    }

    public enum EstadoAsiento {
        DISPONIBLE, RESERVADO, PAGADO
    }
}
