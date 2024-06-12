package io.security.springsecuritymaster.admin.service.impl;

import io.security.springsecuritymaster.admin.repository.AccountRoleRepository;
import io.security.springsecuritymaster.admin.service.UserManagementService;
import io.security.springsecuritymaster.domain.dto.AccountDto;
import io.security.springsecuritymaster.domain.entity.Account;
import io.security.springsecuritymaster.domain.entity.AccountRole;
import io.security.springsecuritymaster.users.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserManagementServiceImpl implements UserManagementService {

  private final UserRepository userRepository;
  private final AccountRoleRepository accountRoleRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Override
  public List<Account> getUsers() {
    return userRepository.findAll();
  }

  @Transactional
  @Override
  public void modifyUser(AccountDto accountDto) {
    Account account = userRepository.findById(accountDto.getId())
        .orElseThrow();

    account.setUsername(accountDto.getUsername());
    account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
    account.setAge(accountDto.getAge());
    if (accountDto.getRoles() != null) {
      List<AccountRole> allByRoleRoleName = accountRoleRepository.findAllByRole_RoleNameIn(accountDto.getRoles());
      allByRoleRoleName.forEach(accountRole -> accountRoleRepository.save(
          createAccountRole(accountRole, account)
      ));
//      account.setAccountRoleList(allByRoleRoleName);
    }
  }

  private AccountRole createAccountRole(AccountRole accountRole, Account account) {
    return AccountRole.builder()
        .account(account)
        .role(accountRole.getRole())
        .build();
  }

  @Override
  public AccountDto getUser(Long id) {
    Account account = userRepository.findById(id)
        .orElseThrow();

    AccountDto accountDto = modelMapper.map(account, AccountDto.class);
    accountDto.setRoles(account.getRoleNames());
    return accountDto;
  }

  @Transactional
  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
