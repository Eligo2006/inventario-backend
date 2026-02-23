package pe.cibertec.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.cibertec.inventario.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	List<Producto> findByEstadoTrue();
	List<Producto> findByEstadoFalse();
}
