package ru.fa.edu.volunteer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.fa.edu.volunteer.dto.RegisterDto;
import ru.fa.edu.volunteer.entity.User;
import ru.fa.edu.volunteer.service.UserService;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("roles", ru.fa.edu.volunteer.entity.Role.values());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", ru.fa.edu.volunteer.entity.Role.values());
            return "register";
        }
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            result.rejectValue("email", "error.email", "Email уже зарегистрирован");
            model.addAttribute("roles", ru.fa.edu.volunteer.entity.Role.values());
            return "register";
        }
        User user = User.builder()
                .surname(dto.getSurname())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .passportNo(dto.getPassportNo())
                .passportSeries(dto.getPassportSeries())
                .passportIssueDate(dto.getPassportIssueDate())
                .passportIssuingAuthority(dto.getPassportIssuingAuthority())
                .birthCertificateNo(dto.getBirthCertificateNo())
                .birthCertificateIssueDate(dto.getBirthCertificateIssueDate())
                .birthCertificateIssuingAuthority(dto.getBirthCertificateIssuingAuthority())
                .role(dto.getRole())
                .build();
        userService.register(user, dto.getPassword());
        return "redirect:/login?registered";
    }
}
