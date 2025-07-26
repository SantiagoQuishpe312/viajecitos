package ec.edu.monster.controlador;

import ec.edu.monster.modelo.BoletoInfo;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.Tarjeta;
import ec.edu.monster.servicio.BoletoWebService;
import ec.edu.monster.servicio.TarjetaWebService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/tarjetas")
public class TarjetaController {

    private final TarjetaWebService tarjetaWebService;
    private final BoletoWebService boletoWebService;

    public TarjetaController(TarjetaWebService tarjetaWebService, BoletoWebService boletoWebService) {
        this.tarjetaWebService = tarjetaWebService;
        this.boletoWebService = boletoWebService;
    }

    @GetMapping
    public String listarTarjetas(HttpSession session, Model model) {
        var cliente = session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }
        Integer idCliente = (cliente instanceof Cliente)
                ? ((Cliente) cliente).getClienteId()
                : (Integer) session.getAttribute("clienteId");

        List<Tarjeta> tarjetas = tarjetaWebService.obtenerTarjetas(idCliente);
        model.addAttribute("tarjetas", tarjetas);
        model.addAttribute("nuevaTarjeta", new Tarjeta());
        return "mis-boletos";  // O la vista que tenga el modal y tarjetas
    }

    @PostMapping("/crear")
    public String crearTarjeta(@ModelAttribute("nuevaTarjeta") Tarjeta tarjeta,
                               HttpSession session,
                               Model model) {
        var cliente = session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }

        tarjeta.setTarjetaFechaRegistro(new Date());

        if (tarjeta.getTarjetaFechaCad() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tarjeta.getTarjetaFechaCad());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            tarjeta.setTarjetaFechaCad(calendar.getTime());
        }

        List<String> prov = Arrays.asList("VISA", "MASTERCARD", "AMEX");
        tarjeta.setTarjetaProv(prov.get(new Random().nextInt(prov.size())));

        List<String> tipos = Arrays.asList("CRÉDITO", "DÉBITO");
        tarjeta.setTarjetaTipo(tipos.get(new Random().nextInt(tipos.size())));

        BigDecimal saldoAleatorio = BigDecimal.valueOf(50 + new Random().nextDouble() * 1950)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        tarjeta.setTarjetaSaldo(saldoAleatorio);

        Integer idCliente = (cliente instanceof Cliente)
                ? ((Cliente) cliente).getClienteId()
                : (Integer) session.getAttribute("clienteId");

        tarjetaWebService.crearTarjeta(tarjeta, idCliente);

        // Usa el service para obtener los boletos pendientes en lugar de llamar directamente con RestTemplate
        List<BoletoInfo> boletos = boletoWebService
                .obtenerBoletosNoPagados(idCliente)
                .orElse(Collections.emptyList());
        BigDecimal totalPrecio = BigDecimal.ZERO;
        if (!boletos.isEmpty()) {
            totalPrecio = boletos.stream()
                    .map(b -> b.getVuelo().getVueloValor()
                            .multiply(new BigDecimal(b.getBoleto().getBoletoCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        model.addAttribute("boletos", boletos);
        model.addAttribute("totalPrecio", totalPrecio);
        model.addAttribute("tarjetas", tarjetaWebService.obtenerTarjetas(idCliente));
        model.addAttribute("nuevaTarjeta", new Tarjeta());

        return "carrito-boletos";
    }

}
