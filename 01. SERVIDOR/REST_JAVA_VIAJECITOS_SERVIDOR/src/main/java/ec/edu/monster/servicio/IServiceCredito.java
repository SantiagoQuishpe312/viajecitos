package ec.edu.monster.servicio;

import java.math.BigDecimal;

public interface IServiceCredito {
    boolean esSujetoCredito(Integer clienteId);
    BigDecimal calcularCreditoDisponible(Integer clienteId);
}
