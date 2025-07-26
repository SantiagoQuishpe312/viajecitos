package ec.edu.monster.mapper;

import ec.edu.monster.dto.CiudadDto;
import ec.edu.monster.modelo.Ciudad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CiudadMapper {

    @Mapping(target = "ciudadCodigo", source = "ciudadCodigo")
    @Mapping(target = "ciudadNombre", source = "ciudadNombre")
    @Mapping(target = "ciudadPaisOrigen", source = "ciudadPaisOrigen")
    @Mapping(target = "ciudadNombreAeropuerto", source = "ciudadNombreAeropuerto")
    @Mapping(target = "ciudadLatitud", source = "ciudadLatitud")
    @Mapping(target = "ciudadLongitud", source = "ciudadLongitud")
    CiudadDto toDTO(Ciudad ciudad);

    @Mapping(target = "ciudadCodigo", source = "ciudadCodigo")
    @Mapping(target = "ciudadNombre", source = "ciudadNombre")
    @Mapping(target = "ciudadPaisOrigen", source = "ciudadPaisOrigen")
    @Mapping(target = "ciudadNombreAeropuerto", source = "ciudadNombreAeropuerto")
    @Mapping(target = "ciudadLatitud", source = "ciudadLatitud")
    @Mapping(target = "ciudadLongitud", source = "ciudadLongitud")
    Ciudad toEntity(CiudadDto ciudadDto);
}
