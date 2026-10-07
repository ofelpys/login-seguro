package br.com.felipe.login_seguro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AutenticacaoController {

    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }

    @GetMapping("/inicio")
    public String exibirInicio(Model model, Principal principal) {
        model.addAttribute("email", principal.getName());

        return "inicio";
    }
}