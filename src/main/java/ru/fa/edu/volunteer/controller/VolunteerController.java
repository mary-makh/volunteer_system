package ru.fa.edu.volunteer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fa.edu.volunteer.entity.Role;
import ru.fa.edu.volunteer.security.SecurityUser;
import ru.fa.edu.volunteer.service.EventService;
import ru.fa.edu.volunteer.service.UserService;
import ru.fa.edu.volunteer.service.VolunteerEventService;

@Controller
@RequestMapping("/volunteer")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Volunteer') or hasRole('Administrator')")
public class VolunteerController {

    private final EventService eventService;
    private final VolunteerEventService volunteerEventService;
    private final UserService userService;

    @GetMapping("/my-events")
    public String myEvents(@AuthenticationPrincipal SecurityUser user, Model model) {
        var list = volunteerEventService.findByUser(user.getUser());
        model.addAttribute("participations", list);
        model.addAttribute("totalHours", userService.getTotalHours(user.getUser()));
        return "volunteer/my-events";
    }

    @PostMapping("/events/{eventId}/register")
    public String register(@PathVariable String eventId, @AuthenticationPrincipal SecurityUser user,
                           RedirectAttributes redirectAttributes) {
        if (user.getUser().getRole() != Role.Volunteer) {
            return "redirect:/events";
        }
        var event = eventService.findById(eventId);
        if (event.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Мероприятие не найдено");
            return "redirect:/events";
        }
        try {
            volunteerEventService.register(user.getUser(), event.get());
            redirectAttributes.addFlashAttribute("message", "Заявка подана на модерацию");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events/" + eventId;
    }
}
