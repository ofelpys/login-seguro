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

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@Validated
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            IUsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
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
}