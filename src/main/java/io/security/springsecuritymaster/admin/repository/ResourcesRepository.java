package io.security.springsecuritymaster.admin.repository;

import io.security.springsecuritymaster.domain.entity.Resources;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

  @Query("select r from Resources r "
      + "join fetch RoleResources rr "
      + "where r.resourceType = 'url' "
      + "order by r.orderNum desc")
  List<Resources> findAllResources();
}
