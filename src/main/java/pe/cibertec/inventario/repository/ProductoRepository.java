package pe.cibertec.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.cibertec.inventario.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
