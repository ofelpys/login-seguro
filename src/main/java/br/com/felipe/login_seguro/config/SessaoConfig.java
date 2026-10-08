package br.com.felipe.login_seguro.config;

import org.mongodb.spring.session.MongoIndexedSessionRepository;
import org.mongodb.spring.session.config.annotation.web.http.EnableMongoHttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableMongoHttpSession(
        collectionName = "sessoes",
        maxInactiveIntervalInSeconds = 1800
)
public class SessaoConfig {

    @Bean
    public SessionRegistry sessionRegistry(
            MongoIndexedSessionRepository sessionRepository
    ) {
        return new SpringSessionBackedSessionRegistry<>(
                sessionRepository
        );
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookie =
                new DefaultCookieSerializer();

        cookie.setCookieName("SESSION");
        cookie.setUseHttpOnlyCookie(true);
        cookie.setSameSite("Lax");

        return cookie;
    }
}