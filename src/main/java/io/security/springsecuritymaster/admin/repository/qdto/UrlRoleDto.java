package io.security.springsecuritymaster.admin.repository.qdto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UrlRoleDto {

  private String resourceName;
  private String roleName;
}
