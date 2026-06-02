package org.example.vistanhr.controller;

import org.example.vistanhr.model.JobApplication;
import org.example.vistanhr.model.Vacancy;
import org.example.vistanhr.repository.JobApplicationRepository;
import org.example.vistanhr.repository.VacancyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;

    public JobApplicationController(JobApplicationRepository applicationRepository, VacancyRepository vacancyRepository) {
        this.applicationRepository = applicationRepository;
        this.vacancyRepository = vacancyRepository;
    }

    // 1. Показ формы отклика (Переход с карточки вакансии)
    @GetMapping("/apply/{vacancyId}")
    public String showApplyForm(@PathVariable Long vacancyId, Model model, RedirectAttributes redirectAttributes) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);

        if (vacancy == null) {
            redirectAttributes.addFlashAttribute("error", "Вакансия не найдена!");
            return "redirect:/";
        }

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("application", new JobApplication()); // Инициализируем th:object="${application}"

        return "apply-vacancy"; // Имя твоего HTML файла формы
    }

    // 2. Обработка отправки формы
    @PostMapping("/apply")
    public String handleApply(@Valid @ModelAttribute("application") JobApplication application,
                              BindingResult bindingResult,
                              @RequestParam("vacancyId") Long vacancyId,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        // Получаем вакансию, чтобы заново перерендерить форму в случае ошибок
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);

        if (bindingResult.hasErrors()) {
            model.addAttribute("vacancy", vacancy);
            return "apply-vacancy"; // Если есть ошибки валидации, возвращаем форму назад с подсветкой ошибок
        }

        if (vacancy == null) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Вакансия не существует.");
            return "redirect:/";
        }

        // Привязываем вакансию к отклику и выставляем дефолтный статус
        application.setVacancy(vacancy);
        application.setApplicationStatus("PENDING"); // Или твой Enum, например: ApplicationStatus.PENDING

        // Сохраняем в PostgreSQL/H2
        applicationRepository.save(application);

        // FlashAttribute передает сообщение один раз при редиректе (сработает th:if="${success}")
        redirectAttributes.addFlashAttribute("success", "Спасибо! Ваша заявка на вакансию '" + vacancy.getTitle() + "' успешно отправлена.");

        return "redirect:/"; // После успешной отправки кидаем на главную
    }
}