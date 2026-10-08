package br.com.felipe.login_seguro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.session.SessionRegistry;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SessionRegistry sessionRegistry
    ) throws Exception {

        http
                .sessionManagement(sessao -> sessao
                        .maximumSessions(-1)
                        .sessionRegistry(sessionRegistry)
                        .expiredUrl("/login?expirada")
                )
                .authorizeHttpRequests(acesso -> acesso
                        .requestMatchers(
                                "/",
                                "/login",
                                "/cadastro",
                                "/css/**",
                                "/error"
                        ).permitAll()
                        .requestMatchers("/admin", "/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers(
                                "/gerenciamento",
                                "/gerenciamento/**"
                        ).hasAnyRole("MANAGER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/inicio", true)
                        .failureUrl("/login?erro")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("SESSION", "JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(erro -> erro
                        .accessDeniedPage("/acesso-negado")
                );

        return http.build();
    }
}