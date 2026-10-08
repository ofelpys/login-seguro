package br.com.felipe.login_seguro.mapper;

import br.com.felipe.login_seguro.dto.UsuarioRequestDTO;
import br.com.felipe.login_seguro.dto.UsuarioResponseDTO;
import br.com.felipe.login_seguro.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioEntity toEntity(UsuarioRequestDTO dto) {
        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());

        return usuario;
    }

    public UsuarioResponseDTO toDTO(UsuarioEntity usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil()
        );
    }
}