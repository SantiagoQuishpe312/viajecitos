package ec.edu.monster.mapper;

import ec.edu.monster.dto.TransaccionBancariaDto;
import ec.edu.monster.modelo.TransaccionBancaria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransaccionBancariaMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "monto", source = "monto")
    @Mapping(target = "fecha", source = "fecha")
    TransaccionBancariaDto toDTO(TransaccionBancaria entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "cliente.clienteId", source = "clienteId")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "monto", source = "monto")
    @Mapping(target = "fecha", source = "fecha")
    TransaccionBancaria toEntity(TransaccionBancariaDto dto);
}
