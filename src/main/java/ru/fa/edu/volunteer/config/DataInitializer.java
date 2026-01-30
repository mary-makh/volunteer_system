package ru.fa.edu.volunteer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.fa.edu.volunteer.entity.Role;
import ru.fa.edu.volunteer.entity.User;
import ru.fa.edu.volunteer.repository.UserRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@volunteer.ru").isEmpty()) {
            User admin = User.builder()
                    .userId(UUID.randomUUID().toString())
                    .surname("Admin")
                    .name("Admin")
                    .email("admin@volunteer.ru")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.Administrator)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
        }
    }
}
