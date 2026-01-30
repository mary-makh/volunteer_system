package ru.fa.edu.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @Column(name = "event_id", length = 36)
    private String eventId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "starting_date", nullable = false)
    private LocalDate startingDate;

    @Column(name = "ending_date", nullable = false)
    private LocalDate endingDate;

    @Column(length = 255)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    private User createdBy;

    @Column(name = "registered_volunteers")
    @Builder.Default
    private Integer registeredVolunteers = 0;

    @Column(name = "participated_volunteers")
    @Builder.Default
    private Integer participatedVolunteers = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean approved = false;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VolunteerEvent> volunteerEvents = new ArrayList<>();
}
