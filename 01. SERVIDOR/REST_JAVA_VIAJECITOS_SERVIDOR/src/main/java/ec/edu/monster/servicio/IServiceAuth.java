package ec.edu.monster.servicio;

import ec.edu.monster.dto.ClienteDto;
import ec.edu.monster.modelo.Cliente;

public interface IServiceAuth {
    ClienteDto login(String usuario, String contrasena);
}
