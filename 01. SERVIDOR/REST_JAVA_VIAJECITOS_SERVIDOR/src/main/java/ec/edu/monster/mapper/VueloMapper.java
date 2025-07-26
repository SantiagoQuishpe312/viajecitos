package ec.edu.monster.mapper;

import ec.edu.monster.dto.VueloDto;
import ec.edu.monster.modelo.Ciudad;
import ec.edu.monster.modelo.Vuelo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface VueloMapper {

    @Mapping(target = "ciudadOrigenCodigo", source = "ciudadOrigen.ciudadCodigo")
    @Mapping(target = "ciudadDestinoCodigo", source = "ciudadDestino.ciudadCodigo")
    VueloDto toDto(Vuelo vuelo);

    @Mapping(target = "ciudadOrigen.ciudadCodigo", source = "ciudadOrigenCodigo")
    @Mapping(target = "ciudadDestino.ciudadCodigo", source = "ciudadDestinoCodigo")
    Vuelo toEntity(VueloDto dto);

}
