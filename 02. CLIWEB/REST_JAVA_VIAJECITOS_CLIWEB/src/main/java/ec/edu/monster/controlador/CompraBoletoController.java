package ec.edu.monster.controlador;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.monster.modelo.*;
import ec.edu.monster.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static ec.edu.monster.util.ApiConstants.API_BASE_PATH;

@Controller
public class CompraBoletoController {
    private static final String API_URL = API_BASE_PATH+"/boleto";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private AmortizacionWebService amortizacionWebService;
    @Autowired
    private TarjetaWebService tarjetaWebService;
    @Autowired
    private BoletoWebService boletoWebService;
    @Autowired
    private AsientoWebService asientoWebService;
    // Guarda los asientos seleccionados en sesión o base datos (simulado aquí)
    @PostMapping("/seleccionar-asientos")
    public String seleccionarAsientos(@ModelAttribute AsientosSeleccionados dto, HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) return "redirect:/login";

        // Crear el boleto
        Boleto boleto = new Boleto();
        boleto.setClienteId(cliente.getClienteId());
        boleto.setVueloId(dto.getVueloId());
        boleto.setFacturaId(null);
        boleto.setBoletoCantidad(dto.getAsientos().size());
        boleto.setBoletoEstado("RESERVADO");
        boleto.setBoletoFechaCompra(java.time.LocalDateTime.now());

        Optional<Boleto> boletoCreadoOpt = Optional.ofNullable(boletoWebService.comprarBoleto(boleto));
        if (boletoCreadoOpt.isEmpty()) {
            model.addAttribute("error", "Error al crear el boleto.");
            return "redirect:/vuelos";
        }

        Integer boletoId = boletoCreadoOpt.get().getBoletoId();

        // Parsear el JSON recibido con relaciones asiento -> cliente
        Map<Integer, Integer> relaciones = new HashMap<>();
        try {
            relaciones = new ObjectMapper().readValue(dto.getClientePorAsientoJson(), new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Integer asientoId : dto.getAsientos()) {
            Integer clienteAsignado = relaciones.getOrDefault(asientoId, cliente.getClienteId());
            asientoWebService.reservar(asientoId, boletoId, clienteAsignado);
        }

        session.setAttribute("asientosSeleccionados", dto.getAsientos());
        session.setAttribute("vueloIdSeleccionado", dto.getVueloId());

        return "seguir".equalsIgnoreCase(dto.getAccion())
                ? "redirect:/vuelos"
                : "redirect:/carrito/" + cliente.getClienteId();
    }


    @GetMapping("/carrito/{idCliente}")
    public String verCarritoBoletos(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }
        Integer idCliente = cliente.getClienteId();
        model.addAttribute("cliente", cliente);


        List<BoletoInfo> boletos = boletoWebService
                .obtenerBoletosNoPagados(cliente.getClienteId())
                .orElse(Collections.emptyList());
        BigDecimal totalPrecio = BigDecimal.ZERO;

            // Calcular total solo si hay boletos
            if (boletos != null && !boletos.isEmpty()) {
                totalPrecio = boletos.stream()
                        .map(b -> b.getVuelo().getVueloValor().multiply(
                                new BigDecimal(b.getBoleto().getBoletoCantidad())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }

        model.addAttribute("boletos", boletos);
        model.addAttribute("totalPrecio", totalPrecio);
        model.addAttribute("totalMasIva",totalPrecio.multiply(BigDecimal.valueOf(1.15)));
        model.addAttribute("tarjetas", tarjetaWebService.obtenerTarjetas(idCliente));
        model.addAttribute("nuevaTarjeta", new Tarjeta());

        return "carrito-boletos";
    }

    @GetMapping("/boletos/eliminar/{id}")
    public String eliminarBoleto(@PathVariable Integer id, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }

        boletoWebService.eliminarBoletoPorId(id);

        // Redirigir nuevamente al carrito del cliente logueado
        return "redirect:/carrito/" + cliente.getClienteId();
    }
    @Autowired
    private FacturaWebService facturaWebService;
    @PostMapping("/carrito/pagar")
    public String procesarPago(@RequestParam("metodoPago") String metodoPago,
                               @RequestParam(value = "numCuotas", required = false) Integer numCuotas,
                               @RequestParam(value = "accion", required = false) String accion,
                               @RequestParam(value = "tipoAmortizacion", required = false) String tipoAmortizacion,
                               HttpSession session,
                               Model model) {

        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }
        model.addAttribute("clienteId", cliente.getClienteId());

        // Obtener boletos del carrito
        List<BoletoInfo> boletos = boletoWebService
                .obtenerBoletosNoPagados(cliente.getClienteId())
                .orElse(Collections.emptyList());
        BigDecimal totalPrecio = BigDecimal.ZERO;

        if (boletos != null && !boletos.isEmpty()) {
            totalPrecio = boletos.stream()
                    .map(b -> b.getVuelo().getVueloValor().multiply(
                            new BigDecimal(b.getBoleto().getBoletoCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        model.addAttribute("boletos", boletos);
        model.addAttribute("totalPrecio", totalPrecio);
        model.addAttribute("nuevaTarjeta", new Tarjeta()); // Asegúrate que Tarjeta es tu clase modelo


        // Crear la factura
        try {
            Factura factura = new Factura();
            factura.setClienteId(cliente.getClienteId());
            factura.setEstado("EMITIDA");
            if(metodoPago.equals("paypal")){
                factura.setMetodoPago("EFECTIVO");
            }else {
                factura.setMetodoPago(metodoPago.toUpperCase());
            }

            factura.setAmortizacion(tipoAmortizacion);
            factura.setCuotas(numCuotas);
            Factura facturaCreada = facturaWebService.facturar(factura);

            if (facturaCreada == null || facturaCreada.getFacturaId() == null) {
                model.addAttribute("error", "Error al crear la factura.");
                return "carrito-boletos";
            }

            Integer facturaId = facturaCreada.getFacturaId();

            // Redirigir según método de pago
            switch (metodoPago.toLowerCase()) {
                case "tarjeta":
                case "credito":
                case "paypal":
                    return "redirect:/factura/" + facturaId;
                default:
                    model.addAttribute("error", "Método de pago no válido");
                    return "carrito-boletos";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la factura: " + e.getMessage());
            return "carrito-boletos";
        }
    }

    // EN CompraBoletoController.java
    @GetMapping("/carrito/amortizacion")
    @ResponseBody
    public ResponseEntity<?> obtenerAmortizacionAjax(
            @RequestParam Integer clienteId,
            @RequestParam Integer numCuotas,
            @RequestParam String tipoAmortizacion,
            HttpSession session,
            Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        List<BoletoInfo> boletos = boletoWebService
                .obtenerBoletosNoPagados(cliente.getClienteId())
                .orElse(Collections.emptyList());
        BigDecimal totalPrecio = BigDecimal.ZERO;
        if (boletos != null && !boletos.isEmpty()) {
            totalPrecio = boletos.stream()
                    .map(b -> b.getVuelo().getVueloValor().multiply(
                            new BigDecimal(b.getBoleto().getBoletoCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }


        totalPrecio = totalPrecio.multiply(BigDecimal.valueOf(1.15)); // con IVA

        // Aquí usas correctamente el Optional
        var cuotasOpt = amortizacionWebService.obtenerAmortizacion(totalPrecio, numCuotas, tipoAmortizacion);
        cuotasOpt.ifPresent(cuotas -> model.addAttribute("cuotasAmortizacion", cuotas));

        return cuotasOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/amortizacion")
    @ResponseBody
    public ResponseEntity<?> obtenerAmortizacionAjax2(
            @RequestParam Integer numCuotas,
            @RequestParam String tipoAmortizacion,
            @RequestParam BigDecimal total,
            HttpSession session,
            Model model) {
        // Aquí usas correctamente el Optional
        var cuotasOpt = amortizacionWebService.obtenerAmortizacion(total, numCuotas, tipoAmortizacion);
        cuotasOpt.ifPresent(cuotas -> model.addAttribute("cuotasAmortizacion", cuotas));

        return cuotasOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
