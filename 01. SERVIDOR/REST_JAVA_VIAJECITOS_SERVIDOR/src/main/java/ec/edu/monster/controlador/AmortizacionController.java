package ec.edu.monster.controlador;


import ec.edu.monster.dto.CuotaAmortizacionDto;
import ec.edu.monster.servicio.ServiceAmortizacion;
import ec.edu.monster.util.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH+"/amortizacion")
public class AmortizacionController {

    @Autowired
    private ServiceAmortizacion amortizacionService;

    @GetMapping
    public List<CuotaAmortizacionDto> obtenerTablaAmortizacion(
            @RequestParam BigDecimal valor,
            @RequestParam int numCuotas,
            @RequestParam String tipoAmortizacion) {

        return amortizacionService.calcularAmortizacion(valor, numCuotas, tipoAmortizacion);
    }
}