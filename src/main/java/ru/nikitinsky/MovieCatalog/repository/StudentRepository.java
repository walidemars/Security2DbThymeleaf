package ru.nikitinsky.MovieCatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.MovieCatalog.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
