package ec.edu.monster.mapper;

import ec.edu.monster.dto.CreditoBancarioDto;
import ec.edu.monster.modelo.CreditoBancario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditoBancarioMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "montoAprobado", source = "montoAprobado")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "fechaAprobacion", source = "fechaAprobacion")
    @Mapping(target = "plazoMeses", source = "plazoMeses")
    @Mapping(target = "tasaInteres", source = "tasaInteres")
    CreditoBancarioDto toDTO(CreditoBancario entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "cliente.clienteId", source = "clienteId")
    @Mapping(target = "montoAprobado", source = "montoAprobado")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "fechaAprobacion", source = "fechaAprobacion")
    @Mapping(target = "plazoMeses", source = "plazoMeses")
    @Mapping(target = "tasaInteres", source = "tasaInteres")
    CreditoBancario toEntity(CreditoBancarioDto dto);
}
