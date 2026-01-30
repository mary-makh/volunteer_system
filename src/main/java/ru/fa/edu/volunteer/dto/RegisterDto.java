package ru.fa.edu.volunteer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.fa.edu.volunteer.entity.Role;

import java.time.LocalDate;

@Data
public class RegisterDto {

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 100)
    private String surname;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String lastName;

    private LocalDate birthDate;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 4, message = "Пароль не менее 4 символов")
    private String password;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;

    private Integer phoneNumber;

    private Integer passportNo;
    private Integer passportSeries;
    private LocalDate passportIssueDate;
    private String passportIssuingAuthority;

    private String birthCertificateNo;
    private LocalDate birthCertificateIssueDate;
    private String birthCertificateIssuingAuthority;

    @NotNull(message = "Выберите роль")
    private Role role;
}
