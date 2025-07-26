package ec.edu.monster.controlador;

import ec.edu.monster.dto.ClienteDto;
import ec.edu.monster.servicio.IServiceCliente;
import ec.edu.monster.util.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/cliente")
public class ClienteController {

    @Autowired
    private IServiceCliente serviceCliente;

    // Crear o actualizar cliente
    @PostMapping
    public ResponseEntity<ClienteDto> guardarCliente(@RequestBody ClienteDto clienteDto) {
        ClienteDto guardado = serviceCliente.guardarCliente(clienteDto);
        return ResponseEntity.ok(guardado);
    }

    // Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerCliente(@PathVariable("id") Integer id) {
        Optional<ClienteDto> cliente = serviceCliente.obtenerClientePorId(id);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos los clientes
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarClientes() {
        List<ClienteDto> clientes = serviceCliente.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    // Eliminar cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable("id") Integer id) {
        serviceCliente.eliminarClientePorId(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener clientes referidos por un cliente referente
    @GetMapping("/referente/{referenteId}")
    public ResponseEntity<List<ClienteDto>> obtenerClientesReferidos(@PathVariable("referenteId") Integer referenteId) {
        List<ClienteDto> referidos = serviceCliente.obtenerClientesPorReferente(referenteId);
        return ResponseEntity.ok(referidos);
    }
}
