package ec.edu.monster.repository;

import ec.edu.monster.modelo.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AsientoRepository extends JpaRepository<Asiento, Integer> {

    @Query("SELECT a FROM Asiento a WHERE a.vuelo.vueloId = :vueloId AND a.estado = 'DISPONIBLE'")
    List<Asiento> findAsientosDisponiblesPorVuelo(Integer vueloId);

    List<Asiento> findByVueloVueloId(Integer vueloId);

    @Query("SELECT a FROM Asiento a WHERE a.boleto.boletoId = :boletoId")
    List<Asiento> findAsientosByBoleto(@Param("boletoId") Integer boletoId);

    @Query("SELECT COUNT(a) FROM Asiento a WHERE a.vuelo.vueloId = :vueloId")
    Long countAsientosPorVueloId(@Param("vueloId") Integer vueloId);
    @Query("SELECT COUNT(a) FROM Asiento a WHERE a.vuelo.vueloId = :vueloId AND a.estado = 'DISPONIBLE'")
    Long countAsientosDisponiblesPorVueloId(@Param("vueloId") Integer vueloId);

}
