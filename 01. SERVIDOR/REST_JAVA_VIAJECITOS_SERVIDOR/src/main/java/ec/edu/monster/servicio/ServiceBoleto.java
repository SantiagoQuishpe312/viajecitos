package ec.edu.monster.servicio;

import ec.edu.monster.dto.AsientoDto;
import ec.edu.monster.dto.BoletoDto;
import ec.edu.monster.dto.BoletoInfoDto;
import ec.edu.monster.mapper.AsientoMapper;
import ec.edu.monster.mapper.BoletoMapper;
import ec.edu.monster.mapper.CiudadMapper;
import ec.edu.monster.mapper.VueloMapper;
import ec.edu.monster.modelo.Asiento;
import ec.edu.monster.modelo.Boleto;
import ec.edu.monster.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceBoleto implements IServiceBoleto{
    @Autowired
    private BoletoRepository boletoRepository;
    @Autowired
    private VueloRepository vueloRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private  IServiceAsiento serviceAsiento;
    @Autowired
    private AsientoRepository asientoRepository;

    @Autowired
    private AsientoMapper asientoMapper;

    @Autowired
    private CiudadRepository ciudadRepository;
   @Autowired
   private BoletoMapper boletoMapper;
    @Autowired
    private VueloMapper vueloMapper;
    @Autowired
    private CiudadMapper ciudadMapper;
    @Override
    public List<BoletoInfoDto> obtenerBoletosByClient(Integer idCliente) {
        return boletoRepository.boletosByClient(idCliente)
                .map(listaBoletos -> listaBoletos.stream().map(boleto -> {
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
// Mapear los asientos
                    dto.setAsientos(
                            asientoRepository.findAsientosByBoleto(boleto.getBoletoId())
                                    .stream()
                                    .map(asientoMapper::toDTO)
                                    .collect(Collectors.toList())
                    );

                    return dto;
                }).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
        // devuelve lista vacía si no hay
    }

    @Override
    public BoletoInfoDto getById(Integer id) {
        return boletoRepository.findById(id)
                .map(boleto -> {
                    BoletoInfoDto dto = new BoletoInfoDto();

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
// Mapear los asientos
                    List<Asiento> asientoList = asientoRepository.findAsientosByBoleto(boleto.getBoletoId());

                    List<AsientoDto> asientoDtoList = asientoList.stream()
                            .map(asientoMapper::toDTO)
                            .collect(Collectors.toList());

// Relacionar cada Asiento con su DTO
                    for (int i = 0; i < asientoDtoList.size(); i++) {
                        Asiento asiento = asientoList.get(i);
                        AsientoDto dtoAs = asientoDtoList.get(i);

                        if (asiento.getCliente() != null) {
                            dtoAs.setNombreCliente(asiento.getCliente().getClienteNombre() +" - "+asiento.getCliente().getClienteCedula());
                        }
                    }


                    dto.setAsientos(asientoDtoList);

                    return dto;
                })
                .orElseThrow(() -> new EntityNotFoundException("Boleto con ID " + id + " no encontrado"));
    }

    @Override
    public BoletoDto guardar(BoletoDto boletoDto){
        Boleto boleto = boletoMapper.toEntity(boletoDto);

        // Cargar Cliente real
        if(boletoDto.getClienteId() != null){
            boleto.setCliente(clienteRepository.findById(boletoDto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        }

        // Cargar Vuelo real
        if(boletoDto.getVueloId() != null){
            boleto.setVuelo(vueloRepository.findById(boletoDto.getVueloId())
                    .orElseThrow(() -> new RuntimeException("Vuelo no encontrado")));
        }

        // Cargar Factura real o null
        if(boletoDto.getFacturaId() != null){
            boleto.setFactura(facturaRepository.findById(boletoDto.getFacturaId())
                    .orElseThrow(() -> new RuntimeException("Factura no encontrada")));
        } else {
            boleto.setFactura(null);
        }

        Boleto creado = boletoRepository.save(boleto);
        return boletoMapper.toDTO(creado);
    }
    @Override
    public List<BoletoInfoDto> obtenerBoletosNoPagados(Integer idCliente) {
        return boletoRepository.boletoSinPagar(idCliente)
                .map(listaBoletos -> listaBoletos.stream().map(boleto -> {
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
// Mapear los asientos
                    dto.setAsientos(
                            asientoRepository.findAsientosByBoleto(boleto.getBoletoId())
                                    .stream()
                                    .map(asientoMapper::toDTO)
                                    .collect(Collectors.toList())
                    );

                    return dto;
                }).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }
    @Override
    public void eliminarPorId(Integer id) {
        boletoRepository.deleteById(id);
    }

    @Override
    public Optional<BoletoDto> obtenerPorId(Integer id) {
        serviceAsiento.liberar(id);
        return  boletoRepository.findById(id)
                .map(boletoMapper::toDTO);
    }
}
