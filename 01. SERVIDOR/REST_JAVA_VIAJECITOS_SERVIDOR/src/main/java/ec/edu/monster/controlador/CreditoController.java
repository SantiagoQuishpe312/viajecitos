package ec.edu.monster.controlador;

import ec.edu.monster.dto.CreditoInfoDto;
import ec.edu.monster.servicio.IServiceCredito;
import ec.edu.monster.util.ApiConstants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/credito")
public class CreditoController {

    @Autowired
    private IServiceCredito creditoService;

    @GetMapping("/evaluar/{clienteId}")
    public ResponseEntity<CreditoInfoDto> evaluarCredito(@PathVariable Integer clienteId) {
        try {
            boolean sujeto = creditoService.esSujetoCredito(clienteId);
            CreditoInfoDto dto = new CreditoInfoDto();
            dto.setSujetoCredito(sujeto);

            if (sujeto) {
                BigDecimal monto = creditoService.calcularCreditoDisponible(clienteId);
                dto.setCreditoDisponible(monto);
            }

            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            CreditoInfoDto dto = new CreditoInfoDto();
            dto.setSujetoCredito(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
