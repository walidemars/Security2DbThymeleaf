package ru.nikitinsky.Security2DbThymeleaf.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import ru.nikitinsky.Security2DbThymeleaf.dto.UserDto;
import ru.nikitinsky.Security2DbThymeleaf.entity.User;
import ru.nikitinsky.Security2DbThymeleaf.service.UserService;

import java.util.List;

@Controller
public class SecurityController {

    private UserService userService;

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
        List<UserDto> users = userService.findAllUsers();
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
}
