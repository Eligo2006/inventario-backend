package pe.cibertec.inventario.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import pe.cibertec.inventario.entity.Role;
import pe.cibertec.inventario.entity.Usuario;
import pe.cibertec.inventario.repository.RoleRepository;
import pe.cibertec.inventario.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder encoder){
        return args -> {
			// Roles mínimos
			Role adminRole = roleRepository.findByNombre("ADMIN").orElseGet(() -> {
				Role r = new Role();
				r.setNombre("ADMIN");
				return roleRepository.save(r);
			});
			roleRepository.findByNombre("USER").orElseGet(() -> {
				Role r = new Role();
				r.setNombre("USER");
				return roleRepository.save(r);
			});

            if(usuarioRepository.findByUsername("admin").isEmpty()){
                Usuario u = Usuario.builder()
                    .username("admin")
                    .password(encoder.encode("admin123")) // contraseña inicial
                    .estado(true)
					.rol(adminRole)
                    .build();
                usuarioRepository.save(u);
                System.out.println("Usuario admin creado: admin / admin123");
            }
        };
    }
}