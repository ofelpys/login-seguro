package br.com.felipe.login_seguro.service;

import br.com.felipe.login_seguro.dto.UsuarioRequestDTO;
import br.com.felipe.login_seguro.dto.UsuarioResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import br.com.felipe.login_seguro.dto.UsuarioAtualizacaoRequestDTO;

import java.util.List;

public interface IUsuarioService {

    UsuarioResponseDTO cadastrar(
            @NotNull @Valid UsuarioRequestDTO usuarioRequestDTO
    );

    List<UsuarioResponseDTO> listar();

    UsuarioResponseDTO buscarPorId(String id);

    UsuarioResponseDTO atualizar(
            String id,
            @NotNull @Valid UsuarioAtualizacaoRequestDTO dto,
            String emailAdministrador
    );
    void excluir(String id, String emailAdministrador);
}

