package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Boleto;
import ec.edu.monster.modelo.Factura;
import ec.edu.monster.modelo.FacturaCompleta;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Service
public class FacturaWebService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = API_BASE_PATH + "/factura";
    public Factura facturar(Factura factura) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Factura> request = new HttpEntity<>(factura, headers);

        ResponseEntity<Factura> response = restTemplate.postForEntity(API_URL, request, Factura.class);

        return response.getBody();
    }
    // 2. Obtener facturas completas por cliente
    public Optional<List<FacturaCompleta>> obtenerFacturasPorCliente(Integer clienteId) {
        String url = API_URL + "/cliente/" + clienteId;

        try {
            ResponseEntity<List<FacturaCompleta>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<FacturaCompleta>>() {}
            );

            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            // No se encontraron facturas para ese cliente (404), devolvemos vacío
            return Optional.empty();
        }
    }


    // 3. Obtener factura completa por ID
    public Optional<FacturaCompleta> obtenerFacturaCompletaPorId(Integer facturaId) {
        String url = API_URL + "/" + facturaId;
        ResponseEntity<FacturaCompleta> response = restTemplate.getForEntity(url, FacturaCompleta.class);
        return Optional.ofNullable(response.getBody());
    }

}
