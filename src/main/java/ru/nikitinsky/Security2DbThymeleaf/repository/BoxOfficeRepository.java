package ru.nikitinsky.Security2DbThymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.Security2DbThymeleaf.entity.BoxOffice;
import ru.nikitinsky.Security2DbThymeleaf.entity.Movie;

import java.util.List;

@Repository
public interface BoxOfficeRepository extends JpaRepository<BoxOffice, Long> {

    List<BoxOffice> findByMovie(Movie movie);
}
