package io.security.springsecuritymaster.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

public class FormAccessDeniedHandler implements AccessDeniedHandler {

  private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
  private final String errorUrl;

  public FormAccessDeniedHandler(String errorUrl) {
    this.errorUrl = errorUrl;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    String deniedUrl = errorUrl + "?exception=" + accessDeniedException.getMessage();
    redirectStrategy.sendRedirect(request, response, deniedUrl);
  }
}
