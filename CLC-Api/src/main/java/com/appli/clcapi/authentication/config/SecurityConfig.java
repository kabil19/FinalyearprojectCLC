package com.appli.clcapi.authentication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String[] WHITE_LIST_APIS = {"/api/authentication/**","/v3/api-docs/**", "/swagger-resources/**",
            "/swagger-ui/**", "/webjars/**"};
    private final JwtAuthFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                        .cors(withDefaults());
        http
                .authorizeHttpRequests(
                        (authReq)->authReq
//                                .requestMatchers("/api/user/**","/api/customer/**","/api/vendor/**","/api/stock/**","/api/category/**","/api/tempInvoice/**")
                                .requestMatchers(WHITE_LIST_APIS)
                                .permitAll()
                                .anyRequest()
                                .authenticated());
        http
                .sessionManagement(
                        (session)->session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authenticationProvider(authenticationProvider);
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http
                .build();

    }

}
