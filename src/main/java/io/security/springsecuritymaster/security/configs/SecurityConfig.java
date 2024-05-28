package io.security.springsecuritymaster.security.configs;

import io.security.springsecuritymaster.security.details.FormAuthenticationDetailsSource;
import io.security.springsecuritymaster.security.provider.FormAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final FormAuthenticationProvider formAuthenticationProvider;
  private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
            .requestMatchers("/", "/signup").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login").permitAll()
            .authenticationDetailsSource(formAuthenticationDetailsSource)
        )
        .authenticationProvider(formAuthenticationProvider)
    ;

    return http.build();
  }
}
