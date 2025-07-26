package ec.edu.monster.servicio;

import ec.edu.monster.modelo.CreditoInfo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class CreditoWebService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/credito";

    public Optional<CreditoInfo> evaluarCredito(Integer clienteId) {
        String url = String.format("%s/evaluar/%d", API_URL, clienteId);

        try {
            ResponseEntity<CreditoInfo> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CreditoInfo>() {}
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

