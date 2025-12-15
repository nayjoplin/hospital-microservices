package com.hospital.centrocirurgico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuração de segurança com Keycloak OAuth2
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permite acesso público ao Swagger
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()
                // Endpoints de consulta - apenas USUARIO, MEDICO e ADMIN
                .requestMatchers("/api/cadastro/consulta/**").hasAnyRole("USUARIO", "MEDICO", "ADMIN")
                // Endpoints de exame - apenas USUARIO, MEDICO e ADMIN
                .requestMatchers("/api/cadastro/exame/**").hasAnyRole("USUARIO", "MEDICO", "ADMIN")
                // Endpoints de pesquisa - todos os roles autenticados
                .requestMatchers("/api/pesquisa/**").hasAnyRole("USUARIO", "MEDICO", "ADMIN")
                // Endpoints administrativos - apenas ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Qualquer outra requisição precisa estar autenticada
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    /**
     * Conversor de JWT para Authentication com extração de roles do Keycloak
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return jwtConverter;
    }

    /**
     * Extrai as roles do token JWT do Keycloak
     */
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = 
            new JwtGrantedAuthoritiesConverter();

        return jwt -> {
            // Extrai authorities padrão do scope
            Collection<GrantedAuthority> authorities = 
                defaultGrantedAuthoritiesConverter.convert(jwt);

            // Extrai roles do Keycloak (realm_access.roles)
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            Collection<GrantedAuthority> keycloakAuthorities = List.of();
            
            if (realmAccess != null && realmAccess.get("roles") != null) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) realmAccess.get("roles");
                keycloakAuthorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
            }

            // Combina ambas as authorities
            return Stream.concat(authorities.stream(), keycloakAuthorities.stream())
                .collect(Collectors.toList());
        };
    }
}
