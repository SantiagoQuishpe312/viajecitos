package ec.edu.monster.modelo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class Tarjeta {
    private Long tarjetaId;
    private String tarjetaCvv;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tarjetaFechaCad;
    private Date tarjetaFechaRegistro;
    private String tarjetaNumero;
    private String tarjetaProv;
    private BigDecimal tarjetaSaldo;
    private String tarjetaTipo;
}
