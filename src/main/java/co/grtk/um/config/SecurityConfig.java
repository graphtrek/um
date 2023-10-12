package co.grtk.um.config;


import co.grtk.um.service.UmUserDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
        private final RsaKeyProperties jwtConfigProperties;
        private final UmUserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .csrf(csrf -> csrf.ignoringRequestMatchers(
                                AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                                AntPathRequestMatcher.antMatcher("/h2-console/**"),
                                AntPathRequestMatcher.antMatcher("/api/**"))
                        )
                        .authorizeHttpRequests( auth -> auth
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/favicon.ico")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/assets/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/js/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/login")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/token")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/resendRegistrationToken")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/validateRegistrationToken")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/refreshToken")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/register")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/registerUser")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/forgot-password")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/forgotPassword")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/change-password")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/resetPassword")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/resendPasswordResetToken")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/validatePasswordResetToken")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/profile")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/users")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/list-beans")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/jwt-tokens")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/refresh-tokens")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/registration-tokens")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/password-reset-tokens")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/activity-logs")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/activity-log-report")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/logUserActivity")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
                                .anyRequest().authenticated()
                        )
                        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                        .userDetailsService(userDetailsService)
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .oauth2ResourceServer((oauth2 -> oauth2.jwt(Customizer.withDefaults())))
                        .exceptionHandling(ex -> ex
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                        )
                        .httpBasic(Customizer.withDefaults())
                        .build();
        }

        @Bean
        JwtDecoder jwtDecoder() {
                return NimbusJwtDecoder.withPublicKey(jwtConfigProperties.publicKey()).build();
        }

        @Bean
        JwtEncoder jwtEncoder() {
                JWK jwk = new RSAKey.Builder(jwtConfigProperties.publicKey()).privateKey(jwtConfigProperties.privateKey()).build();
                JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
                return new NimbusJwtEncoder(jwks);
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
        @Bean
        public AuthenticationEventPublisher authenticationEventPublisher
                (ApplicationEventPublisher applicationEventPublisher) {
                return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
        }
}
