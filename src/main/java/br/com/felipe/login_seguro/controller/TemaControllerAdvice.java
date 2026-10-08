package br.com.felipe.login_seguro.controller;

import br.com.felipe.login_seguro.config.TemaConfig;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(
        basePackages = "br.com.felipe.login_seguro.controller"
)
public class TemaControllerAdvice {

    private final TemaConfig temaConfig;

    public TemaControllerAdvice(TemaConfig temaConfig) {
        this.temaConfig = temaConfig;
    }

    @ModelAttribute("visual")
    public TemaConfig visual() {
        return temaConfig;
    }
}