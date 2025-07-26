package ec.edu.monster.servicio;


import ec.edu.monster.modelo.Vuelo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class VueloWebService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/vuelo";

    public List<Vuelo> obtenerTodos() {
        ResponseEntity<List<Vuelo>> response = restTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }
    public Vuelo obtenerPorId(Integer id) {
        try {
            ResponseEntity<Vuelo> response = restTemplate.exchange(
                    API_URL + "/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Vuelo>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Vuelo con ID " + id + " no encontrado.");
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener vuelo: " + e.getMessage());
        }
    }


}
