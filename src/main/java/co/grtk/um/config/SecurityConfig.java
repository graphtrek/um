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
        private static final String[] WHITE_LIST_URLS = {
                "/",
                "/totp",
                "/favicon.ico",
                "/login",
                "/register",
                "/forgot-password",
                "/change-password",
                "/users",
                "/assets/**",
                "/js/**",
                "/token",
                "/api/resendToken",
                "/api/registerUserForm",
                "/api/registerUser",
                "/api/validateToken",
                "/api/forgotPassword",
                "/api/resendPasswordResetToken",
                "/api/validatePasswordResetToken",
                "/api/resetPassword"
        };

        private final RsaKeyProperties jwtConfigProperties;
        private final UmUserDetailsService userDetailsService;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .csrf(csrf -> csrf.ignoringRequestMatchers(
                                AntPathRequestMatcher.antMatcher("/h2-console/**"),
                                AntPathRequestMatcher.antMatcher("/token"),
                                AntPathRequestMatcher.antMatcher("/api/**"))
                        )
                        .authorizeHttpRequests( auth -> auth
                                .requestMatchers(WHITE_LIST_URLS).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
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
