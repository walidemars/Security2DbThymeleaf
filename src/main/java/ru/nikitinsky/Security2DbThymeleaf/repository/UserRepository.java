package ru.nikitinsky.Security2DbThymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitinsky.Security2DbThymeleaf.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
