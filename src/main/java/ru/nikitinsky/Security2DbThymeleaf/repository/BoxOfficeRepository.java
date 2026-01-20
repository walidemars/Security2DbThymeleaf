package ru.nikitinsky.Security2DbThymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitinsky.Security2DbThymeleaf.entity.BoxOffice;

@Repository
public interface BoxOfficeRepository extends JpaRepository<BoxOffice, Long> {
}
