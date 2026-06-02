package org.example.vistanhr.repository;

import org.example.vistanhr.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByApplicationStatus(String status);
    List<JobApplication> findByVacancyId(Long vacancyId);
    void deleteByVacancyId(Long vacancyId);   // <-- добавлено
    long countByApplicationStatus(String pending);
}