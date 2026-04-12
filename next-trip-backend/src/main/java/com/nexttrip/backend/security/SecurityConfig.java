package com.nexttrip.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Sécurité évolutive pour Next Trip.
 * <p>
 * Évolution prévue (à activer avec JWT / OAuth2) :
 * <ul>
 * <li>{@code /api/admin/**} → rôle ADMIN uniquement</li>
 * <li>{@code /api/wishlist/**} → utilisateur authentifié (USER)</li>
 * <li>{@code GET /api/destinations/**} → public</li>
 * <li>{@code POST|PUT|DELETE /api/destinations/**} → ADMIN pour la gestion catalogue</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		return http.build();
	}
}
