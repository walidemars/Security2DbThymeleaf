package ru.nikitinsky.MovieCatalog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.nikitinsky.MovieCatalog.entity.Role;
import ru.nikitinsky.MovieCatalog.entity.User;
import ru.nikitinsky.MovieCatalog.repository.RoleRepository;
import ru.nikitinsky.MovieCatalog.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/roles")
    public ModelAndView showRoleManagement() {
        log.info("/roles -> role management page");
        ModelAndView mav = new ModelAndView("role-management");
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping("/roles/edit")
    public ModelAndView editUserRoles(@RequestParam Long userId) {
        log.info("/roles/edit -> editing roles for user id: {}", userId);
        ModelAndView mav = new ModelAndView("edit-roles");
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            mav.addObject("user", user);
            List<Role> allRoles = roleRepository.findAll();
            mav.addObject("allRoles", allRoles);
            List<String> userRoleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();
            mav.addObject("userRoleNames", userRoleNames);
        }
        return mav;
    }

    @PostMapping("/roles/save")
    public String saveUserRoles(@RequestParam Long userId, @RequestParam(required = false) List<String> roleNames) {
        log.info("/roles/save -> saving roles for user id: {}", userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<Role> newRoles = new ArrayList<>();
            if (roleNames != null) {
                for (String roleName : roleNames) {
                    Role role = roleRepository.findByName(roleName);
                    if (role == null) {
                        role = new Role();
                        role.setName(roleName);
                        role = roleRepository.save(role);
                    }
                    newRoles.add(role);
                }
            }
            user.setRoles(newRoles);
            userRepository.save(user);
        }
        return "redirect:/roles";
    }
}
