package org.example.vistanhr.repository;

import org.example.vistanhr.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findByStatus(String status);
    List<Vacancy> findByVacancyType(String type);
    long countByStatus(String status); // или countByStatus(VacancyStatus status), если там Enum
}