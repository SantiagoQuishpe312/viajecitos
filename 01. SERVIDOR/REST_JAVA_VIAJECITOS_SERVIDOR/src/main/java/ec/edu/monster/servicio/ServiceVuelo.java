package ec.edu.monster.servicio;

import ec.edu.monster.dto.VueloDto;
import ec.edu.monster.mapper.VueloMapper;
import ec.edu.monster.modelo.Vuelo;
import ec.edu.monster.repository.CiudadRepository;
import ec.edu.monster.repository.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceVuelo implements IServiceVuelo {

    @Autowired
    private VueloRepository vueloRepository;
    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private VueloMapper vueloMapper;
    @Autowired
    private IServiceAsiento serviceAsiento;

    @Override
    public List<VueloDto> obtenerTodos() {
        return vueloRepository.findVueloConResumenAsientos();

    }



    @Override
    public Optional<VueloDto> obtenerPorId(Integer id) {
        return vueloRepository.findById(id)
                .map(vueloMapper::toDto); // ✅ convertir a DTO
    }

    @Override
    public Optional<List<VueloDto>> obtenerPorCiudades(String destino, String origen) {
        return vueloRepository.vuelosByCiudades(destino, origen)
                .map(lista -> lista.stream()
                        .map(vueloMapper::toDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public VueloDto guardar(VueloDto vueloDto) {
        Vuelo vuelo = vueloMapper.toEntity(vueloDto); // ✅ convertir DTO a entidad
        Vuelo vueloGuardado = vueloRepository.save(vuelo);
        return vueloMapper.toDto(vueloGuardado); // ✅ devolver DTO
    }

    @Override
    public void eliminarPorId(Integer id) {
        vueloRepository.deleteById(id);
    }
    @Override
    public void actualizar(Integer id, VueloDto vueloDto) {
        Optional<Vuelo> optionalVuelo = vueloRepository.findById(id);
        if (optionalVuelo.isPresent()) {
            Vuelo vueloExistente = optionalVuelo.get();

            // Mapear los nuevos datos del DTO al vuelo existente
            Vuelo vueloActualizado = vueloMapper.toEntity(vueloDto);
            vueloActualizado.setVueloId(id); // Asegurar que conserve el ID

            vueloRepository.save(vueloActualizado);
        } else {
            throw new RuntimeException("Vuelo con ID " + id + " no encontrado");
        }
    }


}
