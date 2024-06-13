package io.security.springsecuritymaster.admin.service;

import io.security.springsecuritymaster.domain.entity.Resources;
import java.util.List;

public interface ResourcesService {

  List<Resources> getResources();

  Resources createResources(Resources resources);

  Resources getResources(Long id);

  void deleteResources(Long id);
}
