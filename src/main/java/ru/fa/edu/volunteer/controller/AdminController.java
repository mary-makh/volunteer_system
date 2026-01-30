package ru.fa.edu.volunteer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fa.edu.volunteer.entity.Role;
import ru.fa.edu.volunteer.service.EventService;
import ru.fa.edu.volunteer.service.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Administrator')")
public class AdminController {

    private final UserService userService;
    private final EventService eventService;

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String setRole(@PathVariable String id, @RequestParam Role role, RedirectAttributes ra) {
        userService.updateRole(id, role);
        ra.addFlashAttribute("message", "Роль обновлена");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable String id, @RequestParam boolean block, RedirectAttributes ra) {
        userService.setEnabled(id, !block);
        ra.addFlashAttribute("message", block ? "Пользователь заблокирован" : "Пользователь разблокирован");
        return "redirect:/admin/users";
    }

    @GetMapping("/events/pending")
    public String pendingEvents(Model model) {
        model.addAttribute("events", eventService.findPendingApproval());
        return "admin/pending-events";
    }

    @PostMapping("/events/{id}/approve")
    public String approveEvent(@PathVariable String id, RedirectAttributes ra) {
        eventService.approveEvent(id);
        ra.addFlashAttribute("message", "Мероприятие утверждено");
        return "redirect:/admin/events/pending";
    }
}
