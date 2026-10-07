package br.com.felipe.login_seguro.dto;

import br.com.felipe.login_seguro.entity.PerfilEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioAtualizacaoRequestDTO(

        @NotBlank(message = "Informe o nome.")
        @Size(min = 2, max = 100,
                message = "O nome deve ter entre 2 e 100 caracteres.")
        String nome,

        @NotBlank(message = "Informe o e-mail.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 254,
                message = "O e-mail deve ter até 254 caracteres.")
        String email,

        @NotNull(message = "Selecione um perfil.")
        PerfilEnum perfil

) {
}