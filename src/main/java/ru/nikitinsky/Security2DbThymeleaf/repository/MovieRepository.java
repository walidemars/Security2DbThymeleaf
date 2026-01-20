package ru.nikitinsky.Security2DbThymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.Security2DbThymeleaf.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
