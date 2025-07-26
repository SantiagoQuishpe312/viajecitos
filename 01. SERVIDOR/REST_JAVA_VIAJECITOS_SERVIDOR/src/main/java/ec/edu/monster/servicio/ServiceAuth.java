package ec.edu.monster.servicio;

import ec.edu.monster.dto.ClienteDto;
import ec.edu.monster.mapper.ClienteMapper;
import ec.edu.monster.modelo.Cliente;
import ec.edu.monster.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAuth implements IServiceAuth {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private ClienteMapper clienteMapper;

    @Override
    public ClienteDto login(String usuario, String contrasena) {

        return clienteMapper.toDTO(authRepository.login(usuario, contrasena).orElse(new Cliente()));
    }
}
