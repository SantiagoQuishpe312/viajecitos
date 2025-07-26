package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Boleto;
import ec.edu.monster.modelo.BoletoInfo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class BoletoWebService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/boleto";

    public Boleto comprarBoleto(Boleto boleto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Boleto> request = new HttpEntity<>(boleto, headers);

        ResponseEntity<Boleto> response = restTemplate.postForEntity(API_URL, request, Boleto.class);

        return response.getBody();
    }

    public Optional<BoletoInfo> obtenerBoletoPorId(Integer id) {
        String url = API_URL + "/" + id;

        try {
            ResponseEntity<BoletoInfo> response = restTemplate.getForEntity(url, BoletoInfo.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    public Optional<List<BoletoInfo>> obtenerBoletosPorCliente(Integer clienteId) {
        String url = API_URL + "/cliente/" + clienteId;

        try {
            ResponseEntity<List<BoletoInfo>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BoletoInfo>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    public Optional<List<BoletoInfo>>  obtenerBoletosNoPagados(Integer id) {
        String url = API_URL + "/pendientes/cliente/" + id;

        try {
            ResponseEntity<List<BoletoInfo>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BoletoInfo>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    public void eliminarBoletoPorId(Integer id) {
        String url = API_URL + "/" + id;
        restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }
}
