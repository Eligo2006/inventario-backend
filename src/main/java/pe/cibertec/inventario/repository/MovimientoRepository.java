package pe.cibertec.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.inventario.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
	List<Movimiento> findAllByOrderByFechaDesc();
    List<Movimiento> findByProducto_IdOrderByFechaDesc(Long productoId);
}