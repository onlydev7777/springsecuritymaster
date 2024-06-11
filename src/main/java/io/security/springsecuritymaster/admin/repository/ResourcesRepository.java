package io.security.springsecuritymaster.admin.repository;

import io.security.springsecuritymaster.domain.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

}
