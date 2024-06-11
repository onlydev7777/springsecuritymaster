package io.security.springsecuritymaster.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

  private Long id;
  private String username;
  private String password;
  private int age;
  private List<String> roles;
}
