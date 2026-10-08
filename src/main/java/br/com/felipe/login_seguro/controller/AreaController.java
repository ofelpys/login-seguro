package br.com.felipe.login_seguro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AreaController {

    @GetMapping("/gerenciamento")
    public String exibirGerenciamento() {
        return "gerenciamento";
    }

    @GetMapping("/admin")
    public String exibirAdministracao() {
        return "admin";
    }

    @RequestMapping("/acesso-negado")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String exibirAcessoNegado() {
        return "acesso-negado";
    }
}