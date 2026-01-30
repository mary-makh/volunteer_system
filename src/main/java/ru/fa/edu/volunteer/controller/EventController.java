package ru.fa.edu.volunteer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fa.edu.volunteer.entity.Role;
import ru.fa.edu.volunteer.security.SecurityUser;
import ru.fa.edu.volunteer.service.EventService;
import ru.fa.edu.volunteer.service.VolunteerEventService;

import java.util.List;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final VolunteerEventService volunteerEventService;

    @GetMapping
    public String list(@AuthenticationPrincipal SecurityUser user,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) String location,
                       @RequestParam(required = false) String curator,
                       Model model) {
        List<?> events;
        if (user != null && (user.getUser().getRole() == Role.Administrator || user.getUser().getRole() == Role.Coordinator)) {
            events = (title != null || location != null || curator != null)
                    ? eventService.findApprovedFiltered(title, location, curator)
                    : eventService.findApprovedEvents();
        } else {
            events = (title != null || location != null || curator != null)
                    ? eventService.findApprovedFiltered(title, location, curator)
                    : eventService.findApprovedEvents();
        }
        model.addAttribute("events", events);
        model.addAttribute("titleFilter", title);
        model.addAttribute("locationFilter", location);
        model.addAttribute("curatorFilter", curator);
        return "events/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable String id, @AuthenticationPrincipal SecurityUser user, Model model) {
        return eventService.findById(id)
                .map(event -> {
                    model.addAttribute("event", event);
                    boolean canRegister = user != null && user.getUser().getRole() == Role.Volunteer;
                    if (canRegister) {
                        model.addAttribute("alreadyRegistered",
                                volunteerEventService.findByUserAndEvent(user.getUser(), event).isPresent());
                    }
                    return "events/view";
                })
                .orElse("redirect:/events");
    }
}
