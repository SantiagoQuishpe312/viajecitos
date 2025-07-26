package ec.edu.monster.controlador;

import ec.edu.monster.dto.VueloDto;
import ec.edu.monster.servicio.IServiceVuelo;
import ec.edu.monster.util.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/vuelo")
public class VueloController {

    private final IServiceVuelo vueloService;

    public VueloController(IServiceVuelo vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping("")
    public ResponseEntity<List<VueloDto>> listarVuelos() {
        List<VueloDto> vuelos = vueloService.obtenerTodos();
        return ResponseEntity.ok(vuelos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VueloDto> obtenerVueloPorId(@PathVariable Integer id) {
        return vueloService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/destino/{destino}/origen/{origen}")
    public ResponseEntity<List<VueloDto>> listarVuelosporCiudades(@PathVariable String destino, @PathVariable String origen) {
        return vueloService.obtenerPorCiudades(destino, origen)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VueloDto> crearVuelo(@RequestBody VueloDto vueloDto) {
        VueloDto creado = vueloService.guardar(vueloDto);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarVuelo(@PathVariable Integer id, @RequestBody VueloDto vueloDto) {
        try {
            vueloService.actualizar(id, vueloDto);
            return ResponseEntity.ok("Vuelo actualizado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVuelo(@PathVariable Integer id) {
        if (vueloService.obtenerPorId(id).isPresent()) {
            vueloService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
