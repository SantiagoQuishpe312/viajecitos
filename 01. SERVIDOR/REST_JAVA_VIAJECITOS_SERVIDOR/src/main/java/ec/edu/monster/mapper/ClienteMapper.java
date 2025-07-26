package ec.edu.monster.mapper;

import ec.edu.monster.dto.ClienteDto;
import ec.edu.monster.modelo.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "clienteReferenteId", source = "clienteReferente.clienteId")
    ClienteDto toDTO(Cliente cliente);

    // De DTO a entidad
    @Mapping(target = "clienteReferente.clienteId", source = "clienteReferenteId")
    Cliente toEntity(ClienteDto clienteDto);
}
