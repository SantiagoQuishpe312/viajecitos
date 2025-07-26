package ec.edu.monster.servicio;

import ec.edu.monster.dto.AsientoDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IServiceAsiento {
    List<AsientoDto> obtenerPorVuelo(Integer vueloId);
    List<AsientoDto> obtenerDisponiblesPorVuelo(Integer vueloId);
    void liberar(Integer boletoId);
    void confirmar(Integer boletoId);
    Map<String, Long> obtenerResumenAsientosPorVuelo(Integer vueloId);

    AsientoDto reservar(Integer asientoId, Integer boletoId, Integer clienteId);
    AsientoDto pagar(Integer asientoId);
    AsientoDto guardar(AsientoDto dto);
    Optional<AsientoDto> obtenerPorId(Integer id);
    void eliminar(Integer id);
}
