package ec.edu.monster.servicio;

import ec.edu.monster.dto.TarjetaDto;
import ec.edu.monster.mapper.TarjetaMapper;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.ClienteTarjeta;
import ec.edu.monster.modelo.Tarjeta;
import ec.edu.monster.repository.ClienteRepository;
import ec.edu.monster.repository.TarjetaClienteRepository;
import ec.edu.monster.repository.TarjetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceTarjeta implements IServiceTarjeta{

    @Autowired
    private TarjetaRepository tarjetaRepository;
    @Autowired
    private TarjetaClienteRepository tarjetaClienteRepository;
    @Autowired
    private TarjetaMapper tarjetaMapper;
    @Autowired
    private ClienteRepository clienteRepository;
    @Override
    public List<TarjetaDto> obtenerPorClienteId(Integer id) {
        Optional<List<ClienteTarjeta>> clienteTarjetasOpt = tarjetaRepository.tarjetaByCliente(id);

        // Si no hay datos, devolver una lista vacía
        if (clienteTarjetasOpt.isEmpty()) {
            return new ArrayList<>();
        }

        // Convertir ClienteTarjeta → Tarjeta → TarjetaDto
        return clienteTarjetasOpt.get().stream()
                .map(clienteTarjeta -> {
                    Tarjeta tarjeta = clienteTarjeta.getTarjeta(); // Asumiendo que existe getTarjeta()
                    return tarjetaMapper.toDto(tarjeta);
                    // Ajusta los campos según tu clase TarjetaDto
                })
                .collect(Collectors.toList());
    }


    @Override
    public TarjetaDto guardarTarjeta(TarjetaDto tarjetaDto, Integer idCliente) {
        // 1. Buscar el cliente
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2. Mapear DTO a entidad Tarjeta
        Tarjeta tarjeta = tarjetaMapper.toEntity(tarjetaDto);

        // 3. Guardar la tarjeta
        tarjeta = tarjetaRepository.save(tarjeta);

        // 4. Crear la relación ClienteTarjeta
        ClienteTarjeta clienteTarjeta = new ClienteTarjeta();
        clienteTarjeta.setCliente(cliente);
        clienteTarjeta.setTarjeta(tarjeta);
        clienteTarjeta.setClienteTarjetaFecha(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        );
        // 5. Guardar en la tabla intermedia
        tarjetaClienteRepository.save(clienteTarjeta);

        return tarjetaMapper.toDto(tarjeta);
    }

    @Override
    @Transactional
    public void eliminarTarjetaPorId(Integer idTarjeta) {
        // 1. Eliminar la(s) relación(es) en ClienteTarjeta
        List<ClienteTarjeta> relaciones = tarjetaClienteRepository.findByTarjeta_TarjetaId(idTarjeta);

        for (ClienteTarjeta rel : relaciones) {
            tarjetaClienteRepository.delete(rel);
        }

        // 2. Eliminar la tarjeta
        tarjetaRepository.deleteById(idTarjeta);
    }

}
