package io.security.springsecuritymaster.users.controller;

import io.security.springsecuritymaster.domain.dto.AccountDto;
import io.security.springsecuritymaster.domain.entity.Account;
import io.security.springsecuritymaster.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService service;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @PostMapping("/signup")
  public String signup(AccountDto accountDto) {
    Account account = modelMapper.map(accountDto, Account.class);
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    service.createUser(account);

    return "redirect:/";
  }
}
