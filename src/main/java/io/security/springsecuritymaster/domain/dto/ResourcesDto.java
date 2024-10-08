package io.security.springsecuritymaster.domain.dto;

import io.security.springsecuritymaster.domain.entity.Role;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesDto {

  private String id;
  private String resourceName;
  private String httpMethod;
  private int orderNum;
  private String resourceType;
  private String roleName;
  private Set<Role> roleSet;
}
