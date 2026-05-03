package com.unsch.carnet_digital.config;

import com.unsch.carnet_digital.security.jwt.JwtAuthFilter;
import com.unsch.carnet_digital.security.oauth.CustomAuthorizationRequestResolver;
import com.unsch.carnet_digital.security.oauth.OAuth2SuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthFilter jwtFilter;
        private final OAuth2SuccessHandler successHandler;
        private final ClientRegistrationRepository clientRegistrationRepository;

        public SecurityConfig(JwtAuthFilter jwtFilter,
                        OAuth2SuccessHandler successHandler,
                        ClientRegistrationRepository clientRegistrationRepository) {

        this.jwtFilter = jwtFilter;
        this.successHandler = successHandler;
        this.clientRegistrationRepository = clientRegistrationRepository;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline' https://accounts.google.com; connect-src 'self' https://accounts.google.com")
                        )
                        .frameOptions(frame -> frame.deny())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                        )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth2/**", "/login/**", "/error").permitAll()

                        .requestMatchers("/transactions/**").authenticated()

                        .requestMatchers("/verificacion/**").hasAnyRole("VIGILANTE", "ADMIN_SISTEMA")

                        .requestMatchers("/usuarios/ping").hasAnyRole("VIGILANTE", "ADMIN_SISTEMA")
                        
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestResolver(
                                        new CustomAuthorizationRequestResolver(
                                                clientRegistrationRepository,
                                                "/oauth2/authorization"
                                        )
                                )
                        )
                        .successHandler(successHandler)
                )

                //Manejo de sesiones Híbridas (JWT + Sesiones tradicionales para OAuth2)
                .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // 🔥 Manejo de errores (importante)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> res.sendError(401))
                        .accessDeniedHandler((req, res, e) -> res.sendError(403))
                )

                //Filtro que intercepta antes del security configuration para validar el JWT
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
        }
}