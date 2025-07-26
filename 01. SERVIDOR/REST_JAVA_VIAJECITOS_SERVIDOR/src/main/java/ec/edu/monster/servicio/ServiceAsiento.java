package ec.edu.monster.servicio;

import ec.edu.monster.dto.AsientoDto;
import ec.edu.monster.mapper.AsientoMapper;
import ec.edu.monster.modelo.Asiento;
import ec.edu.monster.modelo.Boleto;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.repository.AsientoRepository;
import ec.edu.monster.repository.BoletoRepository;
import ec.edu.monster.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceAsiento implements IServiceAsiento {

    @Autowired
    private AsientoRepository asientoRepository;

    @Autowired
    private AsientoMapper asientoMapper;
    @Autowired
    private BoletoRepository boletoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<AsientoDto> obtenerPorVuelo(Integer vueloId) {
        List<Asiento> asientos = asientoRepository.findByVueloVueloId(vueloId);
        return asientos.stream()
                .map(asientoMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public Map<String, Long> obtenerResumenAsientosPorVuelo(Integer vueloId) {
        Long total = asientoRepository.countAsientosPorVueloId(vueloId);
        Long disponibles = asientoRepository.countAsientosDisponiblesPorVueloId(vueloId);

        Map<String, Long> resumen = new HashMap<>();
        resumen.put("totalAsientos", total);
        resumen.put("asientosDisponibles", disponibles);

        return resumen;
    }
    @Override
    public List<AsientoDto> obtenerDisponiblesPorVuelo(Integer vueloId) {
        List<Asiento> disponibles = asientoRepository.findAsientosDisponiblesPorVuelo(vueloId);
        return disponibles.stream()
                .map(asientoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AsientoDto reservar(Integer asientoId,Integer boletoId,Integer clienteId) {
        Asiento asiento = asientoRepository.findById(asientoId)
                .orElseThrow(() -> new EntityNotFoundException("Asiento no encontrado"));
        if (asiento.getEstado() != Asiento.EstadoAsiento.DISPONIBLE) {
            throw new IllegalStateException("El asiento no está disponible para reservar");
        }
        asiento.setEstado(Asiento.EstadoAsiento.RESERVADO);
        Boleto boleto=boletoRepository.getById(boletoId);
        Cliente cliente=clienteRepository.getById(clienteId);
        asiento.setCliente(cliente);
        asiento.setBoleto(boleto);
        return asientoMapper.toDTO(asientoRepository.save(asiento));
    }
    @Override
    public void liberar(Integer boletoId) {
        List<Asiento> asientos = asientoRepository.findAsientosByBoleto(boletoId);
        for (Asiento asiento : asientos) {
            if (asiento.getEstado() != Asiento.EstadoAsiento.RESERVADO) {
                throw new IllegalStateException("El asiento " + asiento.getAsientoId() + " no está disponible para reservar");
            }
            asiento.setEstado(Asiento.EstadoAsiento.DISPONIBLE);
            asiento.setBoleto(null);
        }
        // Guardar todos los asientos actualizados en batch
        asientoRepository.saveAll(asientos);
    }

    @Override
    public void confirmar(Integer boletoId) {
        List<Asiento> asientos = asientoRepository.findAsientosByBoleto(boletoId);
        for (Asiento asiento : asientos) {
            if (asiento.getEstado() != Asiento.EstadoAsiento.RESERVADO) {
                throw new IllegalStateException("El asiento " + asiento.getAsientoId() + " no está disponible para reservar");
            }
            asiento.setEstado(Asiento.EstadoAsiento.PAGADO);

            Boleto boleto=boletoRepository.getById(boletoId);
            asiento.setBoleto(boleto);        }
        // Guardar todos los asientos actualizados en batch
        asientoRepository.saveAll(asientos);
    }


    @Override
    public AsientoDto pagar(Integer asientoId) {
        Asiento asiento = asientoRepository.findById(asientoId)
                .orElseThrow(() -> new EntityNotFoundException("Asiento no encontrado"));

        if (asiento.getEstado() != Asiento.EstadoAsiento.RESERVADO) {
            throw new IllegalStateException("El asiento no está reservado para poder pagar");
        }

        asiento.setEstado(Asiento.EstadoAsiento.PAGADO);
        return asientoMapper.toDTO(asientoRepository.save(asiento));
    }

    @Override
    public AsientoDto guardar(AsientoDto dto) {
        Asiento asiento = asientoMapper.toEntity(dto);
        Asiento guardado = asientoRepository.save(asiento);
        return asientoMapper.toDTO(guardado);
    }

    @Override
    public Optional<AsientoDto> obtenerPorId(Integer id) {
        return asientoRepository.findById(id)
                .map(asientoMapper::toDTO);
    }

    @Override
    public void eliminar(Integer id) {
        if (!asientoRepository.existsById(id)) {
            throw new EntityNotFoundException("Asiento con ID " + id + " no existe");
        }
        asientoRepository.deleteById(id);
    }
}
