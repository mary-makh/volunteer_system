package ru.fa.edu.volunteer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fa.edu.volunteer.entity.Event;
import ru.fa.edu.volunteer.entity.User;
import ru.fa.edu.volunteer.entity.VolunteerEvent;
import ru.fa.edu.volunteer.entity.VolunteerEventStatus;
import ru.fa.edu.volunteer.repository.VolunteerEventRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerEventService {

    private final VolunteerEventRepository volunteerEventRepository;
    private final EventService eventService;

    @Transactional(readOnly = true)
    public List<VolunteerEvent> findByUser(User user) {
        return volunteerEventRepository.findByUserOrderByEventStartingDateDesc(user);
    }

    @Transactional(readOnly = true)
    public List<VolunteerEvent> findByEvent(Event event) {
        return volunteerEventRepository.findByEvent(event);
    }

    @Transactional(readOnly = true)
    public Optional<VolunteerEvent> findById(Long id) {
        return volunteerEventRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<VolunteerEvent> findByUserAndEvent(User user, Event event) {
        return volunteerEventRepository.findByUserAndEvent(user, event);
    }

    @Transactional
    public VolunteerEvent register(User user, Event event) {
        if (volunteerEventRepository.existsByUserAndEvent(user, event)) {
            throw new IllegalStateException("Вы уже зарегистрированы на это мероприятие");
        }
        VolunteerEvent ve = VolunteerEvent.builder()
                .user(user)
                .event(event)
                .status(VolunteerEventStatus.On_moderation)
                .build();
        ve = volunteerEventRepository.save(ve);
        eventService.updateRegisteredCount(event);
        return ve;
    }

    @Transactional
    public void updateStatus(Long id, VolunteerEventStatus status) {
        volunteerEventRepository.findById(id).ifPresent(ve -> {
            ve.setStatus(status);
            volunteerEventRepository.save(ve);
            eventService.updateParticipatedCount(ve.getEvent());
        });
    }

    @Transactional
    public void setHours(Long id, Integer hours) {
        volunteerEventRepository.findById(id).ifPresent(ve -> {
            ve.setHours(hours != null ? hours : 0);
            volunteerEventRepository.save(ve);
        });
    }
}
