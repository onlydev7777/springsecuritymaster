package io.security.springsecuritymaster.security.service;

import io.security.springsecuritymaster.domain.dto.AccountContext;
import io.security.springsecuritymaster.domain.dto.AccountDto;
import io.security.springsecuritymaster.domain.entity.Account;
import io.security.springsecuritymaster.users.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FormUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("No user name with " + username));

    ModelMapper modelMapper = new ModelMapper();
    AccountDto accountDto = modelMapper.map(account, AccountDto.class);

    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(accountDto.getRoles()));

    return new AccountContext(accountDto, authorities);
  }
}
