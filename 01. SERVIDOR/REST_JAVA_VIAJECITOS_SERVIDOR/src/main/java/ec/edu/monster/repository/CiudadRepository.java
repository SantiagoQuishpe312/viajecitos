package ec.edu.monster.repository;

import ec.edu.monster.modelo.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadRepository extends JpaRepository<Ciudad,String> {
}
