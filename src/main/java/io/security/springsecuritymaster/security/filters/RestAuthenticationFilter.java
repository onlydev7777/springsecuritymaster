package io.security.springsecuritymaster.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.security.springsecuritymaster.domain.dto.AccountDto;
import io.security.springsecuritymaster.security.token.RestAuthenticationToken;
import io.security.springsecuritymaster.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public RestAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationSuccessHandler restAuthenticationSuccessHandler,
      AuthenticationFailureHandler restAuthenticationFailureHandler) {
    super(new AntPathRequestMatcher("/api/login", "POST"));
    setAuthenticationManager(authenticationManager);
    setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
    setAuthenticationFailureHandler(restAuthenticationFailureHandler);
  }

  public RestAuthenticationFilter() {
    super(new AntPathRequestMatcher("/api/login", "POST"));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
      throw new IllegalArgumentException("Authentication method not supported");
    }

    AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);

    if (!StringUtils.hasText(accountDto.getUsername()) || !StringUtils.hasText(accountDto.getPassword())) {
      throw new AuthenticationServiceException("Username or Password not provided");
    }

    RestAuthenticationToken restAuthenticationToken = new RestAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

    return getAuthenticationManager().authenticate(restAuthenticationToken);
  }
}
