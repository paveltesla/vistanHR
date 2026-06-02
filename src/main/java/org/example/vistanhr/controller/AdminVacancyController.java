package org.example.vistanhr.controller;

import org.example.vistanhr.model.Vacancy;
import org.example.vistanhr.service.JobApplicationService;
import org.example.vistanhr.service.VacancyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/vacancies") // Все URL этого класса начинаются с этого префикса
public class AdminVacancyController {

    private final VacancyService vacancyService;
    private final JobApplicationService applicationService;

    public AdminVacancyController(VacancyService vacancyService, JobApplicationService applicationService) {
        this.vacancyService = vacancyService;
        this.applicationService = applicationService;
    }

    @GetMapping
    public String listVacancies(Model model) {
        model.addAttribute("vacancies", vacancyService.getAllVacancies());
        return "vacancies";
    }

    @GetMapping("/add")
    public String addVacancyForm(Model model) {
        model.addAttribute("vacancy", new Vacancy());
        return "add-vacancy";
    }

    @PostMapping("/add")
    public String addVacancy(@Valid @ModelAttribute("vacancy") Vacancy vacancy,
                             BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) return "add-vacancy";
        vacancyService.createVacancy(vacancy);
        ra.addFlashAttribute("success_mess", "Вакансия создана!");
        return "redirect:/admin/vacancies";
    }

    @GetMapping("/edit/{id}")
    public String editVacancyForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Vacancy vacancy = vacancyService.getVacancyById(id);
        if (vacancy == null) {
            ra.addFlashAttribute("error_mess", "Вакансия не найдена");
            return "redirect:/admin/vacancies";
        }
        model.addAttribute("vacancy", vacancy);
        return "edit-vacancy";
    }

    @PostMapping("/edit/{id}")
    public String updateVacancy(@PathVariable Long id,
                                @Valid @ModelAttribute("vacancy") Vacancy vacancy,
                                BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) return "edit-vacancy";
        vacancyService.updateVacancy(id, vacancy);
        ra.addFlashAttribute("success_mess", "Вакансия обновлена!");
        return "redirect:/admin/vacancies";
    }

    @GetMapping("/close/{id}")
    public String closeVacancy(@PathVariable Long id, RedirectAttributes ra) {
        vacancyService.closeVacancy(id);
        ra.addFlashAttribute("success_mess", "Вакансия закрыта!");
        return "redirect:/admin/vacancies";
    }

    @GetMapping("/delete/{id}")
    public String deleteVacancy(@PathVariable Long id, RedirectAttributes ra) {
        vacancyService.deleteVacancy(id);
        ra.addFlashAttribute("success_mess", "Вакансия удалена!");
        return "redirect:/admin/vacancies";
    }

    @GetMapping("/{id}/applications")
    public String vacancyApplications(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Vacancy vacancy = vacancyService.getVacancyById(id);
        if (vacancy == null) {
            ra.addFlashAttribute("error_mess", "Вакансия не найдена");
            return "redirect:/admin/vacancies";
        }
        model.addAttribute("vacancy", vacancy);
        model.addAttribute("applications", applicationService.getApplicationsByVacancy(id));
        return "vacancy-applications";
    }
}