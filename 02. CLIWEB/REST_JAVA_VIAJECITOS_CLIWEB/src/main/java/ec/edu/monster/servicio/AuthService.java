package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.LoginRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/auth/login";

    public Cliente login(LoginRequest loginRequest) throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<Cliente> response = restTemplate.postForEntity(API_URL, request, Cliente.class);

        Cliente cliente = response.getBody();

        if (cliente == null || cliente.getClienteId() == null) {
            throw new IllegalArgumentException("Credenciales incorrectas o usuario no encontrado");
        }

        return cliente;
    }
}
