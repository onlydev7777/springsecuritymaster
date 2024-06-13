package io.security.springsecuritymaster.admin.repository;

import io.security.springsecuritymaster.domain.entity.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  List<Role> findAllByExpressionIs(boolean expression);

  Role findByRoleName(String roleName);

  List<Role> findAllByRoleNameIn(List<String> roleNames);
}
