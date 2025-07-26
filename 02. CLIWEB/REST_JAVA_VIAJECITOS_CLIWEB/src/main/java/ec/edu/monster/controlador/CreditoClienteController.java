package ec.edu.monster.controlador;

import ec.edu.monster.modelo.CreditoInfo;
import ec.edu.monster.servicio.CreditoWebService;
import ec.edu.monster.modelo.Cliente;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class CreditoClienteController {

    @Autowired
    private CreditoWebService creditoWebService;

    @GetMapping("/credito/evaluar")
    public String evaluarCredito(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }

        Optional<CreditoInfo> creditoOpt = creditoWebService.evaluarCredito(cliente.getClienteId());

        if (creditoOpt.isPresent()) {
            CreditoInfo credito = creditoOpt.get();
            model.addAttribute("sujetoCredito", credito.isSujetoCredito());
            model.addAttribute("creditoDisponible", credito.getCreditoDisponible());
        } else {
            model.addAttribute("error", "No se pudo evaluar el crédito.");
        }

        return "evaluacion-credito"; // Vista Thymeleaf que debes crear
    }
    @GetMapping("/credito/evaluar/{clienteId}")
    @ResponseBody
    public ResponseEntity<CreditoInfo> evaluarCredito(@PathVariable Integer clienteId,HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("cliente");

        Optional<CreditoInfo> resultado = creditoWebService.evaluarCredito(cliente.getClienteId());
        return resultado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
