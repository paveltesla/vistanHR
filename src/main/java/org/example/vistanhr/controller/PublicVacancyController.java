package org.example.vistanhr.controller;

import org.example.vistanhr.model.Vacancy;
import org.example.vistanhr.repository.VacancyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PublicVacancyController {

    private final VacancyRepository vacancyRepository;

    public PublicVacancyController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    // 1. Обработка Главной страницы (index.html)
    @GetMapping("/")
    public String showIndexPage(Model model) {
        List<Vacancy> openVacancies = vacancyRepository.findByStatus("OPEN");

        // Передаем openVacancies, чтобы сработал твой #lists.size(openVacancies) на главной!
        model.addAttribute("openVacancies", openVacancies);

        return "index"; // Имя файла index.html
    }

    // 2. Обработка страницы со списком вакансий (public-vacancies.html)
    @GetMapping("/vacancies/public")
    public String showPublicVacancies(Model model) {
        List<Vacancy> openVacancies = vacancyRepository.findByStatus("OPEN");

        // Передаем под именем "vacancies" для th:each="v : ${vacancies}"
        model.addAttribute("vacancies", openVacancies);

        return "public-vacancies"; // Имя файла public-vacancies.html
    }
}