package ec.edu.monster.repository;

import ec.edu.monster.modelo.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoletoRepository extends JpaRepository<Boleto, Integer> {
    @Query("SELECT b FROM Boleto b WHERE b.cliente.clienteId = :idCliente")
    Optional<List<Boleto>> boletosByClient(@Param("idCliente")Integer idCliente);

    @Query("SELECT b FROM Boleto b WHERE b.cliente.clienteId = :idCliente AND b.factura IS NULL")
    Optional<List<Boleto>> boletoSinPagar(@Param("idCliente")Integer idCliente);

    @Query("SELECT b FROM Boleto b WHERE b.factura.facturaId = :idFactura")
    Optional<List<Boleto>> boletoporFactura(@Param("idFactura")Integer idFactura);


}
