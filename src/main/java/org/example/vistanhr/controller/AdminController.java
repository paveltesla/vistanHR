package org.example.vistanhr.controller;

import org.example.vistanhr.repository.JobApplicationRepository;
import org.example.vistanhr.repository.VacancyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final VacancyRepository vacancyRepository;
    private final JobApplicationRepository applicationRepository;

    // Внедряем репозитории через конструктор
    public AdminController(VacancyRepository vacancyRepository, JobApplicationRepository applicationRepository) {
        this.vacancyRepository = vacancyRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping
    public String showDashboard(Model model) {
        // 1. Статистика вакансий (из H2 базы, которую мы только что заполнили)
        long totalVacancies = vacancyRepository.count();
        // Предполагаем, что у тебя в Vacancy есть поле status (например, "OPEN")
        // Если статус — это Enum или строка, подправь метод в репозитории (см. ниже)
        long openVacancies = vacancyRepository.countByStatus("OPEN");

        // 2. Статистика откликов
        long totalApplications = applicationRepository.count();
        // Считаем новые отклики со статусом "PENDING"
        long pendingApplications = applicationRepository.countByApplicationStatus("PENDING");

        // Передаем переменные в модель — точно как в твоем HTML шаблоне
        model.addAttribute("totalVacancies", totalVacancies);
        model.addAttribute("openVacancies", openVacancies);
        model.addAttribute("totalApplications", totalApplications);
        model.addAttribute("pendingApplications", pendingApplications);

        // Возвращаем имя HTML-файла (без .html) из папки templates
        return "admin-index";
    }
}