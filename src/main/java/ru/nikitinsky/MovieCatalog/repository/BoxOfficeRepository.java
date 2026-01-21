package ru.nikitinsky.MovieCatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.MovieCatalog.entity.BoxOffice;
import ru.nikitinsky.MovieCatalog.entity.Movie;

import java.util.List;

@Repository
public interface BoxOfficeRepository extends JpaRepository<BoxOffice, Long> {

    List<BoxOffice> findByMovie(Movie movie);
}
