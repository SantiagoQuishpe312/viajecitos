package ec.edu.monster.controlador;

import ec.edu.monster.dto.FacturaCompletoDto;
import ec.edu.monster.dto.FacturaDto;
import ec.edu.monster.servicio.IServiceFactura;
import ec.edu.monster.util.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/factura")
public class FacturaController {

    private final IServiceFactura facturaService;

    public FacturaController(IServiceFactura facturaService) {
        this.facturaService = facturaService;
    }

    // GET /api/factura - listar todas las facturas
    @GetMapping("")
    public ResponseEntity<List<FacturaDto>> listarFacturas() {
        return ResponseEntity.ok(facturaService.obtenerTodas());
    }

    // GET /api/factura/{id} - obtener una factura por ID
    // GET /api/factura/cliente/{id} - Obtener todas las facturas de un cliente con boletos
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<FacturaCompletoDto>> obtenerFacturasPorCliente(@PathVariable Integer id) {
        return facturaService.obtenerPorUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/factura/completo/{id} - Obtener una factura completa por ID
    @GetMapping("/{id}")
    public ResponseEntity<FacturaCompletoDto> obtenerFacturaCompletaPorId(@PathVariable Integer id) {
        return facturaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/factura - crear nueva factura
    @PostMapping
    public ResponseEntity<FacturaDto> crearFactura(@RequestBody FacturaDto facturaDto) {
        FacturaDto creada = facturaService.guardar(facturaDto);
        return ResponseEntity.ok(creada);
    }

    // PUT /api/factura/{id} - actualizar una factura existente
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarFactura(@PathVariable Integer id, @RequestBody FacturaDto facturaDto) {
        facturaService.actualizar(id, facturaDto);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/factura/{id} - eliminar una factura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Integer id) {
        facturaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
