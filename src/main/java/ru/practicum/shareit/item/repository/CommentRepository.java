package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COUNT(b) > 0 " +
            "FROM Booking b " +
            "WHERE b.booker.id = :userId " +
            "AND b.item.id = :itemId " +
            "AND b.status = 'APPROVED'" +
            "AND b.end < CURRENT_TIMESTAMP")
    boolean existsPastApprovedBooking(@Param("userId") Long userId, @Param("itemId") Long itemId);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.item.id = :itemId")
    List<Comment> findByItemId(@Param("itemId") Long itemId);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.item.id " +
            "IN (SELECT i.id FROM Item i WHERE i.owner.id = :userId)")
    List<Comment> findByUserId(@Param("userId") Long userId);
}
