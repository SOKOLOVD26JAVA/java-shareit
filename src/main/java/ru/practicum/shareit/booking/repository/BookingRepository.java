package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id IN :itemIds AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findByItemIdsAndStatus(@Param("itemIds") List<Long> itemIds, @Param("status") BookingStatus status);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "ORDER BY b.start DESC")
    List<Booking> findByItemIds(@Param("itemIds") List<Long> itemIds);

    List<Booking> findByItemIdAndStatus(Long itemId, BookingStatus status);
}
