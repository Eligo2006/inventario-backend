package pe.cibertec.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.cibertec.inventario.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByUsername(String username);
}
