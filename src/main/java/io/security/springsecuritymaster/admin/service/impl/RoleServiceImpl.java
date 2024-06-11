package io.security.springsecuritymaster.admin.service.impl;

import io.security.springsecuritymaster.admin.repository.RoleRepository;
import io.security.springsecuritymaster.admin.service.RoleService;
import io.security.springsecuritymaster.domain.entity.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  @Override
  public List<Role> getRolesWithoutExpression() {
    return roleRepository.findAllByExpressionIs(false);
  }

  @Override
  public List<Role> getRoles() {
    return roleRepository.findAll();
  }

  @Transactional
  @Override
  public void createRole(Role role) {
    roleRepository.save(role);
  }

  @Override
  public Role getRole(Long id) {
    return roleRepository.findById(id).orElseThrow();
  }

  @Transactional
  @Override
  public void deleteRole(Long id) {
    roleRepository.deleteById(id);
  }
}
