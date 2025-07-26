package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.Tarjeta;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class TarjetaWebService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/tarjeta";

    public List<Tarjeta> obtenerTarjetas(Integer idUser){
        ResponseEntity<List<Tarjeta>> response=restTemplate.exchange(
                API_URL+"/cliente/"+idUser,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}

        );
        return response.getBody();
    }
    public Tarjeta crearTarjeta(Tarjeta tarjeta, Integer idCliente) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Tarjeta> request = new HttpEntity<>(tarjeta, headers);

        String url = API_URL + "/cliente/" + idCliente;  // incluir el idCliente en la URL

        ResponseEntity<Tarjeta> response = restTemplate.postForEntity(url, request, Tarjeta.class);

        return response.getBody();
    }


}
