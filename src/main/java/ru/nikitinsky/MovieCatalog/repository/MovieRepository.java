package ru.nikitinsky.MovieCatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.MovieCatalog.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
