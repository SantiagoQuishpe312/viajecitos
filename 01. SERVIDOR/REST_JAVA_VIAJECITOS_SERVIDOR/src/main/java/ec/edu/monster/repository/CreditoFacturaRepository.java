package ec.edu.monster.repository;

import ec.edu.monster.modelo.CreditoBancario;
import ec.edu.monster.modelo.CreditoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreditoFacturaRepository extends JpaRepository<CreditoFactura, Integer> {
    @Query("SELECT c FROM CreditoFactura c WHERE c.factura.facturaId = :facId")
    Optional<CreditoFactura> obtenerCreditos(@Param("facId") Integer facId);
}
