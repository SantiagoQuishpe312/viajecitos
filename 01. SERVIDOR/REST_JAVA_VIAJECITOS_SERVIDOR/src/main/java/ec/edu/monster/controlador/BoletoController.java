package ec.edu.monster.controlador;

import ec.edu.monster.dto.BoletoDto;
import ec.edu.monster.dto.BoletoInfoDto;
import ec.edu.monster.modelo.Boleto;
import ec.edu.monster.servicio.IServiceBoleto;
import ec.edu.monster.util.ApiConstants;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH+"/boleto")
public class BoletoController {
    @Autowired
    private IServiceBoleto boletoService;
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<BoletoInfoDto>> obtenerBoletosPorCliente(@PathVariable("idCliente") Integer idCliente) {
        List<BoletoInfoDto> boletos = boletoService.obtenerBoletosByClient(idCliente);
        if (boletos == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // si decides retornar null si no existe el cliente
        }
        if (boletos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(boletos); // 200
    }
    @GetMapping("/pendientes/cliente/{idCliente}")
    public ResponseEntity<List<BoletoInfoDto>> obtenerBoletosPendientesPorCliente(@PathVariable("idCliente") Integer idCliente) {
        List<BoletoInfoDto> boletos = boletoService.obtenerBoletosNoPagados(idCliente);
        if (boletos == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // si decides retornar null si no existe el cliente
        }
        if (boletos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(boletos); // 200
    }
    @GetMapping("/{id}")
    public ResponseEntity<BoletoInfoDto> obtenerBoletoPorId(@PathVariable("id") Integer id) {
        try {
            BoletoInfoDto dto = boletoService.getById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("")
    public ResponseEntity<BoletoDto>createBoleto(@RequestBody BoletoDto boletoDto){
        BoletoDto dto=boletoService.guardar(boletoDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVuelo(@PathVariable Integer id) {
        if (boletoService.obtenerPorId(id).isPresent()) {
            boletoService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }





}
