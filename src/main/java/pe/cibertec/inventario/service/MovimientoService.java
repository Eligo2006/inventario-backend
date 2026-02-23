package pe.cibertec.inventario.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.cibertec.inventario.dto.MovimientoCreateDto;
import pe.cibertec.inventario.entity.Movimiento;
import pe.cibertec.inventario.entity.Producto;
import pe.cibertec.inventario.entity.Usuario;
import pe.cibertec.inventario.repository.MovimientoRepository;
import pe.cibertec.inventario.repository.ProductoRepository;
import pe.cibertec.inventario.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Service
public class MovimientoService {
	
	@PersistenceContext
	private EntityManager entityManager;

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoService(MovimientoRepository movimientoRepository,
                             ProductoRepository productoRepository,
                             UsuarioRepository usuarioRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Movimiento> listar() {
        return movimientoRepository.findAllByOrderByFechaDesc();
    }

    @Transactional
    public Movimiento registrar(MovimientoCreateDto dto, String username) {

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Movimiento m = new Movimiento();
        m.setTipo(dto.getTipo().trim().toUpperCase());
        m.setCantidad(dto.getCantidad());
        m.setProducto(producto);
        m.setUsuario(usuario);

        Movimiento saved = movimientoRepository.save(m);

        // 🔥 LIMPIA EL CONTEXTO PARA EVITAR CACHE
        entityManager.flush();
        entityManager.clear();

        return saved;
    }
}