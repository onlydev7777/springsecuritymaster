package io.security.springsecuritymaster.security.mapper;

import io.security.springsecuritymaster.admin.repository.ResourcesRepository;
import java.util.LinkedHashMap;
import java.util.Map;

public class PersistentUrlRoleMapper implements UrlRoleMapper {

  private final LinkedHashMap<String, String> urlRoleMappings = new LinkedHashMap<>();
  private final ResourcesRepository resourcesRepository;

  public PersistentUrlRoleMapper(ResourcesRepository resourcesRepository) {
    this.resourcesRepository = resourcesRepository;
  }

  @Override
  public Map<String, String> getUrlRoleMappings() {
    resourcesRepository.findAllResources().forEach(
        urlRoleDto -> urlRoleMappings.put(urlRoleDto.getResourceName(), urlRoleDto.getRoleName())
    );
    return urlRoleMappings;
  }
}
