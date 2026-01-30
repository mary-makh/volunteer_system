package ru.fa.edu.volunteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fa.edu.volunteer.entity.Event;
import ru.fa.edu.volunteer.entity.User;
import ru.fa.edu.volunteer.entity.VolunteerEvent;
import ru.fa.edu.volunteer.entity.VolunteerEventStatus;

import java.util.List;
import java.util.Optional;

public interface VolunteerEventRepository extends JpaRepository<VolunteerEvent, Long> {

    List<VolunteerEvent> findByUserOrderByEventStartingDateDesc(User user);

    List<VolunteerEvent> findByEvent(Event event);

    Optional<VolunteerEvent> findByUserAndEvent(User user, Event event);

    boolean existsByUserAndEvent(User user, Event event);

    long countByUserAndStatus(User user, VolunteerEventStatus status);

    List<VolunteerEvent> findByEventAndStatus(Event event, VolunteerEventStatus status);

    long countByEventAndStatus(Event event, VolunteerEventStatus status);
}
