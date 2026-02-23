package com.unsch.carnet_digital.security;

import com.unsch.carnet_digital.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;


@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(JwtAuthFilter jwtFilter,
                CustomUserDetailsService customUserDetailsService,
                ClientRegistrationRepository clientRegistrationRepository
        ) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/oauth2/**",
                                "/loginSuccess",
                                "/error",
                                "/auth/**",
                                "/auth/local/**"
                        ).permitAll()

                        // PROXY ZK → cualquier usuario logueado
                        .requestMatchers("/api/transactions/**").authenticated()

                        // solo vigilantes pueden verificar QR
                        .requestMatchers("/verificacion/**").hasRole("VIGILANTE")

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
                        .defaultSuccessUrl("/loginSuccess", true)
                )

                // Aquí conectamos nuestro UserDetailsService
                .userDetailsService(customUserDetailsService)

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
