package br.com.felipe.login_seguro.dto;

import br.com.felipe.login_seguro.entity.PerfilEnum;

public record UsuarioResponseDTO(
        String id,
        String nome,
        String email,
        PerfilEnum perfil
) {
}