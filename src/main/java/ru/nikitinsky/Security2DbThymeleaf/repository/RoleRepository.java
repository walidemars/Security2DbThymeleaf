package ru.nikitinsky.Security2DbThymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitinsky.Security2DbThymeleaf.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
