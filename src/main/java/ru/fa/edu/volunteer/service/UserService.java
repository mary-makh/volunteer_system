package ru.fa.edu.volunteer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fa.edu.volunteer.entity.Role;
import ru.fa.edu.volunteer.entity.User;
import ru.fa.edu.volunteer.entity.VolunteerEvent;
import ru.fa.edu.volunteer.entity.VolunteerEventStatus;
import ru.fa.edu.volunteer.repository.UserRepository;
import ru.fa.edu.volunteer.repository.VolunteerEventRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VolunteerEventRepository volunteerEventRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User register(User user, String plainPassword) {
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAllVolunteers() {
        return userRepository.findByRoleAndEnabledTrueOrderBySurname(Role.Volunteer);
    }

    @Transactional(readOnly = true)
    public List<User> findVolunteersFiltered(String search, Integer minHours, Integer maxHours,
                                             Long minEvents, Integer minAge, Integer maxAge) {
        List<User> list = (search != null && !search.isBlank())
                ? userRepository.findVolunteersBySearch(Role.Volunteer, search.trim())
                : userRepository.findByRoleAndEnabledTrueOrderBySurname(Role.Volunteer);

        return list.stream()
                .filter(u -> matchHours(u, minHours, maxHours))
                .filter(u -> matchEventsCount(u, minEvents))
                .filter(u -> matchAge(u, minAge, maxAge))
                .collect(Collectors.toList());
    }

    private boolean matchHours(User u, Integer minHours, Integer maxHours) {
        if (minHours == null && maxHours == null) return true;
        int total = volunteerEventRepository.findByUserOrderByEventStartingDateDesc(u).stream()
                .filter(ve -> ve.getHours() != null)
                .mapToInt(VolunteerEvent::getHours)
                .sum();
        if (minHours != null && total < minHours) return false;
        if (maxHours != null && total > maxHours) return false;
        return true;
    }

    private boolean matchEventsCount(User u, Long minEvents) {
        if (minEvents == null) return true;
        long count = volunteerEventRepository.countByUserAndStatus(u, VolunteerEventStatus.Participated);
        return count >= minEvents;
    }

    private boolean matchAge(User u, Integer minAge, Integer maxAge) {
        if (u.getBirthDate() == null) return minAge == null && maxAge == null;
        if (minAge == null && maxAge == null) return true;
        int age = LocalDate.now().getYear() - u.getBirthDate().getYear();
        if (minAge != null && age < minAge) return false;
        if (maxAge != null && age > maxAge) return false;
        return true;
    }

    @Transactional(readOnly = true)
    public int getTotalHours(User user) {
        return volunteerEventRepository.findByUserOrderByEventStartingDateDesc(user).stream()
                .filter(ve -> ve.getHours() != null)
                .mapToInt(VolunteerEvent::getHours)
                .sum();
    }

    @Transactional(readOnly = true)
    public long getParticipatedEventsCount(User user) {
        return volunteerEventRepository.countByUserAndStatus(user, VolunteerEventStatus.Participated);
    }

    @Transactional
    public void updateRole(String userId, Role role) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setRole(role);
            userRepository.save(u);
        });
    }

    @Transactional
    public void setEnabled(String userId, boolean enabled) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setEnabled(enabled);
            userRepository.save(u);
        });
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
