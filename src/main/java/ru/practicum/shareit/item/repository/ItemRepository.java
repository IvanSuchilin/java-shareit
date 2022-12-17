package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository <Item, Long> {
    Collection<Item> findItemByOwnerId(Long userId);

    /*Collection<Item> findItemsByDescriptionIsContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue
            (String description, String name);*/
@Query(value = "select it from Item it where lower(it.description) like lower(concat('%',?1, '%')) and it.available is true " +
        "or lower(it.name) like lower(concat('%',?1, '%')) and it.available is true")
    Collection<Item> findByText(String text);
}
