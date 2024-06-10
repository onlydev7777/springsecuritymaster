package io.security.springsecuritymaster.security.provider;

import io.security.springsecuritymaster.domain.dto.AccountContext;
import io.security.springsecuritymaster.security.service.FormUserDetailsService;
import io.security.springsecuritymaster.security.token.RestAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("restAuthenticationProvider")
public class RestAuthenticationProvider implements AuthenticationProvider {

  private final FormUserDetailsService formUserDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    AccountContext accountContext = (AccountContext) formUserDetailsService.loadUserByUsername(username);

    if (!passwordEncoder.matches(password, accountContext.getPassword())) {
      throw new BadCredentialsException("Invalid Password : " + password);
    }

    return new RestAuthenticationToken(accountContext.getAuthorities(), accountContext.getAccountDto(), null);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(RestAuthenticationToken.class);
  }
}
