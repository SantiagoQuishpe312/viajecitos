package ec.edu.monster.mapper;

import ec.edu.monster.dto.FacturaDto;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FacturaMapper {

    @Mapping(target = "clienteId", source = "cliente.clienteId")
        // MapStruct hace el resto de campos automáticamente si tienen el mismo nombre
    FacturaDto toDto(Factura factura);

    @Mapping(target = "cliente.clienteId", source = "clienteId")
    Factura toEntity(FacturaDto dto);


}
