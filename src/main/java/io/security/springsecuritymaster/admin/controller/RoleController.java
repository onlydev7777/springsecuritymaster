package io.security.springsecuritymaster.admin.controller;

import io.security.springsecuritymaster.admin.service.RoleService;
import io.security.springsecuritymaster.domain.dto.RoleDto;
import io.security.springsecuritymaster.domain.entity.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class RoleController {

  private final RoleService roleService;
  private final ModelMapper modelMapper;

  @GetMapping(value = "/admin/roles")
  public String getRoles(Model model) {

    List<Role> roles = roleService.getRoles();
    model.addAttribute("roles", roles);

    return "admin/roles";
  }

  @GetMapping(value = "/admin/roles/register")
  public String rolesRegister(Model model) {

    RoleDto role = new RoleDto();
    model.addAttribute("roles", role);

    return "admin/rolesdetails";
  }

  @PostMapping(value = "/admin/roles")
  public String createRole(RoleDto roleDto) {
    Role role = modelMapper.map(roleDto, Role.class);
    role.setExpression(roleDto.getIsExpression());
    roleService.createRole(role);

    return "redirect:/admin/roles";
  }

  @GetMapping(value = "/admin/roles/{id}")
  public String getRole(@PathVariable Long id, Model model) {
    Role role = roleService.getRole(id);

    RoleDto roleDto = modelMapper.map(role, RoleDto.class);
    roleDto.convertExpression(role.isExpression());
    model.addAttribute("roles", roleDto);

    return "admin/rolesdetails";
  }

  @GetMapping(value = "/admin/roles/delete/{id}")
  public String removeRoles(@PathVariable Long id) {

    roleService.deleteRole(id);

    return "redirect:/admin/roles";
  }
}
