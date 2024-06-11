package io.security.springsecuritymaster.security.configs;

import io.security.springsecuritymaster.security.dsl.RestApiDsl;
import io.security.springsecuritymaster.security.entrypoint.RestAuthenticationEntryPoint;
import io.security.springsecuritymaster.security.handler.FormAccessDeniedHandler;
import io.security.springsecuritymaster.security.handler.FormAuthenticationFailureHandler;
import io.security.springsecuritymaster.security.handler.FormAuthenticationSuccessHandler;
import io.security.springsecuritymaster.security.handler.RestAccessDeniedHandler;
import io.security.springsecuritymaster.security.handler.RestAuthenticationFailureHandler;
import io.security.springsecuritymaster.security.handler.RestAuthenticationSuccessHandler;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final FormAuthenticationProvider formAuthenticationProvider;
  private final RestAuthenticationProvider restAuthenticationProvider;
  private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> formAuthenticationDetailsSource;
  private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
  private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
  private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;
  private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
            .requestMatchers("/", "/signup", "/login*").permitAll()
            .requestMatchers("/user").hasAuthority("ROLE_USER")
            .requestMatchers("/manager").hasAuthority("ROLE_MANAGER")
            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
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

//    SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
//    if (securityContextRepository == null) {
//      securityContextRepository = new DelegatingSecurityContextRepository(
//          new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()
//      );
//    }

    http
        .securityMatcher("/api/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
            .requestMatchers("/api", "/api/login").permitAll()
            .requestMatchers("/api/user").hasAuthority("ROLE_USER")
            .requestMatchers("/api/manager").hasAuthority("ROLE_MANAGER")
            .requestMatchers("/api/admin").hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated()
        )
//        .csrf(AbstractHttpConfigurer::disable)
//        .addFilterBefore(restAuthenticationFilter(authenticationManager, securityContextRepository), UsernamePasswordAuthenticationFilter.class)
        .authenticationManager(authenticationManager)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .accessDeniedHandler(new RestAccessDeniedHandler())
        )
        .with(new RestApiDsl<>(), restApiDsl -> restApiDsl
            .restSuccessHandler(restAuthenticationSuccessHandler)
            .restFailureHandler(restAuthenticationFailureHandler)
            .loginProcessingUrl("/api/login"))
    ;

    return http.build();
  }

//  private RestAuthenticationFilter restAuthenticationFilter(AuthenticationManager authenticationManager,
//      SecurityContextRepository securityContextRepository) {
//    RestAuthenticationFilter restAuthenticationFilter = new RestAuthenticationFilter(authenticationManager, restAuthenticationSuccessHandler,
//        restAuthenticationFailureHandler);
//    restAuthenticationFilter.setSecurityContextRepository(securityContextRepository);
//    return restAuthenticationFilter;
//  }
}
