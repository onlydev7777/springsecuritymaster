package io.security.springsecuritymaster.security.manager;

import io.security.springsecuritymaster.security.mapper.MapBasedUrlRoleMapper;
import io.security.springsecuritymaster.security.service.DynamicAuthorizationService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@RequiredArgsConstructor
@Component
public class CustomDynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

  private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
  List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;
  private final HandlerMappingIntrospector handlerMappingIntrospector;

  @PostConstruct
  public void mapping() {
    DynamicAuthorizationService dynamicAuthorizationService = new DynamicAuthorizationService(new MapBasedUrlRoleMapper());
    this.mappings = dynamicAuthorizationService.getUrlRoleMappings().entrySet().stream()
        .map(entry -> new RequestMatcherEntry(
            new MvcRequestMatcher(handlerMappingIntrospector, entry.getKey()),
            customAuthorizationManager(entry.getValue()))
        )
        .collect(Collectors.toList());
  }

  @Override
  public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext request) {
    for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : mappings) {
      RequestMatcher requestMatcher = mapping.getRequestMatcher();
      RequestMatcher.MatchResult matchResult = requestMatcher.matcher(request.getRequest());

      if (matchResult.isMatch()) {
        AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
        return manager.check(authentication,
            new RequestAuthorizationContext(request.getRequest(), matchResult.getVariables()));
      }
    }
    return DENY;
  }

  @Override
  public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
    AuthorizationManager.super.verify(authentication, object);
  }

  private AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager(String role) {
    if (role.startsWith("ROLE")) {
      return AuthorityAuthorizationManager.hasAuthority(role);
    } else {
      return new WebExpressionAuthorizationManager(role);
    }
  }
}
