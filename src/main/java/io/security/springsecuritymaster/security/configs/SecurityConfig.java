package io.security.springsecuritymaster.security.configs;

import io.security.springsecuritymaster.security.details.FormAuthenticationDetailsSource;
import io.security.springsecuritymaster.security.handler.FormAccessDeniedHandler;
import io.security.springsecuritymaster.security.handler.FormAuthenticationFailureHandler;
import io.security.springsecuritymaster.security.handler.FormAuthenticationSuccessHandler;
import io.security.springsecuritymaster.security.provider.FormAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final FormAuthenticationProvider formAuthenticationProvider;
  private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;
  private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
  private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
            .requestMatchers("/", "/signup", "/login*").permitAll()
            .requestMatchers("/user").hasAuthority("ROLE_USER")
            .requestMatchers("/manager").hasAuthority("ROLE_MANAGER")
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .authenticationDetailsSource(formAuthenticationDetailsSource)
            .successHandler(formAuthenticationSuccessHandler)
            .failureHandler(formAuthenticationFailureHandler)
            .permitAll()
        )
        .authenticationProvider(formAuthenticationProvider)
        .exceptionHandling(exception -> exception
            .accessDeniedHandler(new FormAccessDeniedHandler("/denied"))
        )
    ;

    return http.build();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
            .anyRequest().permitAll()
        )
        .authenticationProvider(formAuthenticationProvider)
        .csrf(AbstractHttpConfigurer::disable)
    ;

    return http.build();
  }
}
