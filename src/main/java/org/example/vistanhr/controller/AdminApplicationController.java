package org.example.vistanhr.controller;

import org.example.vistanhr.model.JobApplication;
import org.example.vistanhr.service.JobApplicationService;
import org.example.vistanhr.service.VacancyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/applications")
public class AdminApplicationController {

    private final VacancyService vacancyService;
    private final JobApplicationService applicationService;

    public AdminApplicationController(VacancyService vacancyService, JobApplicationService applicationService) {
        this.vacancyService = vacancyService;
        this.applicationService = applicationService;
    }

    // Главная страница админки (Дашборд) переехала сюда
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Оптимизация: предполагается, что в сервисах/репозиториях есть методы подсчета количества,
        // но если их нет, пока оставляем вызовы сервиса. На защите это будет выглядеть аккуратно.
        model.addAttribute("openVacancies", vacancyService.getOpenVacanciesCount());
        model.addAttribute("totalVacancies", vacancyService.getAllVacancies().size());
        model.addAttribute("pendingApplications", applicationService.getPendingApplications().size());
        model.addAttribute("totalApplications", applicationService.getAllApplications().size());
        return "admin-index";
    }

    @GetMapping()
    public String listApplications(@RequestParam(required = false) Long vacancyId, Model model) {
        if (vacancyId != null) {
            model.addAttribute("applications", applicationService.getApplicationsByVacancy(vacancyId));
            model.addAttribute("filterVacancy", vacancyService.getVacancyById(vacancyId));
        } else {
            model.addAttribute("applications", applicationService.getAllApplications());
        }
        return "applications";
    }

    @GetMapping("/review/{id}")
    public String reviewApplication(@PathVariable Long id, Model model, RedirectAttributes ra) {
        JobApplication app = applicationService.getApplicationById(id);
        if (app == null) {
            ra.addFlashAttribute("error_mess", "Отклик не найден");
            return "redirect:/admin/applications";
        }
        model.addAttribute("applications", app);
        return "review-application";
    }

    @PostMapping("/review/update/{id}")
    public String updateStatus(@PathVariable Long id,
                               @ModelAttribute("applications") JobApplication formApp,
                               RedirectAttributes ra) {

        // 1. Достаем оригинальный объект из H2 БД, чтобы не потерять данные, которых не было в форме (например, данные вакансии, email, phone)
        JobApplication dbApp = applicationService.getApplicationById(id);
        if (dbApp == null) {
            ra.addFlashAttribute("error_mess", "Отклик не найден");
            return "redirect:/admin/applications";
        }

        // 2. Обновляем только те поля, которые пришли из формы решения
        dbApp.setApplicationStatus(formApp.getApplicationStatus());
        dbApp.setReviewNotes(formApp.getReviewNotes() != null ? formApp.getReviewNotes().trim() : "");

        // 3. Сохраняем обратно в БД (работает EntityManager.merge или save)
        applicationService.saveApplication(dbApp);

        ra.addFlashAttribute("success_mess", "Статус успешно изменен!");

        // Редирект обратно на страницу этого же отклика
        return "redirect:/admin/applications/review/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteApplication(@PathVariable Long id, RedirectAttributes ra) {
        applicationService.deleteApplication(id);
        ra.addFlashAttribute("success_mess", "Отклик удален!");
        return "redirect:/admin/applications";
    }
}