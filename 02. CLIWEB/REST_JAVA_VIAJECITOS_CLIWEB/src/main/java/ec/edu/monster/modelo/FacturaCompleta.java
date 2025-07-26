package ec.edu.monster.modelo;

import lombok.Data;

import java.util.List;

@Data
public class FacturaCompleta {
    public Factura factura;
    public Cliente cliente;
    public List<BoletoInfo> boleto;
    public CreditoFactura credito;

}
