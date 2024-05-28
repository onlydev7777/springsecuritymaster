package io.security.springsecuritymaster.security.provider;

import io.security.springsecuritymaster.domain.dto.AccountContext;
import io.security.springsecuritymaster.security.details.FormAuthenticationDetails;
import io.security.springsecuritymaster.security.exception.SecretException;
import io.security.springsecuritymaster.security.service.FormUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FormAuthenticationProvider implements AuthenticationProvider {

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

    FormAuthenticationDetails details = (FormAuthenticationDetails) authentication.getDetails();
    String secretKey = details.getSecretKey();
    if (secretKey == null || !secretKey.equals("secret")) {
      throw new SecretException("Invalid Securet : " + secretKey);
    }
    return new UsernamePasswordAuthenticationToken(accountContext.getAccountDto(), null, accountContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
  }
}
