package ru.nikitinsky.MovieCatalog.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.ModelAndView;
import ru.nikitinsky.MovieCatalog.dto.UserDto;
import ru.nikitinsky.MovieCatalog.entity.Role;
import ru.nikitinsky.MovieCatalog.entity.User;
import ru.nikitinsky.MovieCatalog.repository.RoleRepository;
import ru.nikitinsky.MovieCatalog.repository.UserRepository;
import ru.nikitinsky.MovieCatalog.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class SecurityController {

    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }
    
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        User existingUserByEmail = userService.findUserByEmail(userDto.getEmail());

        if (existingUserByEmail != null && existingUserByEmail.getEmail() != null && !existingUserByEmail.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "На этот адрес электронной почты уже зарегистрирована учетная запись");
        }

        User existingUserByUsername = userService.findUserByUsername(userDto.getUsername());
        if (existingUserByUsername != null && existingUserByUsername.getUsername() != null && !existingUserByUsername.getUsername().isEmpty()) {
            result.rejectValue("username", null,
                    "Этот username уже занят");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@RequestParam Long userId, Authentication authentication) {
        UserDto current = null;
        if (authentication != null) {
            User u = userService.findUserByUsername(authentication.getName());
            if (u != null && u.getId() == userId.intValue()) {
                return "redirect:/users?error=self";
            }
        }
        userService.deleteUserById(userId);
        return "redirect:/users?deleted";
    }

    @GetMapping("/users/edit-roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editUserRoles(@RequestParam Long userId) {
        log.info("/users/edit-roles -> editing roles for user id: {}", userId);
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

    @PostMapping("/users/save-roles")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveUserRoles(@RequestParam Long userId, @RequestParam(required = false) List<String> roleNames) {
        log.info("/users/save-roles -> saving roles for user id: {}", userId);
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
        return "redirect:/users";
    }
}
