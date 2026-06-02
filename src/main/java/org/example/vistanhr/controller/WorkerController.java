package org.example.vistanhr.controller;

import org.example.vistanhr.model.Worker;
import org.example.vistanhr.repository.WorkerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/workers")
public class WorkerController {

    private final WorkerRepository workerRepository;

    public WorkerController(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    // 1. Открытие страницы назначения задачи
    @GetMapping("/assign-task/{id}")
    public String showAssignTaskForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Worker worker = workerRepository.findById(id).orElse(null);

        if (worker == null) {
            redirectAttributes.addFlashAttribute("error", "Рабочий с ID " + id + " не найден.");
            return "redirect:/workers"; // Или на ту страницу, где у тебя список рабочих
        }

        model.addAttribute("worker", worker);
        return "assign-task"; // Имя твоего HTML-файла
    }

    // 2. Обработка отправки формы назначения задачи
    @PostMapping("/assign-task/{id}")
    public String handleAssignTask(@PathVariable Long id,
                                   @RequestParam("task") String taskDescription,
                                   RedirectAttributes redirectAttributes) {

        Worker worker = workerRepository.findById(id).orElse(null);

        if (worker == null) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Рабочий не найден.");
            return "redirect:/workers";
        }

        if (taskDescription == null || taskDescription.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Описание задачи не может быть пустым.");
            return "redirect:/workers/assign-task/" + id;
        }

        // Добавляем новую задачу в список рабочего
        worker.getAssignedTasks().add(taskDescription.trim());

        // Сохраняем обновленного рабочего вместе с его коллекцией задач
        workerRepository.save(worker);

        redirectAttributes.addFlashAttribute("success", "Задача успешно назначена рабочему " + worker.getFullName());

        // Перенаправляем обратно на форму, чтобы сразу увидеть обновленный список задач
        return "redirect:/workers/assign-task/" + id;
    }
}