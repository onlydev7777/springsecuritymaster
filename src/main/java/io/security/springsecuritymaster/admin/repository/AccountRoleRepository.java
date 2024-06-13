package io.security.springsecuritymaster.admin.repository;

import io.security.springsecuritymaster.domain.entity.Account;
import io.security.springsecuritymaster.domain.entity.AccountRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

  void deleteByAccount(Account account);

  List<AccountRole> findAllByRole_RoleNameIn(List<String> roleNames);
}
