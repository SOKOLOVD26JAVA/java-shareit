package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COUNT(b) > 0 " +
            "FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.item.id = :itemId " +
            "AND b.status = 'APPROVED'" +
            "AND b.end < CURRENT_TIMESTAMP")
    boolean existsPastApprovedBooking(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
