package pe.cibertec.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import pe.cibertec.inventario.entity.Producto;
import pe.cibertec.inventario.repository.ProductoRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository repo;

    public ProductoController(ProductoRepository repo){
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar(){
        return ResponseEntity.ok(repo.findAll());
    }

    @PostMapping
	public ResponseEntity<Producto> crear(@Valid @RequestBody Producto p){
        Producto guardado = repo.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
	public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto p){
        return repo.findById(id).map(existing -> {
            existing.setNombre(p.getNombre());
            existing.setDescripcion(p.getDescripcion());
            existing.setPrecio(p.getPrecio());
            existing.setStock(p.getStock());
            existing.setEstado(p.getEstado());
            repo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        return repo.findById(id).map(existing -> {
            repo.delete(existing);
            return ResponseEntity.ok().body("Eliminado");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado"));
    }
}