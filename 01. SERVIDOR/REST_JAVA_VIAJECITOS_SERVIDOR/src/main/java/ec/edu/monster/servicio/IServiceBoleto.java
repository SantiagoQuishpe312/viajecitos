package ec.edu.monster.servicio;

import ec.edu.monster.dto.BoletoDto;
import ec.edu.monster.dto.BoletoInfoDto;
import ec.edu.monster.dto.VueloDto;
import ec.edu.monster.modelo.Boleto;

import java.util.List;
import java.util.Optional;

public interface IServiceBoleto {
    List<BoletoInfoDto> obtenerBoletosByClient(Integer idCliente);
    BoletoDto guardar(BoletoDto boletoDto);
    BoletoInfoDto getById(Integer id);
    List<BoletoInfoDto> obtenerBoletosNoPagados(Integer idCliente);
    void eliminarPorId(Integer id);
    Optional<BoletoDto> obtenerPorId(Integer id);

}
