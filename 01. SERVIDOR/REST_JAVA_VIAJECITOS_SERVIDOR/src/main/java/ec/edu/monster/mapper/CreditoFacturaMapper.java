package ec.edu.monster.mapper;

import ec.edu.monster.dto.CreditoFacturaDto;
import ec.edu.monster.modelo.CreditoFactura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditoFacturaMapper {

    @Mapping(target = "facturaId", source = "factura.facturaId")
    CreditoFacturaDto toDTO(CreditoFactura creditoFactura);

    @Mapping(target = "factura.facturaId", source = "facturaId")
    CreditoFactura toEntity(CreditoFacturaDto dto);


}
