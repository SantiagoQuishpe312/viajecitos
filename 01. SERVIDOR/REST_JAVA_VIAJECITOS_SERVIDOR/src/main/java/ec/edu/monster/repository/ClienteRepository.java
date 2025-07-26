package ec.edu.monster.repository;

import ec.edu.monster.modelo.Asiento;
import ec.edu.monster.modelo.Boleto;
import ec.edu.monster.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    @Query("SELECT c FROM Cliente c WHERE c.clienteReferente.clienteId = :clienteId" )
    List<Cliente> findByClienteCreate(Integer clienteId);
}
