package io.security.springsecuritymaster.security.listener;

import io.security.springsecuritymaster.admin.repository.AccountRoleRepository;
import io.security.springsecuritymaster.admin.repository.RoleRepository;
import io.security.springsecuritymaster.domain.entity.Account;
import io.security.springsecuritymaster.domain.entity.AccountRole;
import io.security.springsecuritymaster.domain.entity.Role;
import io.security.springsecuritymaster.users.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private boolean alreadySetup = false;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AccountRoleRepository accountRoleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }
    setupData();
    alreadySetup = true;
  }

  private void setupData() {
    Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
    AccountRole accountRole = createAccountRole(adminRole);
    createUserIfNotFound("admin", "admin@admin.com", "pass", accountRole);
  }

  public Role createRoleIfNotFound(String roleName, String roleDesc) {
    Role role = roleRepository.findByRoleName(roleName);

    if (role == null) {
      role = Role.builder()
          .roleName(roleName)
          .roleDesc(roleDesc)
          .build();
    }
    return roleRepository.save(role);
  }

  public void createUserIfNotFound(final String userName, final String email, final String password, AccountRole accountRole) {
    Optional<Account> optionalAccount = userRepository.findByUsername(userName);

    if (optionalAccount.isEmpty()) {
      Account account = Account.builder()
          .username(userName)
          .password(passwordEncoder.encode(password))
          .build();

      account.addAccountRoleList(accountRole);
      userRepository.save(account);
    }
  }

  public AccountRole createAccountRole(Role role) {
    List<AccountRole> findAllByRoleName = accountRoleRepository.findAllByRole_RoleNameIn(List.of(role.getRoleName()));
    if (findAllByRoleName.isEmpty()) {
      AccountRole accountRole = AccountRole.builder()
          .role(role)
          .build();
      return accountRoleRepository.save(accountRole);
    }
    return findAllByRoleName.stream()
        .filter(accountRole -> accountRole.getRole().getRoleName().equals(role.getRoleName()))
        .findFirst()
        .orElseThrow();
  }
}