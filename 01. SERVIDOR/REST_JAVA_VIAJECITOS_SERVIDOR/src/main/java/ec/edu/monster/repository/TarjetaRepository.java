package ec.edu.monster.repository;

import ec.edu.monster.modelo.ClienteTarjeta;
import ec.edu.monster.modelo.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {
    @Query("SELECT t FROM ClienteTarjeta t WHERE t.cliente.clienteId = :id")
    Optional<List<ClienteTarjeta>> tarjetaByCliente(@Param("id") Integer id);
}
