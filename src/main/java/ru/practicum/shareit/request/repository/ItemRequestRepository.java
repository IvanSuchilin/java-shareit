package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(Long userId);

    @Query(value = "select r from ItemRequest r where r.requester.id <> :userId order by r.created desc")
    List<ItemRequest> findAllWithPagination(Pageable pageable, @Param("userId") Long userId);
}
