package br.com.felipe.login_seguro.repository;

import br.com.felipe.login_seguro.entity.UsuarioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUsuarioRepository
        extends MongoRepository<UsuarioEntity, String> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}