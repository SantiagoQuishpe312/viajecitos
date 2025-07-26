package ec.edu.monster.repository;

import ec.edu.monster.modelo.CreditoBancario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditoBancarioRepository extends JpaRepository<CreditoBancario, Integer> {

    @Query("SELECT c FROM CreditoBancario c WHERE c.cliente.clienteId = :clienteId AND c.estado = :estado")
    List<CreditoBancario> obtenerCreditosAprobados(@Param("clienteId") Integer clienteId, @Param("estado") CreditoBancario.EstadoCredito estado);

}
