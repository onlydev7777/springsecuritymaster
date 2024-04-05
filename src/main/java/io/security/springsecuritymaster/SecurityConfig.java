package io.security.springsecuritymaster;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults());
    
//        .formLogin(form -> form
////            .loginPage("/loginPage")
//            .loginProcessingUrl("/loginProc")
//            .defaultSuccessUrl("/", true)
//            .failureUrl("/failed")
//            .usernameParameter("userId")
//            .passwordParameter("passwd")
//            .successHandler((request, response, authentication) -> {  //defaultSuccessUrl 보다 우선
//              System.out.println("authentication : " + authentication);
//              response.sendRedirect("/home");
//            })
//            .failureHandler((request, response, exception) -> {       //failureUrl 보다 우선
//              System.out.println("exception = " + exception.getMessage());
//              response.sendRedirect("/login");
//            })
//            .permitAll());

    return http.build();
  }
}
