package io.security.springsecuritymaster.admin.service.impl;

import io.security.springsecuritymaster.admin.repository.ResourcesRepository;
import io.security.springsecuritymaster.admin.service.ResourcesService;
import io.security.springsecuritymaster.domain.entity.Resources;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ResourcesServiceImpl implements ResourcesService {

  private final ResourcesRepository resourcesRepository;

  @Override
  public List<Resources> getResources() {
    return resourcesRepository.findAll();
  }

  @Override
  public void createResources(Resources resources) {
    resourcesRepository.save(resources);
  }

  @Override
  public Resources getResources(Long id) {
    return resourcesRepository.findById(id).orElseThrow();
  }

  @Override
  public void deleteResources(Long id) {
    resourcesRepository.deleteById(id);
  }
}
