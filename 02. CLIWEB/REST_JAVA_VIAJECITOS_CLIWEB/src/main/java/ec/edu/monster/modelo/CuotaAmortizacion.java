package ec.edu.monster.modelo;

import lombok.Data;

import java.math.BigDecimal;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CuotaAmortizacion {
        private Integer numeroCuota;
        private BigDecimal valorCuota;
        private BigDecimal interesPagado;
        private BigDecimal capitalPagado;
        private BigDecimal saldoPendiente;
    }


