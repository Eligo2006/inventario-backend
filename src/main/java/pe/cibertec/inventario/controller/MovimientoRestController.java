package pe.cibertec.inventario.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.inventario.dto.MovimientoCreateDto;
import pe.cibertec.inventario.entity.Movimiento;
import pe.cibertec.inventario.service.MovimientoService;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoRestController {

    private final MovimientoService movimientoService;

    public MovimientoRestController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public List<Movimiento> listar() {
        return movimientoService.listar();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody MovimientoCreateDto dto, Authentication auth) {
        Movimiento m = movimientoService.registrar(dto, auth.getName());
        return ResponseEntity.ok(m);
    }
}