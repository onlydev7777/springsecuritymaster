package io.security.springsecuritymaster.admin.controller;

import io.security.springsecuritymaster.admin.repository.RoleRepository;
import io.security.springsecuritymaster.admin.repository.RoleResourcesRepository;
import io.security.springsecuritymaster.admin.service.ResourcesService;
import io.security.springsecuritymaster.admin.service.RoleService;
import io.security.springsecuritymaster.domain.dto.ResourcesDto;
import io.security.springsecuritymaster.domain.entity.Resources;
import io.security.springsecuritymaster.domain.entity.Role;
import io.security.springsecuritymaster.domain.entity.RoleResources;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ResourcesController {

  private final ResourcesService resourcesService;
  private final RoleRepository roleRepository;
  private final RoleResourcesRepository roleResourcesRepository;
  private final RoleService roleService;
  private final ModelMapper modelMapper;

  @GetMapping(value = "/admin/resources")
  public String getResources(Model model) {
    List<Resources> resources = resourcesService.getResources();
    model.addAttribute("resources", resources);

    return "admin/resources";
  }

  @PostMapping(value = "/admin/resources")
  public String createResources(ResourcesDto resourcesDto) {
    Role role = roleRepository.findByRoleName(resourcesDto.getRoleName());
    Resources resources = modelMapper.map(resourcesDto, Resources.class);
    resources.setHttpMethod(resourcesDto.getHttpMethod());

    Resources savedResources = resourcesService.createResources(resources);
    roleResourcesRepository.save(
        RoleResources.builder()
            .role(role)
            .resources(savedResources)
            .build()
    );
    
    return "redirect:/admin/resources";
  }

  @GetMapping(value = "/admin/resources/register")
  public String resourcesRegister(Model model) {

    List<Role> roleList = roleService.getRoles();
    List<String> myRoles = new ArrayList<>();
    ResourcesDto resourcesDto = new ResourcesDto();

    Set<Role> roleSet = new HashSet<>();
    roleSet.add(new Role());
    resourcesDto.setRoleSet(roleSet);

    model.addAttribute("roleList", roleList);
    model.addAttribute("myRoles", myRoles);
    model.addAttribute("resources", resourcesDto);

    return "admin/resourcesdetails";
  }

  @GetMapping(value = "/admin/resources/{id}")
  public String resourceDetails(@PathVariable Long id, Model model) {

    List<Role> roleList = roleService.getRoles();
    Resources resources = resourcesService.getResources(id);
    List<String> myRoles = resources.getRoleResourcesList().stream()
        .map(roleResources -> roleResources.getRole().getRoleName())
        .toList();

    ResourcesDto resourcesDto = modelMapper.map(resources, ResourcesDto.class);
    Set<Role> roleSet = resources.getRoleResourcesList().stream()
        .map(RoleResources::getRole)
        .collect(Collectors.toSet());
    resourcesDto.setRoleSet(roleSet);

    model.addAttribute("roleList", roleList);
    model.addAttribute("myRoles", myRoles);
    model.addAttribute("resources", resourcesDto);

    return "admin/resourcesdetails";
  }

  @GetMapping(value = "/admin/resources/delete/{id}")
  public String removeResources(@PathVariable Long id) throws Exception {

    resourcesService.deleteResources(id);

    return "redirect:/admin/resources";
  }
}
