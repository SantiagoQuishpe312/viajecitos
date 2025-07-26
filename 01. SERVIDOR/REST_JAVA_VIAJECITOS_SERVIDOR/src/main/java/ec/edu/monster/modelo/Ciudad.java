package ec.edu.monster.modelo;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "CIUDAD")
@Data
public class Ciudad {

    @Id
    @Column(name = "CIUDAD_CODIGO")
    private String ciudadCodigo;

    @Column(name = "CIUDAD_NOMBRE")
    private String ciudadNombre;

    @Column(name = "CIUDAD_PAIS_ORIGEN")
    private String ciudadPaisOrigen;

    @Column(name = "CIUDAD_NOMBRE_AEROPUERTO")
    private String ciudadNombreAeropuerto;

    @Column(name = "CIUDAD_LATITUD")
    private String ciudadLatitud;

    @Column(name = "CIUDAD_LONGITUD")
    private String ciudadLongitud;

    // Relaciones inversas con Vuelo
    @OneToMany(mappedBy = "ciudadOrigen" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vuelo> vuelosOrigen;

    @OneToMany(mappedBy = "ciudadDestino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vuelo> vuelosDestino;
}
