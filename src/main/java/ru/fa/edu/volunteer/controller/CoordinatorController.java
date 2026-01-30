package ru.fa.edu.volunteer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fa.edu.volunteer.dto.EventDto;
import ru.fa.edu.volunteer.entity.Event;
import ru.fa.edu.volunteer.entity.VolunteerEventStatus;
import ru.fa.edu.volunteer.security.SecurityUser;
import ru.fa.edu.volunteer.service.EventService;
import ru.fa.edu.volunteer.service.VolunteerEventService;

@Controller
@RequestMapping("/coordinator")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Coordinator') or hasRole('Administrator')")
public class CoordinatorController {

    private final EventService eventService;
    private final VolunteerEventService volunteerEventService;

    @GetMapping("/events/new")
    public String newEventForm(Model model) {
        model.addAttribute("eventDto", new EventDto());
        return "coordinator/event-form";
    }

    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute EventDto dto, BindingResult result,
                              @AuthenticationPrincipal SecurityUser user,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "coordinator/event-form";
        }
        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startingDate(dto.getStartingDate())
                .endingDate(dto.getEndingDate())
                .location(dto.getLocation())
                .build();
        eventService.create(event, user.getUser());
        redirectAttributes.addFlashAttribute("message", "Мероприятие создано и ожидает утверждения администратором");
        return "redirect:/coordinator/events";
    }

    @GetMapping("/events")
    public String myEvents(@AuthenticationPrincipal SecurityUser user, Model model) {
        model.addAttribute("events", eventService.findApprovedEvents());
        return "coordinator/events";
    }

    @GetMapping("/events/{id}/responses")
    public String responses(@PathVariable String id, Model model) {
        return eventService.findById(id)
                .map(event -> {
                    model.addAttribute("event", event);
                    model.addAttribute("responses", volunteerEventService.findByEvent(event));
                    return "coordinator/responses";
                })
                .orElse("redirect:/coordinator/events");
    }

    @PostMapping("/events/{eventId}/responses/{responseId}/status")
    public String setStatus(@PathVariable String eventId, @PathVariable Long responseId,
                            @RequestParam VolunteerEventStatus status,
                            RedirectAttributes redirectAttributes) {
        volunteerEventService.updateStatus(responseId, status);
        redirectAttributes.addFlashAttribute("message", "Статус обновлён");
        return "redirect:/coordinator/events/" + eventId + "/responses";
    }

    @PostMapping("/events/{eventId}/responses/{responseId}/hours")
    public String setHours(@PathVariable String eventId, @PathVariable Long responseId,
                           @RequestParam(required = false) Integer hours,
                           RedirectAttributes redirectAttributes) {
        volunteerEventService.setHours(responseId, hours != null ? hours : 0);
        redirectAttributes.addFlashAttribute("message", "Часы сохранены");
        return "redirect:/coordinator/events/" + eventId + "/responses";
    }
}
