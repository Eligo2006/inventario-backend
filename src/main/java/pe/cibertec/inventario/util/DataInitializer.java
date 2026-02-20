package pe.cibertec.inventario.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pe.cibertec.inventario.entity.Usuario;
import pe.cibertec.inventario.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, BCryptPasswordEncoder encoder){
        return args -> {
            if(usuarioRepository.findByUsername("admin").isEmpty()){
                Usuario u = Usuario.builder()
                    .username("admin")
                    .password(encoder.encode("admin123")) // contraseña inicial
                    .estado(true)
                    .build();
                usuarioRepository.save(u);
                System.out.println("Usuario admin creado: admin / admin123");
            }
        };
    }
}