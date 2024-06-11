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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Getter
@NoArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String roleName;

  @Column
  private String roleDesc;

  @Column(name = "is_expression")
  private boolean expression;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AccountRole> accountRoleList = new ArrayList<>();

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoleResources> roleResourcesList = new ArrayList<>();

  public void addAccountRoleList(AccountRole accountRole) {
    this.accountRoleList.add(accountRole);
    accountRole.mapRole(this);
  }

  public void addRoleResourcesList(RoleResources roleResources) {
    this.roleResourcesList.add(roleResources);
    roleResources.mapRole(this);
  }

  public void setExpression(String isExpression) {
    this.expression = "Y".equals(isExpression);
  }

  @Builder
  public Role(String roleName, String roleDesc, boolean expression, List<AccountRole> accountRoleList, List<RoleResources> roleResourcesList) {
    this.roleName = roleName;
    this.roleDesc = roleDesc;
    this.expression = expression;
    this.accountRoleList = accountRoleList;
    this.roleResourcesList = roleResourcesList;
  }
}
