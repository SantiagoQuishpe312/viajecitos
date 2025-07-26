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
        model.addAttribute("referenteId",cliente.getClienteId());
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

        // Datos para el iframe dinámico
        model.addAttribute("vuelo", vuelo);
        model.addAttribute("lat1", vuelo.getCiudadOrigenLatitud());
        model.addAttribute("lon1", vuelo.getCiudadOrigenLongitud());
        model.addAttribute("aero1", vuelo.getCiudadOrigenAeropuerto());
        model.addAttribute("lat2", vuelo.getCiudadDestinoLatitud());
        model.addAttribute("lon2", vuelo.getCiudadDestinoLongitud());
        model.addAttribute("aero2", vuelo.getCiudadDestinoAeropuerto());

        return "fragments/modalRutaVuelo :: modalRuta";
    }

}
