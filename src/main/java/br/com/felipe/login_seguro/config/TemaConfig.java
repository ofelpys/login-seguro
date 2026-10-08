package br.com.felipe.login_seguro.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "app.visual")
public class TemaConfig {

    @NotBlank
    private String nome = "Login Seguro";

    @NotBlank
    @Pattern(
            regexp = "[a-z0-9-]+",
            message = "Use letras minúsculas, números ou hífen no tema."
    )
    private String tema = "padrao";

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}