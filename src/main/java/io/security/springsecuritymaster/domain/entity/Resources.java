package io.security.springsecuritymaster.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "resources")
@Entity
public class Resources {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String resourceName;

  @Column
  private HttpMethod httpMethod;

  @Column
  private Integer orderNum;

  @Column
  private String resourceType;

  @OneToMany(mappedBy = "resources", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoleResources> roleResourcesList = new ArrayList<>();

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = HttpMethod.valueOf(httpMethod);
  }

  public void setRoleResourcesList(List<RoleResources> roleResourcesList) {
    this.roleResourcesList = roleResourcesList;
  }

  public void addRoleResourcesList(RoleResources roleResources) {
    this.roleResourcesList.add(roleResources);
    roleResources.mapResources(this);
  }
}
