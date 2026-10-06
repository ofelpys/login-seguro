package br.com.felipe.login_seguro.service;

import br.com.felipe.login_seguro.dto.UsuarioRequestDTO;
import br.com.felipe.login_seguro.dto.UsuarioResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface IUsuarioService {

    UsuarioResponseDTO cadastrar(
            @NotNull @Valid UsuarioRequestDTO usuarioRequestDTO
    );
}