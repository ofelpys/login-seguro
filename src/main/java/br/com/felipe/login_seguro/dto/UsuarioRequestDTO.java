package br.com.felipe.login_seguro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank(message = "Informe seu nome.")
        @Size(min = 2, max = 100,
                message = "O nome deve ter entre 2 e 100 caracteres.")
        String nome,

        @NotBlank(message = "Informe seu e-mail.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 254, message = "O e-mail é muito longo.")
        String email,

        @NotBlank(message = "Informe uma senha.")
        @Size(min = 8, max = 72,
                message = "A senha deve ter entre 8 e 72 caracteres.")
        String senha

) {
}