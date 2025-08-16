package com.example.jewelry_management.config;

import com.example.jewelry_management.security.CustomAccessDeniedHandler;
import com.example.jewelry_management.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cấu hình CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Xử lý lỗi xác thực và phân quyền
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                // Cấu hình phân quyền
                .authorizeHttpRequests(auth ->
                        auth
                                // Cho phép yêu cầu OPTIONS không cần xác thực
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                // ảnh tĩnh
                                .requestMatchers("/uploads/**").permitAll()

                                // Phân quyền riêng (ADMIN)
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/account/filter",
                                        "/api/v1/account/role",
                                        "api/v1/account/gender",
                                        "/api/v1/account/status"
                                ).hasAnyRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/account/status/{id}",
                                        "/api/v1/account/role/{id}"
                                ).hasAnyRole("ADMIN")

                                // Phân quyền chung
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/account/{id}"
                                ).hasAnyRole("ADMIN", "STAFF", "USER")

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/order"
                                ).hasAnyRole("ADMIN", "STAFF", "USER")

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/account/{id}"
                                ).hasAnyRole("ADMIN", "STAFF", "USER")

                                // Cho phép tất cả methods cho API v1
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/**",
                                        "/api/v1/product/latest",
                                        "/api/v1/category/tree",
                                        "/api/v1/product/gold-type",
                                        "/api/v1/product/category/{id}",
                                        "/api/v1/product/search",
                                        "/api/v1/order/me/{id}"

                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/**",
                                        "/api/v1/login",
                                        "/api/v1/register",
                                        "/api/v1/forgot-password"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/**",
                                        "/api/v1/reset-password"
                                ).permitAll()

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/**"
                                ).permitAll()

                                // Tất cả các yêu cầu khác cần xác thực
                                .anyRequest().authenticated()
                )
                // Sử dụng Basic Authentication
                .httpBasic(Customizer.withDefaults())
                // Tắt CSRF
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://127.0.0.1:5500");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
