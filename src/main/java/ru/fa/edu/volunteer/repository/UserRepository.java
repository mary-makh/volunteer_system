package ru.fa.edu.volunteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fa.edu.volunteer.entity.Role;
import ru.fa.edu.volunteer.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRoleAndEnabledTrueOrderBySurname(Role role);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.enabled = true " +
            "AND (:search IS NULL OR :search = '' OR LOWER(u.surname) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY u.surname")
    List<User> findVolunteersBySearch(@Param("role") Role role, @Param("search") String search);
}
