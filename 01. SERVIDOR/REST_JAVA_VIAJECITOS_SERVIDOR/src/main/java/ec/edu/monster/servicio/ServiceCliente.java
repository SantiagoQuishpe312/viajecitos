package ec.edu.monster.servicio;

import ec.edu.monster.dto.ClienteDto;
import ec.edu.monster.mapper.ClienteMapper;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceCliente implements IServiceCliente {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Override
    public ClienteDto guardarCliente(ClienteDto clienteDto) {
        Cliente cliente = clienteMapper.toEntity(clienteDto);
        if (cliente.getClientesReferidos() == null || cliente.getClientesReferidos().isEmpty()) {
            cliente.setClienteReferente(clienteRepository.getById(1));
        }
        cliente.setClienteContrasena(cliente.getClienteCedula());
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.toDTO(guardado);
    }

    @Override
    public void eliminarClientePorId(Integer clienteId) {
        clienteRepository.deleteById(clienteId);
    }

    @Override
    public Optional<ClienteDto> obtenerClientePorId(Integer clienteId) {
        return clienteRepository.findById(clienteId)
                .map(clienteMapper::toDTO);
    }

    @Override
    public List<ClienteDto> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteDto> obtenerClientesPorReferente(Integer clienteReferenteId) {
        return clienteRepository.findByClienteCreate(clienteReferenteId).stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }
}
