package pe.cibertec.inventario.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.inventario.dto.MovimientoCreateDto;
import pe.cibertec.inventario.repository.ProductoRepository;
import pe.cibertec.inventario.service.MovimientoService;

@Controller
@RequestMapping("/movimientos")
public class MovimientoViewController {

    private final MovimientoService movimientoService;
    private final ProductoRepository productoRepository;

    public MovimientoViewController(MovimientoService movimientoService, ProductoRepository productoRepository) {
        this.movimientoService = movimientoService;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoService.listar());
        return "movimientos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("dto", new MovimientoCreateDto());
        model.addAttribute("productos", productoRepository.findAll());
        return "movimientos/nuevo";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("dto") MovimientoCreateDto dto,
                            BindingResult br,
                            Model model,
                            Authentication auth) {

        if (br.hasErrors()) {
            model.addAttribute("productos", productoRepository.findAll());
            return "movimientos/nuevo";
        }

        try {
            movimientoService.registrar(dto, auth.getName());
            return "redirect:/productos?movOk";
        } catch (Exception ex) {

            model.addAttribute("productos", productoRepository.findAll());

            String mensajeUsuario = "Ocurrió un error al registrar el movimiento.";

            if (ex.getCause() != null && ex.getCause().getCause() != null) {
                String mensajeBD = ex.getCause().getCause().getMessage();

                if (mensajeBD.contains("Stock insuficiente")) {
                    mensajeUsuario = "Stock insuficiente para realizar la salida.";
                } else if (mensajeBD.contains("Producto no existe")) {
                    mensajeUsuario = "El producto seleccionado no existe.";
                }
            }

            model.addAttribute("error", mensajeUsuario);
            return "movimientos/nuevo";
        }
    }
}