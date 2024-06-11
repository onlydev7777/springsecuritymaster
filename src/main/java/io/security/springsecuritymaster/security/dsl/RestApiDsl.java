package io.security.springsecuritymaster.security.dsl;

import io.security.springsecuritymaster.security.filters.RestAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class RestApiDsl<H extends HttpSecurityBuilder<H>> extends
    AbstractAuthenticationFilterConfigurer<H, RestApiDsl<H>, RestAuthenticationFilter> {

  private AuthenticationSuccessHandler successHandler;
  private AuthenticationFailureHandler failureHandler;

  public RestApiDsl() {
    super(new RestAuthenticationFilter(), null);
  }

  @Override
  public void init(H http) throws Exception {
    super.init(http);
  }

  @Override
  public void configure(H http) throws Exception {
    
    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    RestAuthenticationFilter authenticationFilter = getAuthenticationFilter();

    authenticationFilter.setAuthenticationManager(authenticationManager);
    authenticationFilter.setAuthenticationSuccessHandler(successHandler);
    authenticationFilter.setAuthenticationFailureHandler(failureHandler);
    authenticationFilter.setSecurityContextRepository(getSecurityContextRepository(http));

    SessionAuthenticationStrategy sessionAuthenticationStrategy = http.getSharedObject(SessionAuthenticationStrategy.class);
    if (sessionAuthenticationStrategy != null) {
      authenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
    }
    RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
    if (rememberMeServices != null) {
      authenticationFilter.setRememberMeServices(rememberMeServices);
    }
    http.setSharedObject(RestAuthenticationFilter.class, authenticationFilter);
    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  private SecurityContextRepository getSecurityContextRepository(H http) {
    SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
    if (securityContextRepository == null) {
      securityContextRepository = new DelegatingSecurityContextRepository(
          new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()
      );
    }
    return securityContextRepository;
  }

  public RestApiDsl<H> restSuccessHandler(AuthenticationSuccessHandler successHandler) {
    this.successHandler = successHandler;
    return this;
  }

  public RestApiDsl<H> restFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
    this.failureHandler = authenticationFailureHandler;
    return this;
  }

  public RestApiDsl<H> loginPage(String loginPage) {
    return super.loginPage(loginPage);
  }

  public RestApiDsl<H> setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
    return super.securityContextRepository(securityContextRepository);
  }

  @Override
  protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return new AntPathRequestMatcher(loginProcessingUrl, "POST");
  }
}
