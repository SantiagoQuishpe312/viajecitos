package ec.edu.monster.mapper;

import ec.edu.monster.dto.ClienteTarjetaDto;
import ec.edu.monster.modelo.ClienteTarjeta;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.Tarjeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ClienteTarjetaMapper {

    @Mappings({
            @Mapping(target = "clienteId", source = "cliente.clienteId"),
            @Mapping(target = "tarjetaId", source = "tarjeta.tarjetaId"),
            @Mapping(target = "clienteTarjetaFecha", source = "clienteTarjetaFecha")
    })
    ClienteTarjetaDto toDTO(ClienteTarjeta clienteTarjeta);

    @Mappings({
            @Mapping(target = "cliente.clienteId", source = "clienteId"),
            @Mapping(target = "tarjeta.tarjetaId", source = "tarjetaId"),
            @Mapping(target = "clienteTarjetaFecha", source = "clienteTarjetaFecha")
    })
    ClienteTarjeta toEntity(ClienteTarjetaDto dto);

    // Métodos de soporte para construir objetos con solo el ID
}
