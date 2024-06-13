package io.security.springsecuritymaster.admin.service.impl;

import io.security.springsecuritymaster.admin.repository.RoleHierarchyRepository;
import io.security.springsecuritymaster.admin.service.RoleHierarchyService;
import io.security.springsecuritymaster.domain.entity.RoleHierarchy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

  private final RoleHierarchyRepository roleHierarchyRepository;

  @Override
  public String findAllHierarchy() {
    List<RoleHierarchy> hierarchyList = roleHierarchyRepository.findAll();

    StringBuilder hierarchyRole = new StringBuilder();

    for (RoleHierarchy roleHierarchy : hierarchyList) {
      if (roleHierarchy.getParent() != null) {
        hierarchyRole.append(roleHierarchy.getParent().getRoleName())
            .append(" > ")
            .append(roleHierarchy.getRoleName())
            .append("\n");
      }
    }

    return hierarchyRole.toString();
  }
}
