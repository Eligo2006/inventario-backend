package pe.cibertec.inventario.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import pe.cibertec.inventario.entity.Usuario;
import pe.cibertec.inventario.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.builder()
                .username(u.getUsername())
                .password(u.getPassword()) // password ya está cifrada en DB
                .authorities(Collections.emptyList())
                .disabled(!u.getEstado())
                .build();
    }
}