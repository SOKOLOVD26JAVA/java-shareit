package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;


import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT DISTINCT ir FROM ItemRequest ir " +
            "LEFT JOIN FETCH ir.items i " +
            "LEFT JOIN FETCH i.owner " +
            "WHERE ir.requester.id = :userId " +
            "ORDER BY ir.created DESC")
    List<ItemRequest> findItemRequestsWithItemsByUserID(@Param("userId") Long userId);
}
