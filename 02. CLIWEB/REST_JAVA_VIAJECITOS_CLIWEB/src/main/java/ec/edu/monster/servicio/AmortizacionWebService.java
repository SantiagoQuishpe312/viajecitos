package ec.edu.monster.servicio;

import ec.edu.monster.modelo.CuotaAmortizacion;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;
@Service

public class AmortizacionWebService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/amortizacion";

    public Optional<List<CuotaAmortizacion>> obtenerAmortizacion(BigDecimal valor,
                                                                 Integer numCuotas,
                                                                 String tipoAmortizacion) {
        String url = String.format("%s?valor=%s&numCuotas=%d&tipoAmortizacion=%s",
                API_URL, valor, numCuotas, tipoAmortizacion);

        try {
            ResponseEntity<List<CuotaAmortizacion>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CuotaAmortizacion>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            // Puedes loguear el error si deseas
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
