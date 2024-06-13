package io.security.springsecuritymaster.users.service;

import io.security.springsecuritymaster.admin.repository.AccountRoleRepository;
import io.security.springsecuritymaster.admin.repository.RoleRepository;
import io.security.springsecuritymaster.domain.entity.Account;
import io.security.springsecuritymaster.domain.entity.AccountRole;
import io.security.springsecuritymaster.domain.entity.Role;
import io.security.springsecuritymaster.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AccountRoleRepository accountRoleRepository;

  @Transactional
  public void createUser(Account account) {
    Role roleUser = roleRepository.findByRoleName("ROLE_USER");
    Account savedAccount = userRepository.save(account);
    accountRoleRepository.save(
        AccountRole.builder()
            .role(roleUser)
            .account(savedAccount)
            .build()
    );
  }

}