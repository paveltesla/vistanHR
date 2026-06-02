package org.example.vistanhr.service;

import org.example.vistanhr.model.JobApplication;
import org.example.vistanhr.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final VacancyService vacancyService;

    public JobApplicationService(JobApplicationRepository applicationRepository,
                                 VacancyService vacancyService) {
        this.applicationRepository = applicationRepository;
        this.vacancyService = vacancyService;
    }

    public List<JobApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    public JobApplication getApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public List<JobApplication> getApplicationsByVacancy(Long vacancyId) {
        return applicationRepository.findByVacancyId(vacancyId);
    }

    public JobApplication submitApplication(JobApplication application) {
        application.setId(null);
        application.setApplicationDate(LocalDateTime.now());
        application.setApplicationStatus(JobApplication.STATUS_PENDING);
        if (application.getExperience() == null) application.setExperience(0);
        if (application.getExpectedSalary() == null) application.setExpectedSalary(0.0);
        if (application.getCoverLetter() == null) application.setCoverLetter("");
        if (application.getResume() == null) application.setResume("");
        if (application.getReviewNotes() == null) application.setReviewNotes("");
        if (application.getSkills() == null) application.setSkills("");
        if (application.getVacancy() != null) {
            vacancyService.incrementApplicationsCount(application.getVacancy().getId());
        }
        return applicationRepository.save(application);
    }

    @Transactional
    public JobApplication saveApplication(JobApplication application) {
        return applicationRepository.save(application);
    }

    public List<JobApplication> getPendingApplications() {
        return applicationRepository.findByApplicationStatus(JobApplication.STATUS_PENDING);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}