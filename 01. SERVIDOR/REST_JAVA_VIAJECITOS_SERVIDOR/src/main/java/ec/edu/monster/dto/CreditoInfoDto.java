package ec.edu.monster.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditoInfoDto {
    private boolean sujetoCredito;
    private BigDecimal creditoDisponible;
}
