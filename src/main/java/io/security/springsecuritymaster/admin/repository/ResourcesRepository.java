package io.security.springsecuritymaster.admin.repository;

import io.security.springsecuritymaster.admin.repository.qdto.UrlRoleDto;
import io.security.springsecuritymaster.domain.entity.Resources;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

  @Query("select new io.security.springsecuritymaster.admin.repository.qdto."
      + "   UrlRoleDto(r.resourceName, role.roleName) "
      + "from Resources r "
      + "join RoleResources rr "
      + "on r.id = rr.resources.id "
      + "join Role role "
      + "on rr.role.id = role.id "
      + "where r.resourceType = 'url' "
      + "order by r.orderNum desc")
  List<UrlRoleDto> findAllResources();
}
