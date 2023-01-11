package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void findByText() {
        User user = new User(null, "userName", "email@mail");
        Item item = new Item();
        item.setName("itemName");
        item.setDescription("itemDescription");
        item.setAvailable(true);
        item.setOwner(user);

        em.persist(user);
        em.persist(item);

        List<Item> items = itemRepository.findByText("item", null);

        Assertions.assertEquals("itemName", items.get(0).getName());

    }
}