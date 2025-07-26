package ec.edu.monster.controlador;

import ec.edu.monster.modelo.FacturaCompleta;
import ec.edu.monster.servicio.FacturaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class FacturaController {

    @Autowired
    FacturaWebService facturaWebService;

    @GetMapping("/factura/{id}")
    public String mostrarFactura(@PathVariable Integer id, Model model) {
        Optional<FacturaCompleta> optFactura = facturaWebService.obtenerFacturaCompletaPorId(id);
        if (optFactura.isPresent()) {
            FacturaCompleta facturaCompleta = optFactura.get();
            model.addAttribute("factura", facturaCompleta.getFactura());
            model.addAttribute("cliente", facturaCompleta.getCliente());
            model.addAttribute("boleto", facturaCompleta.getBoleto());
            // Pasa directamente LocalDateTime a la vista
            LocalDateTime fechaFactura = facturaCompleta.getFactura().getFecha();
            model.addAttribute("fechaFactura", fechaFactura);
            model.addAttribute("credito",facturaCompleta.getCredito());
            return "factura"; // nombre de la plantilla Thymeleaf factura.html
        } else {
            return "error/404"; // o como manejes los errores
        }
    }
}
