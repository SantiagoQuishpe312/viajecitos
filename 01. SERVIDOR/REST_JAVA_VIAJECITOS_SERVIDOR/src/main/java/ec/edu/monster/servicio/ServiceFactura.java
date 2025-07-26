package ec.edu.monster.servicio;

import ec.edu.monster.dto.*;
import ec.edu.monster.mapper.*;
import ec.edu.monster.modelo.*;
import ec.edu.monster.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceFactura implements IServiceFactura {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private BoletoRepository boletoRepository;

    @Autowired
    private VueloRepository vueloRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private BoletoMapper boletoMapper;

    @Autowired
    private FacturaMapper facturaMapper;
    @Autowired
    private CiudadMapper ciudadMapper;
    @Autowired
    private VueloMapper vueloMapper;
    @Autowired
    private IServiceAsiento serviceAsiento;
    @Autowired
    private CreditoFacturaRepository creditoFacturaRepository;
    @Autowired
    private CreditoFacturaMapper creditoFacturaMapper;

    @Override
    public List<FacturaDto> obtenerTodas() {
        return facturaRepository.findAll()
                .stream()
                .map(facturaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FacturaCompletoDto> obtenerPorId(Integer id) {
        return facturaRepository.findById(id).map(factura -> {
            FacturaCompletoDto dto = new FacturaCompletoDto();

            // Mapear factura
            dto.setFactura(facturaMapper.toDto(factura));

            // Mapear cliente
            dto.setCliente(clienteRepository.findById(factura.getCliente().getClienteId())
                    .map(clienteMapper::toDTO)
                    .orElse(null)); // o lanzar excepción si prefieres

            // Obtener boletos relacionados a la factura
            List<BoletoInfoDto> boletos = boletoRepository.boletoporFactura(factura.getFacturaId())
                    .map(lista -> lista.stream()
                            .map(this::convertirABoletoInfoDto)
                            .collect(Collectors.toList()))
                    .orElseGet(ArrayList::new);

            dto.setBoleto(boletos);
            Optional<CreditoFactura> optionalCreditoFactura = creditoFacturaRepository.obtenerCreditos(id);

            if (optionalCreditoFactura.isPresent()) {
                CreditoFactura creditoFactura = optionalCreditoFactura.get();
                creditoFactura.setTasaInteres(new BigDecimal("16.9"));
                dto.setCredito(creditoFacturaMapper.toDTO(creditoFactura));
            } else {
                // Redirigir con respuesta vacía (puede variar según el tipo de aplicación)
                dto.setCredito(new CreditoFacturaDto());
            }

            return dto;
        });
    }

    private BoletoInfoDto convertirABoletoInfoDto(Boleto boleto) {
        BoletoInfoDto dto = new BoletoInfoDto();

        // Mapear el boleto
        dto.setBoleto(boletoMapper.toDTO(boleto));

        // Mapear el vuelo
        if (boleto.getVuelo() != null) {
            dto.setVuelo(vueloMapper.toDto(boleto.getVuelo()));

            // Mapear ciudad origen
            if (boleto.getVuelo().getCiudadOrigen() != null) {
                dto.setCuidadOrigen(ciudadMapper.toDTO(boleto.getVuelo().getCiudadOrigen()));
            }

            // Mapear ciudad destino
            if (boleto.getVuelo().getCiudadDestino() != null) {
                dto.setCiudadDestino(ciudadMapper.toDTO(boleto.getVuelo().getCiudadDestino()));
            }
        }

        return dto;
    }


    @Override
    public FacturaDto guardar(FacturaDto facturaDto) {
        // 1. Validar cliente
        if (facturaDto.getClienteId() == null) {
            throw new RuntimeException("El clienteId no puede ser nulo.");
        }

        // 2. Obtener todos los boletos sin factura del cliente
        List<Boleto> boletosSinFactura = boletoRepository.boletoSinPagar(facturaDto.getClienteId())
                .orElseThrow(() -> new RuntimeException("No hay boletos sin pagar para el cliente con ID: " + facturaDto.getClienteId()));

        if (boletosSinFactura.isEmpty()) {
            throw new RuntimeException("No hay boletos pendientes de pago.");
        }

        // 3. Calcular subtotal
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Boleto boleto : boletosSinFactura) {
            if (boleto.getVuelo() == null || boleto.getVuelo().getVueloValor() == null) {
                throw new RuntimeException("Boleto con datos incompletos: vuelo o valor nulo.");
            }

            BigDecimal precio = boleto.getVuelo().getVueloValor()
                    .multiply(BigDecimal.valueOf(boleto.getBoletoCantidad()));

            subtotal = subtotal.add(precio);
        }

        CreditoFactura creditoFactura=new CreditoFactura();
        // 4. Calcular IVA y Total
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.15"));
        BigDecimal total = subtotal.add(iva);

        // 5. Crear entidad Factura
        Factura factura = new Factura();
        factura.setCliente(clienteRepository.findById(facturaDto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        factura.setFecha(LocalDateTime.now());
        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);
        factura.setEstado(facturaDto.getEstado());
        factura.setMetodoPago(facturaDto.getMetodoPago());

        // 6. Guardar factura (ya con ID generado)
        Factura facturaGuardada = facturaRepository.save(factura);
        if(facturaDto.getMetodoPago().equals("CREDITO")){
            creditoFactura.setFactura(facturaGuardada);
            creditoFactura.setNumeroCuotas(facturaDto.getCuotas());
            creditoFactura.setTipoAmortizacion(facturaDto.getAmortizacion());
            creditoFactura.setMontoFinanciado(total);
            creditoFactura.setTasaInteres(new BigDecimal("16.9"));
            LocalDate fechaLocal = LocalDate.now();
            creditoFactura.setFechaRegistro(fechaLocal);
            creditoFacturaRepository.save(creditoFactura);
        }

        // 7. Actualizar boletos con factura y estado PAGADO
        for (Boleto boleto : boletosSinFactura) {
            boleto.setFactura(facturaGuardada);
            boleto.setBoletoEstado("PAGADO");

            // 8. Actualizar capacidad del vuelo
            serviceAsiento.confirmar(boleto.getBoletoId());

            boletoRepository.save(boleto);
        }
        // 9. Devolver el DTO de la factura
        return facturaMapper.toDto(facturaGuardada);
    }

    @Override
    public Optional<List<FacturaCompletoDto>> obtenerPorUsuario(Integer id) {
        Optional<List<Factura>> facturasOpt = facturaRepository.facturaByCliente(id);

        if (facturasOpt.isEmpty() || facturasOpt.get().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(facturasOpt.get().stream().map(factura -> {
            FacturaCompletoDto dto = new FacturaCompletoDto();

            // Mapeo con mapper
            dto.setFactura(facturaMapper.toDto(factura));

            // Cliente mapeado con mapper (asegúrate que tengas uno)
            dto.setCliente(clienteMapper.toDTO(factura.getCliente()));

            // Boletos de la factura
            List<BoletoInfoDto> boletos = boletoRepository.boletoporFactura(factura.getFacturaId())
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(boleto -> {
                        BoletoInfoDto bDto = new BoletoInfoDto();
                        bDto.setBoleto(boletoMapper.toDTO(boleto));

                        if (boleto.getVuelo() != null) {
                            bDto.setVuelo(vueloMapper.toDto(boleto.getVuelo()));

                            if (boleto.getVuelo().getCiudadOrigen() != null) {
                                bDto.setCuidadOrigen(ciudadMapper.toDTO(boleto.getVuelo().getCiudadOrigen()));
                            }

                            if (boleto.getVuelo().getCiudadDestino() != null) {
                                bDto.setCiudadDestino(ciudadMapper.toDTO(boleto.getVuelo().getCiudadDestino()));
                            }
                        }

                        return bDto;
                    }).collect(Collectors.toList());

            dto.setBoleto(boletos);

            return dto;
        }).collect(Collectors.toList()));
    }


    @Override
    public void eliminarPorId(Integer id) {
        facturaRepository.deleteById(id);
    }

    @Override
    public void actualizar(Integer id, FacturaDto facturaDto) {
        Factura facturaExistente = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
        Cliente cliente=new Cliente();
        cliente.setClienteId(facturaDto.getClienteId());
        facturaExistente.setCliente(cliente);
        facturaExistente.setFecha(facturaDto.getFecha());
        facturaExistente.setSubtotal(facturaDto.getSubtotal());
        facturaExistente.setIva(facturaDto.getIva());
        facturaExistente.setTotal(facturaDto.getTotal());
        facturaExistente.setEstado(facturaDto.getEstado());
        facturaExistente.setMetodoPago(facturaDto.getMetodoPago());
        facturaRepository.save(facturaExistente);
    }
}
