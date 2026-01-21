package ru.nikitinsky.MovieCatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitinsky.MovieCatalog.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
