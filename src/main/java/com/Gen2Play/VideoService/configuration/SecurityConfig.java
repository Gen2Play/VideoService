package com.Gen2Play.VideoService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authz -> authz
                // Cho phép các yêu cầu tới trang chủ
                .requestMatchers("/").permitAll()
                // Cho phép các endpoint Swagger
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                // Cho phép các yêu cầu tới endpoint auth khác
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated() // Mọi request khác yêu cầu xác thực
                )
                .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                })
                );

        return http.build();
    }

    // @Bean
    // public CorsFilter corsFilter() {
    //     return new CorsFilter(corsConfigurationSource());
    // }

    // private UrlBasedCorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration config = new CorsConfiguration();
    //     config.setAllowCredentials(true);
    //     config.addAllowedOriginPattern("*");
    //     config.addAllowedHeader("*");
    //     config.addAllowedMethod("*");

    //     config.addExposedHeader(HttpHeaders.CONTENT_DISPOSITION);

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", config);
    //     return source;
    // }

    // @Bean
    // public SimpleUrlAuthenticationSuccessHandler successHandler() {
    //     return new SimpleUrlAuthenticationSuccessHandler() {
    //         @Override
    //         public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    //                 Authentication authentication) throws java.io.IOException, ServletException {
    //             // Kiểm tra xem authentication có phải là OAuth2AuthenticationToken không
    //             if (authentication instanceof OAuth2AuthenticationToken token) {
    //                 OAuth2User oauthUser = token.getPrincipal();
    //                 // Lấy thuộc tính email từ OAuth2User
    //                 String email = oauthUser.getAttribute("email");

    //                 if (email != null ) {
    //                 // Redirect đến trang login frontend với thông tin email và hình ảnh
    //                 String redirectUrl = String.format(
    //                     frontendDomain + "/?email=%s",
    //                     email
    //                 );
    //                 response.sendRedirect(redirectUrl);
    //                 } else {
    //                     // Chuyển hướng tới trang login với lỗi
    //                     response.sendRedirect(frontendDomain + "/");
    //                 }
    //             } else {
    //                 super.onAuthenticationSuccess(request, response, authentication);
    //             }
    //         }
    //     };
    // }

}
