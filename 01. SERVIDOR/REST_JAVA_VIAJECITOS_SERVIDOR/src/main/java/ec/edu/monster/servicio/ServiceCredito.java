package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.CreditoBancario;
import ec.edu.monster.repository.ClienteRepository;
import ec.edu.monster.repository.CreditoBancarioRepository;
import ec.edu.monster.repository.TransaccionBancariaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceCredito implements IServiceCredito {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private TransaccionBancariaRepository transaccionRepository;
    @Autowired
    private CreditoBancarioRepository creditoRepository;

    @Override
    public boolean esSujetoCredito(Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        // Depósito en el último mes
        LocalDateTime haceUnMes = LocalDateTime.now().minusMonths(1);
        Long tieneDeposito = transaccionRepository.cuentaDepositosUltimoMes(clienteId, haceUnMes);
        System.out.println("Depósitos últimos 30 días: " + tieneDeposito);
        if (tieneDeposito == 0) return false;

        // Edad mínima si es masculino
        if (cliente.getClienteGenero() == Cliente.Genero.M) {
            int edad = LocalDate.now().getYear() - cliente.getClienteFechaNacimiento().getYear();
            System.out.println("Edad cliente: " + edad);
            if (edad < 25) return false;
        }
        try {
            System.out.println("Antes de obtener créditos");

            List<CreditoBancario> creditos = creditoRepository.obtenerCreditosAprobados(clienteId, CreditoBancario.EstadoCredito.APROBADO);

            System.out.println("Después de obtener créditos. Resultados: " + (creditos == null ? "null" : creditos.size()));

            if (creditos != null && !creditos.isEmpty()) {
                System.out.println("El cliente ya tiene un crédito aprobado.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("No tiene créditos aprobados.");
        return true;
        // sí es sujeto de crédito
    }



    @Override
    public BigDecimal calcularCreditoDisponible(Integer clienteId) {
        if (!esSujetoCredito(clienteId)) {
            throw new RuntimeException("Cliente no es sujeto de crédito");
        }

        LocalDateTime inicio = LocalDate.now().minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime fin = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59);

        BigDecimal promedioDep = transaccionRepository.promedioDepositos(clienteId, inicio, fin);
        BigDecimal promedioRet = transaccionRepository.promedioRetiros(clienteId, inicio, fin);

        promedioDep = promedioDep != null ? promedioDep : BigDecimal.ZERO;
        promedioRet = promedioRet != null ? promedioRet : BigDecimal.ZERO;

        BigDecimal base = promedioDep.subtract(promedioRet);
        if (base.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("No tiene capacidad financiera");

        return base.multiply(BigDecimal.valueOf(0.30)).multiply(BigDecimal.valueOf(6));
    }
}
