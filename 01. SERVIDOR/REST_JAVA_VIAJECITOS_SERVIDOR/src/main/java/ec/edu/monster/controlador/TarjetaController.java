package ec.edu.monster.controlador;

import ec.edu.monster.dto.TarjetaDto;
import ec.edu.monster.servicio.IServiceTarjeta;
import ec.edu.monster.util.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/tarjeta")
public class TarjetaController {

    @Autowired
    private IServiceTarjeta tarjetaService;

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<TarjetaDto>> obtenerTarjetasPorCliente(@PathVariable("idCliente") Integer idCliente) {
        List<TarjetaDto> tarjetas = tarjetaService.obtenerPorClienteId(idCliente);
        if (tarjetas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (tarjetas.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(tarjetas); // 200 OK
    }

    @PostMapping("/cliente/{idCliente}")
    public ResponseEntity<TarjetaDto> crearTarjeta(@RequestBody TarjetaDto tarjetaDto,@PathVariable("idCliente") Integer idCliente) {
        TarjetaDto tarjetaCreada = tarjetaService.guardarTarjeta(tarjetaDto,idCliente);
        return new ResponseEntity<>(tarjetaCreada, HttpStatus.CREATED); // 201 Created
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarjeta(@PathVariable Integer id) {
        // Podrías validar que exista primero si tu servicio lo soporta
        tarjetaService.eliminarTarjetaPorId(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
