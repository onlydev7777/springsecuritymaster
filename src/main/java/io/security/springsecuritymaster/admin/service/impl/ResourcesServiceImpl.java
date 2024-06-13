package io.security.springsecuritymaster.admin.service.impl;

import io.security.springsecuritymaster.admin.repository.ResourcesRepository;
import io.security.springsecuritymaster.admin.service.ResourcesService;
import io.security.springsecuritymaster.domain.entity.Resources;
import io.security.springsecuritymaster.security.manager.CustomDynamicAuthorizationManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ResourcesServiceImpl implements ResourcesService {

  private final ResourcesRepository resourcesRepository;
  private final CustomDynamicAuthorizationManager authorizationManager;

  @Override
  public List<Resources> getResources() {
    return resourcesRepository.findAll();
  }

  @Override
  public Resources createResources(Resources resources) {
    Resources saved = resourcesRepository.save(resources);
    authorizationManager.reload();
    return saved;
  }

  @Override
  public Resources getResources(Long id) {
    return resourcesRepository.findById(id).orElseThrow();
  }

  @Override
  public void deleteResources(Long id) {
    resourcesRepository.deleteById(id);
    authorizationManager.reload();
  }
}
