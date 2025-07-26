package ec.edu.monster.controlador;

import ec.edu.monster.modelo.*;
import ec.edu.monster.servicio.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ClienteWebController {

    @Autowired
    private FacturaWebService facturaWebService;

    @Autowired
    private VueloWebService vueloWebService;

    @Autowired
    private ClienteWebService clienteWebService;

    @Autowired
    private BoletoWebService boletoWebService;

    @Autowired
    private AsientoWebService asientoWebService;

    @GetMapping("/")
    public String panelUsuario(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null || !"user".equalsIgnoreCase(cliente.getClienteRol())) {
            return "redirect:/login";
        }
        model.addAttribute("cliente", cliente);
        return "usuario-home";
    }

    @GetMapping("/nav")
    public String navbar(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null || !"user".equalsIgnoreCase(cliente.getClienteRol())) {
            return "redirect:/login";
        }
        model.addAttribute("cliente", cliente);
        return "fragments/navbar :: navbar";
    }

    @GetMapping("/mis-boletos")
    public String verMisBoletos(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }

        Integer idCliente = cliente.getClienteId();
        model.addAttribute("cliente", cliente);

        boletoWebService.obtenerBoletosPorCliente(idCliente)
                .ifPresentOrElse(
                        boletos -> model.addAttribute("boletos", boletos),
                        () -> model.addAttribute("boletos", List.of())
                );

        return "mis-boletos";
    }

    @GetMapping("/boletos/imprimir/{id}")
    public String imprimirBoleto(@PathVariable Integer id, Model model) {
        boletoWebService.obtenerBoletoPorId(id)
                .ifPresentOrElse(
                        boleto -> model.addAttribute("boleto", boleto),
                        () -> { /* Si no existe, redirige a mis boletos */ }
                );

        if (!model.containsAttribute("boleto")) {
            return "redirect:/mis-boletos";
        }

        return "boleto-imprimir";
    }

    @GetMapping("/vuelos")
    public String verVuelos(Model model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }
        model.addAttribute("cliente", cliente);

        List<Vuelo> vuelos = vueloWebService.obtenerTodos();
        model.addAttribute("vuelos", vuelos);

        return "ver-vuelos";
    }
    @GetMapping("/clientes/referente/{referenteId}")
    @ResponseBody
    public ResponseEntity<List<Cliente>> obtenerReferidos(@PathVariable Integer referenteId) {
        return clienteWebService.obtenerClientesReferidos(referenteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/clientes")
    @ResponseBody
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevo = clienteWebService.guardarCliente(cliente);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping("/asientos/vuelo/{vueloId}")
    public String modalAsientos(@PathVariable Integer vueloId, Model model, HttpSession session) {
        List<Asiento> lista = asientoWebService.obtenerPorVuelo(vueloId)
                .orElse(Collections.emptyList());
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }
        Comparator<String> numericStringComparator = (s1, s2) -> {
            try {
                return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
            } catch (NumberFormatException e) {
                return s1.compareTo(s2);
            }
        };

        Map<String, Map<String, Asiento>> asientoMap = new TreeMap<>(numericStringComparator);
        Set<String> rows = new TreeSet<>(numericStringComparator);
        Set<String> cols = new TreeSet<>();

        for (Asiento a : lista) {
            String filaStr = String.valueOf(a.getFila());
            String col = a.getColumna();
            rows.add(filaStr);
            cols.add(col);

            asientoMap.computeIfAbsent(filaStr, k -> new HashMap<>()).put(col, a);
        }
        model.addAttribute("referenteId",cliente.getClienteId());
        model.addAttribute("vueloId", vueloId);
        model.addAttribute("rows", rows);
        model.addAttribute("cols", cols);
        model.addAttribute("asientoMap", asientoMap);

        return "fragments/modal-asientos :: modalAsientos";
    }

    @GetMapping("/facturas")
    public String verFacturas(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }
        model.addAttribute("cliente", cliente);

        List<FacturaCompleta> facturas = facturaWebService.obtenerFacturasPorCliente(cliente.getClienteId())
                .orElse(Collections.emptyList());

        model.addAttribute("facturas", facturas);
        return "mis-facturas";
    }

    @PostMapping("/comprar-boleto")
    public String comprarBoleto(@RequestParam Integer vueloId, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) return "redirect:/login";

        Boleto boleto = new Boleto();
        boleto.setClienteId(cliente.getClienteId());
        boleto.setVueloId(vueloId);
        boleto.setBoletoCantidad(1);
        boleto.setBoletoEstado("COMPRADO");

        boletoWebService.comprarBoleto(boleto);

        return "redirect:/mis-boletos";
    }
    @GetMapping("/rutaVuelo/{id}")
    public String mostrarRutaVuelo(@PathVariable Integer id, Model model) {
        Vuelo vuelo = vueloWebService.obtenerPorId(id);

        if (vuelo == null) {
            return "error/404";
        }

        String origen = vuelo.getCiudadOrigenCodigo();
        String destino = vuelo.getCiudadDestinoCodigo();

        // Coordenadas simuladas por ciudad
        Map<String, double[]> coords = Map.ofEntries(
                Map.entry("SCL", new double[]{-33.3930, -70.7858}),  // Santiago
                Map.entry("BOG", new double[]{4.7016, -74.1469}),    // Bogotá
                Map.entry("MEX", new double[]{19.4361, -99.0719}),   // Ciudad de México
                Map.entry("UIO", new double[]{-0.1255, -78.3546}),   // Quito
                Map.entry("LIM", new double[]{-12.0219, -77.1143}),  // Lima
                Map.entry("MDE", new double[]{6.1645, -75.4231}),    // Medellín
                Map.entry("CUN", new double[]{21.0365, -86.8771}),   // Cancún
                Map.entry("GYE", new double[]{-2.149, -79.883}),     // Guayaquil
                Map.entry("BUE", new double[]{-34.8222, -58.5358}),  // Buenos Aires (Ezeiza)
                Map.entry("CDG", new double[]{49.0097, 2.5479}),     // París (Charles de Gaulle)
                Map.entry("ROM", new double[]{41.8003, 12.2389}),    // Roma (Fiumicino)
                Map.entry("AMS", new double[]{52.3105, 4.7683}),     // Ámsterdam (Schiphol)
                Map.entry("BER", new double[]{52.3667, 13.5033}),    // Berlín (Brandenburgo)
                Map.entry("NYC", new double[]{40.6413, -73.7781}),   // Nueva York (JFK)
                Map.entry("TOR", new double[]{43.6777, -79.6248})    // Toronto (Pearson)
        );

        model.addAttribute("origen", origen);
        model.addAttribute("destino", destino);
        model.addAttribute("latOrigen", coords.get(origen)[0]);
        model.addAttribute("lonOrigen", coords.get(origen)[1]);
        model.addAttribute("latDestino", coords.get(destino)[0]);
        model.addAttribute("lonDestino", coords.get(destino)[1]);

        return "fragments/modalRutaVuelo :: modalRuta"; // Fragmento Thymeleaf
    }

}
