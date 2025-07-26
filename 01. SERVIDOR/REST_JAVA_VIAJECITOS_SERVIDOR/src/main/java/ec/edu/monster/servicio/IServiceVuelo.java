package ec.edu.monster.servicio;

import ec.edu.monster.dto.VueloDto;
import ec.edu.monster.modelo.Vuelo;
import java.util.List;
import java.util.Optional;

public interface IServiceVuelo {
    List<VueloDto> obtenerTodos();
    Optional<VueloDto> obtenerPorId(Integer id);
    VueloDto guardar(VueloDto vuelo);
    void eliminarPorId(Integer id);
    Optional<List<VueloDto>> obtenerPorCiudades(String destino, String origen);
    void actualizar(Integer id,VueloDto vuelo);
}
