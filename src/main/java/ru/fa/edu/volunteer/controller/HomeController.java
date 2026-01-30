package ru.fa.edu.volunteer.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.fa.edu.volunteer.security.SecurityUser;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal SecurityUser user, Model model) {
        if (user != null) {
            model.addAttribute("role", user.getUser().getRole().name());
        }
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
