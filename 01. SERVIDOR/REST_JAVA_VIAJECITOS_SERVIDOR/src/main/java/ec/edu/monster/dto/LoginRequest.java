package ec.edu.monster.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String usuario;
    private String contrasena;

}
