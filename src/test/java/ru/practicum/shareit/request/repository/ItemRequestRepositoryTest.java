package ru.practicum.shareit.request.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    /*@Test
    void findAllWithPagination() {
        User user = new User(null, "userName", "email@mail");
        User user2 = new User(null, "user2Name", "email2@mail");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setDescription("text");
        itemRequest.setCreated(LocalDateTime.now());

        em.persist(user);
        em.persist(user2);
        em.persist(itemRequest);

        List<ItemRequest> requests = itemRequestRepository.findAllWithPagination(null, 2L);
        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals("text", requests.get(0).getDescription());
    }*/
}