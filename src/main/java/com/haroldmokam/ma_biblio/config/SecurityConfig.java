package com.haroldmokam.ma_biblio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuration du filtre de sécurité HTTP
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Pages publiques
                .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/error/**").permitAll()
                // Pages admin
                .requestMatchers("/admin/**").hasRole("BIBLIOTHÉCAIRE")
                // Pages utilisateur
                .requestMatchers("/user/**").hasRole("EMPLOYÉ")
                // API REST (pour l'instant, accessible à tous)
                .requestMatchers("/api/**").permitAll()
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable); // Désactivé pour le développement

        return http.build();
    }

    /**
     * Encodeur de mot de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
