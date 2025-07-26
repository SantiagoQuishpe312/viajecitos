package ec.edu.monster.servicio;

import ec.edu.monster.dto.FacturaCompletoDto;
import ec.edu.monster.dto.FacturaDto;

import java.util.List;
import java.util.Optional;

public interface IServiceFactura {

    List<FacturaDto> obtenerTodas();

    Optional<FacturaCompletoDto> obtenerPorId(Integer id);

    Optional<List<FacturaCompletoDto>> obtenerPorUsuario(Integer id);

    FacturaDto guardar(FacturaDto facturaDto);

    void eliminarPorId(Integer id);

    void actualizar(Integer id, FacturaDto facturaDto);
}
