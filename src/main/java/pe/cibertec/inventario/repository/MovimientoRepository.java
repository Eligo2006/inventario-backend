package pe.cibertec.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.inventario.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}