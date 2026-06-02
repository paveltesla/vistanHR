package org.example.vistanhr.service;

import org.example.vistanhr.model.Vacancy;
import org.example.vistanhr.repository.VacancyRepository;
import org.example.vistanhr.repository.JobApplicationRepository;   // добавлен импорт
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final JobApplicationRepository applicationRepository;   // новое поле

    public VacancyService(VacancyRepository vacancyRepository,
                          JobApplicationRepository applicationRepository) {   // изменён конструктор
        this.vacancyRepository = vacancyRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    public List<Vacancy> getOpenVacancies() {
        return vacancyRepository.findByStatus(Vacancy.STATUS_OPEN);
    }

    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    public Vacancy createVacancy(Vacancy vacancy) {
        vacancy.setId(null);
        vacancy.setCreatedDate(LocalDateTime.now());
        if (vacancy.getStatus() == null) vacancy.setStatus(Vacancy.STATUS_OPEN);
        if (vacancy.getApplicationsCount() == null) vacancy.setApplicationsCount(0);
        if (vacancy.getSalaryMin() == null) vacancy.setSalaryMin(0.0);
        if (vacancy.getSalaryMax() == null) vacancy.setSalaryMax(0.0);
        if (vacancy.getRequiredExperience() == null) vacancy.setRequiredExperience(0);
        return vacancyRepository.save(vacancy);
    }

    public Vacancy updateVacancy(Long id, Vacancy details) {
        Vacancy vacancy = getVacancyById(id);
        if (vacancy == null) return null;
        vacancy.setTitle(details.getTitle());
        vacancy.setDepartment(details.getDepartment());
        vacancy.setVacancyType(details.getVacancyType());
        vacancy.setDescription(details.getDescription());
        vacancy.setRequirements(details.getRequirements());
        vacancy.setResponsibilities(details.getResponsibilities());
        vacancy.setSalaryMin(details.getSalaryMin());
        vacancy.setSalaryMax(details.getSalaryMax());
        vacancy.setRequiredExperience(details.getRequiredExperience());
        vacancy.setWorkSchedule(details.getWorkSchedule());
        vacancy.setEmploymentType(details.getEmploymentType());
        vacancy.setLocation(details.getLocation());
        vacancy.setNotes(details.getNotes());
        if (details.getStatus() != null) vacancy.setStatus(details.getStatus());
        return vacancyRepository.save(vacancy);
    }

    public void closeVacancy(Long id) {
        Vacancy vacancy = getVacancyById(id);
        if (vacancy != null) {
            vacancy.setStatus(Vacancy.STATUS_CLOSED);
            vacancy.setClosingDate(LocalDateTime.now());
            vacancyRepository.save(vacancy);
        }
    }

    public void incrementApplicationsCount(Long id) {
        Vacancy vacancy = getVacancyById(id);
        if (vacancy != null) {
            vacancy.setApplicationsCount(vacancy.getApplicationsCount() + 1);
            vacancyRepository.save(vacancy);
        }
    }

    public void deleteVacancy(Long id) {
        // Сначала удаляем все заявки, связанные с вакансией
        applicationRepository.deleteByVacancyId(id);
        // Затем удаляем саму вакансию
        vacancyRepository.deleteById(id);
    }

    public long getOpenVacanciesCount() {
        return vacancyRepository.findByStatus(Vacancy.STATUS_OPEN).size();
    }
}