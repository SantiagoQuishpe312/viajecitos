package ec.edu.monster.controlador;

import ec.edu.monster.dto.AsientoDto;
import ec.edu.monster.servicio.IServiceAsiento;
import ec.edu.monster.util.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/asiento")
public class AsientoController {

    @Autowired
    private IServiceAsiento asientoService;

    @GetMapping("/vuelo/{vueloId}")
    public ResponseEntity<List<AsientoDto>> obtenerPorVuelo(@PathVariable Integer vueloId) {
        return ResponseEntity.ok(asientoService.obtenerPorVuelo(vueloId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsientoDto> obtenerPorId(@PathVariable Integer id) {
        return asientoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<AsientoDto> crearAsiento(@RequestBody AsientoDto asientoDto) {
        return ResponseEntity.ok(asientoService.guardar(asientoDto));
    }

    @PutMapping("/{id}/reservar/{idBoleto}/cliente/{idCliente}")
    public ResponseEntity<AsientoDto> reservar(@PathVariable Integer id,@PathVariable Integer idBoleto, @PathVariable Integer idCliente) {
        return ResponseEntity.ok(asientoService.reservar(id,idBoleto,idCliente));
    }
    @GetMapping("/resumen/{vueloId}")
    public ResponseEntity<Map<String, Long>> obtenerResumen(@PathVariable Integer vueloId) {
        return ResponseEntity.ok(asientoService.obtenerResumenAsientosPorVuelo(vueloId));
    }
    @PutMapping("/{id}/pagar")
    public ResponseEntity<AsientoDto> pagar(@PathVariable Integer id) {
        return ResponseEntity.ok(asientoService.pagar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        asientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
