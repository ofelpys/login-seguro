package br.com.felipe.login_seguro.controller;

import br.com.felipe.login_seguro.dto.UsuarioRequestDTO;
import br.com.felipe.login_seguro.service.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/inicio";
    }

    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {
        model.addAttribute(
                "usuario",
                new UsuarioRequestDTO("", "", "")
        );

        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid @ModelAttribute("usuario") UsuarioRequestDTO dto,
            BindingResult resultado,
            RedirectAttributes redirectAttributes
    ) {
        if (resultado.hasErrors()) {
            return "cadastro";
        }

        try {
            usuarioService.cadastrar(dto);
        } catch (IllegalArgumentException exception) {
            resultado.reject("cadastro", exception.getMessage());
            return "cadastro";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Cadastro realizado com sucesso!"
        );

        return "redirect:/login";
    }
}