package ec.edu.monster.servicio;

import ec.edu.monster.dto.ClienteDto;
import ec.edu.monster.modelo.Cliente;

import java.util.List;
import java.util.Optional;

public interface IServiceCliente {

    // Crear o actualizar un cliente
    ClienteDto guardarCliente(ClienteDto cliente);

    // Eliminar cliente por ID
    void eliminarClientePorId(Integer clienteId);

    // Buscar cliente por ID
    Optional<ClienteDto> obtenerClientePorId(Integer clienteId);

    // Listar todos los clientes
    List<ClienteDto> listarClientes();

    // Buscar clientes referidos por un cliente referente
    List<ClienteDto> obtenerClientesPorReferente(Integer clienteReferenteId);
}
