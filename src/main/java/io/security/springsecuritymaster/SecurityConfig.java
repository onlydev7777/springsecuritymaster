//package io.security.springsecuritymaster;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig {
//
//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////    http
////        .authorizeHttpRequests(auth -> auth
////            .anyRequest().authenticated()
////        )
////        .formLogin(Customizer.withDefaults())
////    ;
//
//    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
//        AuthenticationManagerBuilder.class);
//
//    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//
//    http
//        .authorizeHttpRequests(auth -> auth
//            .requestMatchers("/", "/api/login").permitAll()
//            .anyRequest().authenticated())
//        .authenticationManager(authenticationManager)
//        .addFilterBefore(customFilter(http, authenticationManager),
//            UsernamePasswordAuthenticationFilter.class);
//
//    return http.build();
//  }
//
//  private CustomAuthenticationFilter customFilter(HttpSecurity http,
//      AuthenticationManager authenticationManager) {
//    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(http);
//    customAuthenticationFilter.setAuthenticationManager(authenticationManager);
//    return customAuthenticationFilter;
//  }
//}
