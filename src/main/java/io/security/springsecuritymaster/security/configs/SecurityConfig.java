package io.security.springsecuritymaster.security.configs;

import io.security.springsecuritymaster.security.filters.RestAuthenticationFilter;
import io.security.springsecuritymaster.security.handler.FormAccessDeniedHandler;
import io.security.springsecuritymaster.security.provider.FormAuthenticationProvider;
import io.security.springsecuritymaster.security.provider.RestAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final FormAuthenticationProvider formAuthenticationProvider;
  private final RestAuthenticationProvider restAuthenticationProvider;
  private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> formAuthenticationDetailsSource;
  private final AuthenticationSuccessHandler formAuthenticationSuccessHandler;
  private final AuthenticationSuccessHandler restAuthenticationSuccessHandler;
  private final AuthenticationFailureHandler formAuthenticationFailureHandler;
  private final AuthenticationFailureHandler restAuthenticationFailureHandler;

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

    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    http
        .securityMatcher("/api/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
            .anyRequest().permitAll()
        )
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(restAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
        .authenticationManager(authenticationManager)
    ;

    return http.build();
  }


  private RestAuthenticationFilter restAuthenticationFilter(AuthenticationManager authenticationManager) {
    return new RestAuthenticationFilter(authenticationManager, restAuthenticationSuccessHandler, restAuthenticationFailureHandler);
  }
}
