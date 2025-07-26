package ec.edu.monster.modelo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "VUELO")
@Data
public class Vuelo {
    @Id
    @Column(name = "VUELO_ID")
    private Integer vueloId;

    @ManyToOne
    @JoinColumn(name = "CIUDAD_ORIGEN")
    private Ciudad ciudadOrigen;

    @ManyToOne
    @JoinColumn(name = "CIUDAD_DESTINO")
    private Ciudad ciudadDestino;

    @Column(name = "VUELO_CAPACIDAD")
    private Integer vueloCapacidad;

    @Column(name = "VUELO_ESTADO")
    private String vueloEstado;

    @Column(name = "VUELO_FECHA_SALIDA")
    private LocalDateTime vueloFechaSalida;

    @Column(name = "VUELO_VALOR")
    private BigDecimal vueloValor;

    @OneToMany(mappedBy = "vuelo" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Boleto> boletos;
}
