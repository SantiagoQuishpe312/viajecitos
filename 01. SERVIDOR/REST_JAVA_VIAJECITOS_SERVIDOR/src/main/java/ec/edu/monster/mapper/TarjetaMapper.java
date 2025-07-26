package ec.edu.monster.mapper;

import ec.edu.monster.dto.TarjetaDto;
import ec.edu.monster.modelo.Tarjeta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarjetaMapper {

    TarjetaDto toDto(Tarjeta tarjeta);

    Tarjeta toEntity(TarjetaDto dto);


}
