package io.security.springsecuritymaster.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
public class Account implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  //  private String roles;
  private int age;


  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AccountRole> accountRoleList = new ArrayList<>();

  public void addAccountRoleList(AccountRole accountRole) {
    this.accountRoleList.add(accountRole);
    accountRole.mapAccount(this);
  }

  public List<String> getRoleNames() {
    return this.accountRoleList.stream()
        .map(accountRole -> accountRole.getRole().getRoleName())
        .toList();
  }

  @Builder
  public Account(String username, String password, int age) {
    this.username = username;
    this.password = password;
    this.age = age;
  }
}
