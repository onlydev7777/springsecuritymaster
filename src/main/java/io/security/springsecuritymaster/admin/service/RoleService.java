package io.security.springsecuritymaster.admin.service;

import io.security.springsecuritymaster.domain.entity.Role;
import java.util.List;

public interface RoleService {

  List<Role> getRolesWithoutExpression();

  List<Role> getRoles();

  void createRole(Role role);

  Role getRole(Long id);

  void deleteRole(Long id);
}
