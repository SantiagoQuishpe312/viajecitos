package ec.edu.monster.repository;

import ec.edu.monster.modelo.ClienteTarjeta;
import ec.edu.monster.modelo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    @Query("SELECT f FROM Factura f WHERE f.cliente.clienteId = :id")
    Optional<List<Factura>> facturaByCliente(@Param("id") Integer id);

}
