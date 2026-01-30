package ru.fa.edu.volunteer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fa.edu.volunteer.entity.Event;
import ru.fa.edu.volunteer.entity.User;
import ru.fa.edu.volunteer.entity.VolunteerEventStatus;
import ru.fa.edu.volunteer.repository.EventRepository;
import ru.fa.edu.volunteer.repository.VolunteerEventRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final VolunteerEventRepository volunteerEventRepository;

    @Transactional(readOnly = true)
    public Optional<Event> findById(String id) {
        return eventRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Event> findApprovedEvents() {
        return eventRepository.findByApprovedOrderByStartingDateDesc(true);
    }

    @Transactional(readOnly = true)
    public List<Event> findApprovedFiltered(String title, String location, String curatorName) {
        return eventRepository.findApprovedFiltered(
                title != null && !title.isBlank() ? title.trim() : null,
                location != null && !location.isBlank() ? location.trim() : null,
                curatorName != null && !curatorName.isBlank() ? curatorName.trim() : null
        );
    }

    @Transactional(readOnly = true)
    public List<Event> findPendingApproval() {
        return eventRepository.findByApprovedOrderByStartingDateDesc(false);
    }

    @Transactional
    public Event create(Event event, User createdBy) {
        event.setEventId(UUID.randomUUID().toString());
        event.setCreatedBy(createdBy);
        event.setApproved(false);
        event.setRegisteredVolunteers(0);
        event.setParticipatedVolunteers(0);
        return eventRepository.save(event);
    }

    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public void approveEvent(String eventId) {
        eventRepository.findById(eventId).ifPresent(e -> {
            e.setApproved(true);
            eventRepository.save(e);
        });
    }

    @Transactional
    public void updateRegisteredCount(Event event) {
        int count = event.getVolunteerEvents().size();
        event.setRegisteredVolunteers(count);
        eventRepository.save(event);
    }

    @Transactional
    public void updateParticipatedCount(Event event) {
        Event e = eventRepository.findById(event.getEventId()).orElseThrow();
        long count = volunteerEventRepository.countByEventAndStatus(e, VolunteerEventStatus.Participated);
        e.setParticipatedVolunteers((int) count);
        eventRepository.save(e);
    }
}
