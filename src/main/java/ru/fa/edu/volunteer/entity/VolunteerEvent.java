package ru.fa.edu.volunteer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "volunteer_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private VolunteerEventStatus status = VolunteerEventStatus.Registered;

    @Column(name = "hours")
    private Integer hours;
}
