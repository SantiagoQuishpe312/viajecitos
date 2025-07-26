package ec.edu.monster.dto;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class FacturaCompletoDto {
    public FacturaDto factura;
    public ClienteDto cliente;
    public List<BoletoInfoDto> boleto;
    public CreditoFacturaDto credito;
}
