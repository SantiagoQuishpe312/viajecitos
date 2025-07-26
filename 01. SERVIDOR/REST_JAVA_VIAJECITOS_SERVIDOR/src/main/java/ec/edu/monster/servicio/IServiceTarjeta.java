package ec.edu.monster.servicio;

import ec.edu.monster.dto.FacturaDto;
import ec.edu.monster.dto.TarjetaDto;

import java.util.List;

public interface IServiceTarjeta {
    List<TarjetaDto> obtenerPorClienteId(Integer id);
    TarjetaDto guardarTarjeta(TarjetaDto tarjetaDto,Integer idCliente);
    void eliminarTarjetaPorId(Integer idTarjeta);


}
