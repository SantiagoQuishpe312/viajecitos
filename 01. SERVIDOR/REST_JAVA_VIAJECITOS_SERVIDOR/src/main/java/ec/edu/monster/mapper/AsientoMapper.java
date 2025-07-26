package ec.edu.monster.mapper;

import ec.edu.monster.dto.AsientoDto;
import ec.edu.monster.modelo.Asiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsientoMapper {

    @Mapping(target = "asientoId", source = "asientoId")
    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "vueloId", source = "vuelo.vueloId")
    @Mapping(target = "fila", source = "fila")
    @Mapping(target = "columna", source = "columna")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "boletoId", source = "boleto.boletoId")
    AsientoDto toDTO(Asiento asiento);

    @Mapping(target = "asientoId", source = "asientoId")
    @Mapping(target = "vuelo.vueloId", source = "vueloId")
    @Mapping(target = "cliente.clienteId", source = "clienteId")
    @Mapping(target = "fila", source = "fila")
    @Mapping(target = "columna", source = "columna")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "boleto.boletoId", source = "boletoId")
    Asiento toEntity(AsientoDto asientoDto);
}
