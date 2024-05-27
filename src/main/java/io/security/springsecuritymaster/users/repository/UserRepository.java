package io.security.springsecuritymaster.users.repository;

import io.security.springsecuritymaster.domain.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByUsername(String username);
}
