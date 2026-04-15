package com.nexttrip.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JWT stateless + rôles.
 * <p>
 * Évolutions possibles :
 * <ul>
 * <li>{@code /api/admin/**} → ADMIN uniquement (déjà appliqué)</li>
 * <li>{@code /api/wishlist/**} → utilisateur authentifié (déjà appliqué)</li>
 * <li>{@code GET /api/destinations/**} → public ; écriture catalogue → ADMIN</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/weather/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/flights/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/destinations/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/recommendations/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/destinations").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/destinations/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/destinations/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/destinations/*/reviews").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/api/destinations/*/reviews/*").authenticated()
						.requestMatchers("/api/wishlist/**").authenticated()
						.requestMatchers("/api/bookings/**").authenticated()
						.requestMatchers("/api/user/**").authenticated()
						.requestMatchers(HttpMethod.POST, "/api/search-history").authenticated()
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/ai/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/compare").permitAll()
						.requestMatchers("/api/notifications/**").authenticated()
						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
