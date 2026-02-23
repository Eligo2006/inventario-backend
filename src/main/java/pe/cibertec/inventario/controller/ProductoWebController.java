package pe.cibertec.inventario.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import pe.cibertec.inventario.entity.Producto;
import pe.cibertec.inventario.repository.ProductoRepository;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoWebController {

    private final ProductoRepository repo;

    @GetMapping
    public String listar(@RequestParam(name = "estado", required = false, defaultValue = "activos") String estado,
                         Model model) {

        if ("inactivos".equalsIgnoreCase(estado)) {
            model.addAttribute("productos", repo.findByEstadoFalse());
        } else if ("todos".equalsIgnoreCase(estado)) {
            model.addAttribute("productos", repo.findAll());
        } else {
            model.addAttribute("productos", repo.findByEstadoTrue());
        }

        model.addAttribute("estado", estado.toLowerCase());
        return "productos/lista";
    }
    
    @GetMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id,
                         @RequestParam(name = "estado", required = false, defaultValue = "activos") String estado) {

        Producto p = repo.findById(id).orElseThrow();
        p.setEstado(p.getEstado() == null ? true : !p.getEstado());
        repo.save(p);

        return "redirect:/productos?estado=" + estado + "&toggled";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
            return "productos/formulario";
        }

        repo.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Producto producto = repo.findById(id).orElseThrow();
        model.addAttribute("producto", producto);
        return "productos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        Producto p = repo.findById(id).orElseThrow();
        p.setEstado(false);
        repo.save(p);
        return "redirect:/productos?deleted";
    }
}