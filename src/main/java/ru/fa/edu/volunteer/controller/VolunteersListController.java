package ru.fa.edu.volunteer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fa.edu.volunteer.service.UserService;
import ru.fa.edu.volunteer.service.VolunteerEventService;

import java.util.List;

@Controller
@RequestMapping("/volunteers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Coordinator') or hasRole('Administrator')")
public class VolunteersListController {

    private final UserService userService;
    private final VolunteerEventService volunteerEventService;

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) Integer minHours,
                       @RequestParam(required = false) Integer maxHours,
                       @RequestParam(required = false) Long minEvents,
                       @RequestParam(required = false) Integer minAge,
                       @RequestParam(required = false) Integer maxAge,
                       Model model) {
        List<?> volunteers = (search != null || minHours != null || maxHours != null || minEvents != null || minAge != null || maxAge != null)
                ? userService.findVolunteersFiltered(search, minHours, maxHours, minEvents, minAge, maxAge)
                : userService.findAllVolunteers();
        model.addAttribute("volunteers", volunteers);
        model.addAttribute("search", search);
        model.addAttribute("minHours", minHours);
        model.addAttribute("maxHours", maxHours);
        model.addAttribute("minEvents", minEvents);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);
        return "volunteers/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable String id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("volunteer", user);
                    model.addAttribute("totalHours", userService.getTotalHours(user));
                    model.addAttribute("participatedCount", userService.getParticipatedEventsCount(user));
                    model.addAttribute("participations", volunteerEventService.findByUser(user));
                    return "volunteers/view";
                })
                .orElse("redirect:/volunteers");
    }
}
