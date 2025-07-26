package ec.edu.monster.repository;

import ec.edu.monster.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Cliente, Integer> {

    @Query("SELECT c FROM Cliente c WHERE c.clienteUsuario = :usuario AND c.clienteContrasena = :contrasena")
    Optional<Cliente> login(@Param("usuario") String usuario, @Param("contrasena") String contrasena);
}
