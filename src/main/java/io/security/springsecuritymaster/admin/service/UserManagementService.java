package io.security.springsecuritymaster.admin.service;

import io.security.springsecuritymaster.domain.dto.AccountDto;
import io.security.springsecuritymaster.domain.entity.Account;
import java.util.List;

public interface UserManagementService {

  List<Account> getUsers();

  void modifyUser(AccountDto accountDto);

  AccountDto getUser(Long id);

  void deleteUser(Long id);
}
