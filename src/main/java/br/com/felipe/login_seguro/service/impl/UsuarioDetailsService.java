package br.com.felipe.login_seguro.service.impl;

import br.com.felipe.login_seguro.entity.UsuarioEntity;
import br.com.felipe.login_seguro.repository.IUsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioDetailsService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        String emailNormalizado = email.strip().toLowerCase(Locale.ROOT);

        UsuarioEntity usuario = usuarioRepository
                .findByEmail(emailNormalizado)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "E-mail ou senha inválidos."
                ));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .roles(usuario.getPerfil().name())
                .build();
    }
}