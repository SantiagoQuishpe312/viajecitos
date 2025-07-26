package ec.edu.monster.modelo;
import lombok.Data;

@Data
public class LoginRequest {
    private String usuario;
    private String contrasena;
}
