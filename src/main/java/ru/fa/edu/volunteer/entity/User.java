package ru.fa.edu.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(nullable = false, length = 100)
    private String surname;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column(name = "passport_no")
    private Integer passportNo;

    @Column(name = "passport_series")
    private Integer passportSeries;

    @Column(name = "passport_issue_date")
    private LocalDate passportIssueDate;

    @Column(name = "passport_issuing_authority", length = 255)
    private String passportIssuingAuthority;

    @Column(name = "birth_certificate_no", length = 50)
    private String birthCertificateNo;

    @Column(name = "birth_certificate_issue_date")
    private LocalDate birthCertificateIssueDate;

    @Column(name = "birth_certificate_issuing_authority", length = 255)
    private String birthCertificateIssuingAuthority;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VolunteerEvent> volunteerEvents = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<Event> createdEvents;

    public String getFullName() {
        if (lastName != null && !lastName.isBlank()) {
            return surname + " " + name + " " + lastName;
        }
        return surname + " " + name;
    }
}
