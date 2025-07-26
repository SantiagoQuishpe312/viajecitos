package ec.edu.monster.controlador;

import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.modelo.LoginRequest;
import ec.edu.monster.servicio.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("")
public class WebAuthController {

    private final AuthService authService;

    @Autowired
    public WebAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model, HttpSession session) {
        try {
            Cliente cliente = authService.login(loginRequest);

            model.addAttribute("cliente", cliente);
            session.setAttribute("cliente", cliente);

            if ("admin".equalsIgnoreCase(cliente.getClienteRol())) {
                return "dashboard-admin";
            } else {
                return "usuario-home";
            }

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                model.addAttribute("error", "No autorizado: Credenciales incorrectas");
            } else {
                model.addAttribute("error", "Error inesperado: " + e.getStatusCode());
            }
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Error del servidor, intente más tarde");
            return "login";
        }
    }
}
