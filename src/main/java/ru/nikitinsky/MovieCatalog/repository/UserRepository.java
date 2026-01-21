package ru.nikitinsky.MovieCatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nikitinsky.MovieCatalog.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    
    User findByUsername(String username);
}
