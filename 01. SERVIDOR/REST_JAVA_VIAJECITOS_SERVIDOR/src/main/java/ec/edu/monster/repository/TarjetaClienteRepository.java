package ec.edu.monster.repository;

import ec.edu.monster.modelo.ClienteTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TarjetaClienteRepository extends JpaRepository<ClienteTarjeta, Integer>  {
    @Query("SELECT t FROM ClienteTarjeta t WHERE t.cliente.clienteId = :id")
    Optional<List<ClienteTarjeta>> tarjetaByCliente(@Param("id") Integer id);

    List<ClienteTarjeta> findByTarjeta_TarjetaId(Integer tarjetaId);

}
