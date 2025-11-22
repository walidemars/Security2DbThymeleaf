package ru.nikitinsky.Security2DbThymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.Security2DbThymeleaf.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
