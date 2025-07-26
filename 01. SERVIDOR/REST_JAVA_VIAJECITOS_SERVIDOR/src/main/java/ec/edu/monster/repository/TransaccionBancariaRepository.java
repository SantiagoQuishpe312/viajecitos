package ec.edu.monster.repository;

import ec.edu.monster.modelo.TransaccionBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransaccionBancariaRepository extends JpaRepository<TransaccionBancaria, Integer> {

    @Query("SELECT t FROM TransaccionBancaria t WHERE t.cliente.clienteId = :clienteId AND t.fecha BETWEEN :desde AND :hasta")
    List<TransaccionBancaria> findByClienteAndRangoFecha(Integer clienteId, LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT AVG(t.monto) FROM TransaccionBancaria t WHERE t.cliente.clienteId = :clienteId AND t.tipo = 'DEPOSITO' AND t.fecha BETWEEN :desde AND :hasta")
    BigDecimal promedioDepositos(Integer clienteId, LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT AVG(t.monto) FROM TransaccionBancaria t WHERE t.cliente.clienteId = :clienteId AND t.tipo = 'RETIRO' AND t.fecha BETWEEN :desde AND :hasta")
    BigDecimal promedioRetiros(Integer clienteId, LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT COUNT(t) FROM TransaccionBancaria t WHERE t.cliente.clienteId = :clienteId AND t.tipo = 'DEPOSITO' AND t.fecha >= :desde")
    Long cuentaDepositosUltimoMes(Integer clienteId, LocalDateTime desde);
}
