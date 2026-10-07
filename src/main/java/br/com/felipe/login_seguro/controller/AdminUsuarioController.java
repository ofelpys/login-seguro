package br.com.felipe.login_seguro.controller;

import br.com.felipe.login_seguro.dto.UsuarioAtualizacaoRequestDTO;
import br.com.felipe.login_seguro.dto.UsuarioResponseDTO;
import br.com.felipe.login_seguro.entity.PerfilEnum;
import br.com.felipe.login_seguro.service.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final IUsuarioService usuarioService;

    public AdminUsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());

        return "admin/usuarios";
    }

    @GetMapping("/{id}/editar")
    public String exibirEdicao(
            @PathVariable String id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UsuarioResponseDTO usuario = usuarioService.buscarPorId(id);

            model.addAttribute(
                    "usuario",
                    new UsuarioAtualizacaoRequestDTO(
                            usuario.nome(),
                            usuario.email(),
                            usuario.perfil()
                    )
            );

            prepararFormulario(model, id);

            return "admin/editar-usuario";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "erro", exception.getMessage()
            );

            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/{id}/editar")
    public String atualizarUsuario(
            @PathVariable String id,
            @Valid @ModelAttribute("usuario")
            UsuarioAtualizacaoRequestDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        prepararFormulario(model, id);

        if (bindingResult.hasErrors()) {
            return "admin/editar-usuario";
        }

        try {
            usuarioService.atualizar(id, dto, principal.getName());
        } catch (IllegalArgumentException exception) {
            bindingResult.reject(
                    "erro.atualizacao", exception.getMessage()
            );

            return "admin/editar-usuario";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Usuário atualizado com sucesso!"
        );

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/excluir")
    public String excluirUsuario(
            @PathVariable String id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            usuarioService.excluir(id, principal.getName());

            redirectAttributes.addFlashAttribute(
                    "sucesso", "Usuário excluído com sucesso!"
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "erro", exception.getMessage()
            );
        }

        return "redirect:/admin/usuarios";
    }

    private void prepararFormulario(Model model, String id) {
        model.addAttribute("id", id);
        model.addAttribute("perfis", PerfilEnum.values());
    }
}