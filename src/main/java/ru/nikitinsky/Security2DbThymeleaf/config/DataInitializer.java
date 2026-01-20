package ru.nikitinsky.Security2DbThymeleaf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nikitinsky.Security2DbThymeleaf.entity.Role;
import ru.nikitinsky.Security2DbThymeleaf.entity.User;
import ru.nikitinsky.Security2DbThymeleaf.repository.RoleRepository;
import ru.nikitinsky.Security2DbThymeleaf.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception { // инициализируем роли

        if (roleRepository.findByName("ROLE_READ_ONLY") == null) {
            Role readOnlyRole = new Role();
            readOnlyRole.setName("ROLE_READ_ONLY");
            roleRepository.save(readOnlyRole);
        }

        if (roleRepository.findByName("ROLE_USER") == null) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN"); // админ по умолчанию Логин/пароль: admin/admin
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole = roleRepository.save(adminRole);
        }

        User admin = userRepository.findByUsername("admin");
        if (admin == null) {
            admin = new User();
            admin.setUsername("admin");
            admin.setName("Admin Admin");
            admin.setEmail("admin@local");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(java.util.List.of(adminRole));
            userRepository.save(admin);
        } else {
            boolean hasAdmin = admin.getRoles() != null && admin.getRoles().stream() // проверка существует ли админ
                    .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
            if (!hasAdmin) {
                admin.setRoles(java.util.List.of(adminRole));
                userRepository.save(admin);
            }
        }
    }
}
