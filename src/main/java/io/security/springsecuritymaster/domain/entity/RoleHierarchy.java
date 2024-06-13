package io.security.springsecuritymaster.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "role_hierarchy")
@Entity
public class RoleHierarchy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String roleName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private RoleHierarchy parent;

  @OneToMany(mappedBy = "parent")
  private Set<RoleHierarchy> children = new HashSet<>();

  @Builder
  public RoleHierarchy(String roleName, RoleHierarchy parent, Set<RoleHierarchy> children) {
    this.roleName = roleName;
    this.parent = parent;
    this.children = children;
  }
}
