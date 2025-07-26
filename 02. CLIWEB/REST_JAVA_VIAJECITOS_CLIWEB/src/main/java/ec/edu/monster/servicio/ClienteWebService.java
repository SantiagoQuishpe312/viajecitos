package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Cliente;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class ClienteWebService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/cliente";

    public Cliente guardarCliente(Cliente clienteDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cliente> request = new HttpEntity<>(clienteDto, headers);

        ResponseEntity<Cliente> response = restTemplate.postForEntity(API_URL, request, Cliente.class);

        return response.getBody();
    }

    public Optional<Cliente> obtenerClientePorId(Integer id) {
        String url = API_URL + "/" + id;

        try {
            ResponseEntity<Cliente> response = restTemplate.getForEntity(url, Cliente.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    public Optional<List<Cliente>> listarClientes() {
        try {
            ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Cliente>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.of(response.getBody());
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    public void eliminarClientePorId(Integer id) {
        String url = API_URL + "/" + id;
        restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }

    public Optional<List<Cliente>> obtenerClientesReferidos(Integer referenteId) {
        String url = API_URL + "/referente/" + referenteId;

        try {
            ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Cliente>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.of(response.getBody());
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }
}
