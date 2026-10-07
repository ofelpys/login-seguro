package br.com.felipe.login_seguro.service.impl;

import br.com.felipe.login_seguro.dto.UsuarioRequestDTO;
import br.com.felipe.login_seguro.dto.UsuarioResponseDTO;
import br.com.felipe.login_seguro.entity.PerfilEnum;
import br.com.felipe.login_seguro.entity.UsuarioEntity;
import br.com.felipe.login_seguro.mapper.UsuarioMapper;
import br.com.felipe.login_seguro.repository.IUsuarioRepository;
import br.com.felipe.login_seguro.service.IUsuarioService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Sort;
import java.util.List;
import br.com.felipe.login_seguro.dto.UsuarioAtualizacaoRequestDTO;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@Validated
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final SessionRegistry sessionRegistry;

    public UsuarioService(
            IUsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder,
            SessionRegistry sessionRegistry
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        String email = dto.email().strip().toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        if (dto.senha().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "A senha ultrapassa o limite de 72 bytes do BCrypt."
            );
        }

        UsuarioEntity usuario = usuarioMapper.toEntity(dto);

        usuario.setNome(dto.nome().strip());
        usuario.setEmail(email);
        usuario.setPerfil(PerfilEnum.USER);
        usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));

        UsuarioEntity usuarioSalvo;

        try {
            usuarioSalvo = usuarioRepository.save(usuario);
        } catch (DuplicateKeyException exception) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado.",
                    exception
            );
        }

        return usuarioMapper.toDTO(usuarioSalvo);
    }

    @Override
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll(Sort.by("nome"))
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO buscarPorId(String id) {
        return usuarioMapper.toDTO(buscarEntidade(id));
    }

    @Override
    public UsuarioResponseDTO atualizar(
            String id,
            UsuarioAtualizacaoRequestDTO dto,
            String emailAdministrador
    ) {
        UsuarioEntity usuario = buscarEntidade(id);

        String email = dto.email().strip().toLowerCase(Locale.ROOT);
        String emailAnterior = usuario.getEmail();

        boolean emailAlterado = !emailAnterior.equals(email);
        boolean perfilAlterado = usuario.getPerfil() != dto.perfil();
        boolean propriaConta = emailAnterior.equals(emailAdministrador);

        if (propriaConta && (emailAlterado || perfilAlterado)) {
            throw new IllegalArgumentException(
                    "Para alterar seu próprio e-mail ou perfil, "
                            + "use outra conta administrativa."
            );
        }

        boolean emailEmUso = usuarioRepository.findByEmail(email)
                .map(outro -> !outro.getId().equals(id))
                .orElse(false);

        if (emailEmUso) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado."
            );
        }

        usuario.setNome(dto.nome().strip());
        usuario.setEmail(email);
        usuario.setPerfil(dto.perfil());

        UsuarioEntity usuarioSalvo;

        try {
            usuarioSalvo = usuarioRepository.save(usuario);
        } catch (DuplicateKeyException exception) {
            throw new IllegalArgumentException(
                    "Este e-mail já está cadastrado.",
                    exception
            );
        }

        if (emailAlterado || perfilAlterado) {
            invalidarSessoes(emailAnterior);
        }

        return usuarioMapper.toDTO(usuarioSalvo);
    }

    @Override
    public void excluir(String id, String emailAdministrador) {
        UsuarioEntity usuario = buscarEntidade(id);

        if (usuario.getEmail().equals(emailAdministrador)) {
            throw new IllegalArgumentException(
                    "Você não pode excluir sua própria conta."
            );
        }

        usuarioRepository.delete(usuario);
        invalidarSessoes(usuario.getEmail());
    }

    private UsuarioEntity buscarEntidade(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuário não encontrado."
                        )
                );
    }

    private void invalidarSessoes(String email) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserDetails usuarioLogado
                    && usuarioLogado.getUsername().equals(email)) {

                for (SessionInformation sessao :
                        sessionRegistry.getAllSessions(principal, false)) {
                    sessao.expireNow();
                }
            }
        }
    }
}