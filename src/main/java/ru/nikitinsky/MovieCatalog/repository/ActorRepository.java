package ru.nikitinsky.MovieCatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.MovieCatalog.entity.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
}
