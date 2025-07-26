package ec.edu.monster.controlador;

import ec.edu.monster.modelo.Asiento;
import ec.edu.monster.servicio.AsientoWebService;
import ec.edu.monster.modelo.Cliente;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class AsientoClienteController {

    @Autowired
    private AsientoWebService asientoWebService;

    /*@GetMapping("/asientos/vuelo/{vueloId}")
    public String listarAsientosPorVuelo(@PathVariable Integer vueloId, HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }

        Optional<List<Asiento>> asientosOpt = asientoWebService.obtenerPorVuelo(vueloId);

        if (asientosOpt.isPresent()) {
            List<Asiento> asientos = asientosOpt.get();
            model.addAttribute("asientos", asientos);
            model.addAttribute("vueloId", vueloId);
        } else {
            model.addAttribute("error", "No se encontraron asientos para el vuelo.");
        }

        return "asientos-vuelo"; // Vista Thymeleaf para mostrar los asientos
    }*/
}
