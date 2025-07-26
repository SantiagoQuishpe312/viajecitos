package ec.edu.monster.mapper;

import ec.edu.monster.dto.BoletoDto;
import ec.edu.monster.modelo.Boleto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoletoMapper {

    @Mapping(target = "boletoId", source = "boletoId")
    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "vueloId", source = "vuelo.vueloId")
    @Mapping(target = "facturaId", source = "factura.facturaId")
    @Mapping(target = "boletoCantidad", source = "boletoCantidad")
    @Mapping(target = "boletoEstado", source = "boletoEstado")
    @Mapping(target = "boletoFechaCompra", source = "boletoFechaCompra")
    BoletoDto toDTO(Boleto boleto);

    @Mapping(target = "boletoId", source = "boletoId")
    @Mapping(target = "cliente.clienteId", source = "clienteId")
    @Mapping(target = "vuelo.vueloId", source = "vueloId")
    @Mapping(target = "factura.facturaId", source = "facturaId")
    @Mapping(target = "boletoCantidad", source = "boletoCantidad")
    @Mapping(target = "boletoEstado", source = "boletoEstado")
    @Mapping(target = "boletoFechaCompra", source = "boletoFechaCompra")
    Boleto toEntity(BoletoDto boletoDto);
}
