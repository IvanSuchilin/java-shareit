package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

   List<Item> findItemByOwnerId(Long userId, Pageable pageable);
   List<Item> findItemByOwnerId(Long userId);
    @Query(value = "select it from Item it where lower(it.description) like lower(concat('%',?1, '%')) and it.available is true " +
            "or lower(it.name) like lower(concat('%',?1, '%')) and it.available is true")
    List<Item> findByText(String text, Pageable pageable);
}
