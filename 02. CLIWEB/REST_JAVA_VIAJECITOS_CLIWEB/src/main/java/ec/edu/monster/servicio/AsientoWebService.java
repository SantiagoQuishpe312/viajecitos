package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Asiento;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class AsientoWebService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/asiento";

    // Obtener lista de asientos por vuelo
    public Optional<List<Asiento>> obtenerPorVuelo(Integer vueloId) {
        String url = String.format("%s/vuelo/%d", API_URL, vueloId);
        try {
            ResponseEntity<List<Asiento>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Asiento>>() {}
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

    // Obtener asiento por id
    public Optional<Asiento> obtenerPorId(Integer id) {
        String url = String.format("%s/%d", API_URL, id);
        try {
            ResponseEntity<Asiento> response = restTemplate.getForEntity(url, Asiento.class);
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

    // Crear un asiento
    public Optional<Asiento> crearAsiento(Asiento asientoDto) {
        try {
            ResponseEntity<Asiento> response = restTemplate.postForEntity(API_URL, asientoDto, Asiento.class);
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

    // Reservar asiento
    public Optional<Asiento> reservar(Integer id, Integer idBoleto, Integer idCliente) {
        String url = String.format("%s/%d/reservar/%d/cliente/%d", API_URL, id, idBoleto,idCliente);
        try {
            restTemplate.put(url, null);
            return obtenerPorId(id); // Para obtener el estado actualizado
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Map<String, Long>> obtenerResumenAsientosPorVuelo(Integer vueloId) {
        String url = String.format("%s/resumen/%d", API_URL, vueloId);

        try {
            ResponseEntity<Map<String, Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Long>>() {}
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

    // Pagar asiento
    public Optional<Asiento> pagar(Integer id) {
        String url = String.format("%s/%d/pagar", API_URL, id);
        try {
            restTemplate.put(url, null);
            return obtenerPorId(id); // para obtener estado actualizado
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Eliminar asiento
    public boolean eliminar(Integer id) {
        String url = String.format("%s/%d", API_URL, id);
        try {
            restTemplate.delete(url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
