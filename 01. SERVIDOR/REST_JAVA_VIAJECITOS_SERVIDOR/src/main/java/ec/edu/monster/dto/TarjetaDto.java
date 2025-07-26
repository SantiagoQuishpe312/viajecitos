package ec.edu.monster.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TarjetaDto {
    private Long tarjetaId;
    private String tarjetaCvv;
    private Date tarjetaFechaCad;
    private Date tarjetaFechaRegistro;
    private String tarjetaNumero;
    private String tarjetaProv;
    private BigDecimal tarjetaSaldo;
    private String tarjetaTipo;
}
