package ec.edu.monster.servicio;

import ec.edu.monster.dto.CuotaAmortizacionDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceAmortizacion {

    private static final BigDecimal TASA_ANUAL = new BigDecimal("0.165"); // 16.5%
    private static final int ESCALA = 10;

    public List<CuotaAmortizacionDto> calcularAmortizacion(BigDecimal valor, int numCuotas, String tipoAmortizacion) {
        BigDecimal tasaPeriodo = TASA_ANUAL.divide(BigDecimal.valueOf(12), ESCALA, RoundingMode.HALF_UP);

        if ("frances".equalsIgnoreCase(tipoAmortizacion)) {
            return calcularSistemaFrances(valor, numCuotas, tasaPeriodo);
        } else if ("alemana".equalsIgnoreCase(tipoAmortizacion)) {
            return calcularSistemaAlemana(valor, numCuotas, tasaPeriodo);
        } else {
            throw new IllegalArgumentException("Tipo de amortización no soportado");
        }
    }

    // Amortización Francesa: cuota fija
    private List<CuotaAmortizacionDto> calcularSistemaFrances(BigDecimal valor, int numCuotas, BigDecimal tasaPeriodo) {
        List<CuotaAmortizacionDto> tabla = new ArrayList<>();

        BigDecimal uno = BigDecimal.ONE;
        BigDecimal base = uno.add(tasaPeriodo);
        BigDecimal potencia = uno.divide(base.pow(numCuotas, new java.math.MathContext(ESCALA, RoundingMode.HALF_UP)), ESCALA, RoundingMode.HALF_UP);

        BigDecimal denominador = uno.subtract(potencia);
        BigDecimal cuota = valor.multiply(tasaPeriodo).divide(denominador, ESCALA, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

        BigDecimal saldo = valor;

        for (int i = 1; i <= numCuotas; i++) {
            BigDecimal interes = saldo.multiply(tasaPeriodo).setScale(2, RoundingMode.HALF_UP);
            BigDecimal capital = cuota.subtract(interes).setScale(2, RoundingMode.HALF_UP);
            saldo = saldo.subtract(capital).setScale(2, RoundingMode.HALF_UP);

// Si es la última cuota, corregir posibles residuales
            if (i == numCuotas) {
                capital = capital.add(saldo); // suma el residual al capital
                cuota = capital.add(interes).setScale(2, RoundingMode.HALF_UP);
                saldo = BigDecimal.ZERO;
            }

            CuotaAmortizacionDto dto = new CuotaAmortizacionDto();
            dto.setNumeroCuota(i);
            dto.setValorCuota(cuota);
            dto.setInteresPagado(interes);
            dto.setCapitalPagado(capital);
            dto.setSaldoPendiente(saldo);

            tabla.add(dto);
        }

        return tabla;
    }

    // Amortización Alemana: capital fijo, interés decreciente
    private List<CuotaAmortizacionDto> calcularSistemaAlemana(BigDecimal valor, int numCuotas, BigDecimal tasaPeriodo) {
        List<CuotaAmortizacionDto> tabla = new ArrayList<>();

        BigDecimal capitalFijo = valor.divide(BigDecimal.valueOf(numCuotas), ESCALA, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        BigDecimal saldo = valor;

        for (int i = 1; i <= numCuotas; i++) {
            BigDecimal interes = saldo.multiply(tasaPeriodo).setScale(2, RoundingMode.HALF_UP);
            BigDecimal cuota = capitalFijo.add(interes).setScale(2, RoundingMode.HALF_UP);
            saldo = saldo.subtract(capitalFijo).setScale(2, RoundingMode.HALF_UP);

// Última cuota: corrige residuo
            if (i == numCuotas) {
                capitalFijo = capitalFijo.add(saldo); // agrega el residuo al capital
                cuota = capitalFijo.add(interes).setScale(2, RoundingMode.HALF_UP);
                saldo = BigDecimal.ZERO;
            }

            CuotaAmortizacionDto dto = new CuotaAmortizacionDto();
            dto.setNumeroCuota(i);
            dto.setValorCuota(cuota);
            dto.setInteresPagado(interes);
            dto.setCapitalPagado(capitalFijo);
            dto.setSaldoPendiente(saldo);

            tabla.add(dto);
        }

        return tabla;
    }
}
