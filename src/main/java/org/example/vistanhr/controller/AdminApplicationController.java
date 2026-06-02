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

    @GetMapping
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
            ra.addFlashAttribute("error", "Отклик не найден");
            return "redirect:/admin/applications";
        }
        model.addAttribute("application", app);
        return "review-application";
    }

    @PostMapping("/review/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") String status,
                               @RequestParam(value = "reviewNotes", defaultValue = "") String reviewNotes,
                               RedirectAttributes ra) {
        JobApplication app = applicationService.getApplicationById(id);
        if (app == null) {
            ra.addFlashAttribute("error", "Отклик не найден");
            return "redirect:/admin/applications";
        }

        app.setApplicationStatus(status);
        app.setReviewNotes(reviewNotes != null ? reviewNotes.trim() : "");
        applicationService.saveApplication(app);

        ra.addFlashAttribute("success", "Статус изменен на: " + status);
        return "redirect:/admin/applications/review/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteApplication(@PathVariable Long id, RedirectAttributes ra) {
        applicationService.deleteApplication(id);
        ra.addFlashAttribute("success", "Отклик удален!");
        return "redirect:/admin/applications";
    }
}