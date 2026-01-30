package ru.fa.edu.volunteer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fa.edu.volunteer.entity.Event;
import ru.fa.edu.volunteer.entity.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByApprovedOrderByStartingDateDesc(Boolean approved);

    List<Event> findByCreatedBy(User createdBy);

    @Query("SELECT e FROM Event e WHERE e.approved = true " +
            "AND (:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:curatorName IS NULL OR LOWER(e.createdBy.surname) LIKE LOWER(CONCAT('%', :curatorName, '%')) " +
            "OR LOWER(e.createdBy.name) LIKE LOWER(CONCAT('%', :curatorName, '%'))) " +
            "ORDER BY e.startingDate DESC")
    List<Event> findApprovedFiltered(@Param("title") String title,
                                     @Param("location") String location,
                                     @Param("curatorName") String curatorName);
}
