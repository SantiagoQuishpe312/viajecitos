package ec.edu.monster.controlador;

import ec.edu.monster.dto.ClienteDto;
import ec.edu.monster.dto.LoginRequest;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.servicio.IServiceAuth;
import ec.edu.monster.util.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.AUTH_PATH)
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private IServiceAuth serviceAuth;

    @PostMapping("/login")
    public ResponseEntity<ClienteDto> login(@RequestBody LoginRequest request) {
        ClienteDto cliente = serviceAuth.login(request.getUsuario(), request.getContrasena());

        if (cliente != null) {
            return new ResponseEntity<>(cliente,HttpStatus.OK);
         } else {
            return new ResponseEntity<>(cliente,HttpStatus.UNAUTHORIZED);
        }
    }
}
