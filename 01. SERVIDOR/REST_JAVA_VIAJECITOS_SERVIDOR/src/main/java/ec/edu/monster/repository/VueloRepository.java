package ec.edu.monster.repository;

import ec.edu.monster.dto.VueloDto;
import ec.edu.monster.modelo.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VueloRepository extends JpaRepository<Vuelo, Integer> {
    @Query("SELECT v FROM Vuelo v WHERE v.ciudadDestino.ciudadCodigo = :ciudadDestino AND v.ciudadOrigen.ciudadCodigo = :ciudadOrigen")
    Optional<List<Vuelo>> vuelosByCiudades(@Param("ciudadDestino") String ciudadDestino, @Param("ciudadOrigen") String ciudadOrigen);

    @Query("""
    SELECT new ec.edu.monster.dto.VueloDto(
        v.vueloId,
        v.ciudadOrigen.ciudadCodigo,
        v.ciudadOrigen.ciudadNombre,
        v.ciudadOrigen.ciudadPaisOrigen,
        v.ciudadOrigen.ciudadNombreAeropuerto,
        v.ciudadOrigen.ciudadLatitud,
        v.ciudadOrigen.ciudadLongitud,
        v.ciudadDestino.ciudadCodigo,
        v.ciudadDestino.ciudadNombre,
        v.ciudadDestino.ciudadPaisOrigen,
        v.ciudadDestino.ciudadNombreAeropuerto,
        v.ciudadDestino.ciudadLatitud,
        v.ciudadDestino.ciudadLongitud,
        v.vueloCapacidad,
        v.vueloEstado,
        v.vueloFechaSalida,
        v.vueloValor,

        COUNT(a),
        SUM(CASE WHEN a.estado = ec.edu.monster.modelo.Asiento.EstadoAsiento.DISPONIBLE THEN 1 ELSE 0 END)
    )
    FROM Vuelo v
    LEFT JOIN Asiento a ON a.vuelo.vueloId = v.vueloId
    GROUP BY 
        v.vueloId,

        v.ciudadOrigen.ciudadCodigo,
        v.ciudadOrigen.ciudadNombre,
        v.ciudadOrigen.ciudadPaisOrigen,
        v.ciudadOrigen.ciudadNombreAeropuerto,
        v.ciudadOrigen.ciudadLatitud,
        v.ciudadOrigen.ciudadLongitud,

        v.ciudadDestino.ciudadCodigo,
        v.ciudadDestino.ciudadNombre,
        v.ciudadDestino.ciudadPaisOrigen,
        v.ciudadDestino.ciudadNombreAeropuerto,
        v.ciudadDestino.ciudadLatitud,
        v.ciudadDestino.ciudadLongitud,

        v.vueloCapacidad,
        v.vueloEstado,
        v.vueloFechaSalida,
        v.vueloValor
""")
    List<VueloDto> findVueloConResumenAsientos();
    @Query("""
    SELECT new ec.edu.monster.dto.VueloDto(
        v.vueloId,
        v.ciudadOrigen.ciudadCodigo,
        v.ciudadOrigen.ciudadNombre,
        v.ciudadOrigen.ciudadPaisOrigen,
        v.ciudadOrigen.ciudadNombreAeropuerto,
        v.ciudadOrigen.ciudadLatitud,
        v.ciudadOrigen.ciudadLongitud,
        v.ciudadDestino.ciudadCodigo,
        v.ciudadDestino.ciudadNombre,
        v.ciudadDestino.ciudadPaisOrigen,
        v.ciudadDestino.ciudadNombreAeropuerto,
        v.ciudadDestino.ciudadLatitud,
        v.ciudadDestino.ciudadLongitud,
        v.vueloCapacidad,
        v.vueloEstado,
        v.vueloFechaSalida,
        v.vueloValor,

        COUNT(a),
        SUM(CASE WHEN a.estado = ec.edu.monster.modelo.Asiento.EstadoAsiento.DISPONIBLE THEN 1 ELSE 0 END)
    )
    FROM Vuelo v
    LEFT JOIN Asiento a ON a.vuelo.vueloId = v.vueloId
    WHERE v.vueloId = :idVuelo
    GROUP BY 
        v.vueloId,

        v.ciudadOrigen.ciudadCodigo,
        v.ciudadOrigen.ciudadNombre,
        v.ciudadOrigen.ciudadPaisOrigen,
        v.ciudadOrigen.ciudadNombreAeropuerto,
        v.ciudadOrigen.ciudadLatitud,
        v.ciudadOrigen.ciudadLongitud,

        v.ciudadDestino.ciudadCodigo,
        v.ciudadDestino.ciudadNombre,
        v.ciudadDestino.ciudadPaisOrigen,
        v.ciudadDestino.ciudadNombreAeropuerto,
        v.ciudadDestino.ciudadLatitud,
        v.ciudadDestino.ciudadLongitud,

        v.vueloCapacidad,
        v.vueloEstado,
        v.vueloFechaSalida,
        v.vueloValor
""")
    Optional<VueloDto> findVueloConResumenAsientosById(@Param("idVuelo") Integer idVuelo);

}