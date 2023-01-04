package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreatingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {
    private ItemDto itemDto;
    private CommentDto commentDto;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("nameDto");
        itemDto.setDescription("descriptionDto");
        commentDto = new CommentDto(1L, "commentText", "userName", LocalDateTime.now());
    }

    @SneakyThrows
    @Test
    void createItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("description");
        itemDto.setName("name");
        ItemCreatingDto itemCreatingDto =
                new ItemCreatingDto(1L, "name", "description", true, 1L);
        when(itemService.create(anyLong(), any()))
                .thenReturn(itemCreatingDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemCreatingDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name"));
    }

    @SneakyThrows
    @Test
    void getItemTest() {
        when(userService.getUserById(1L)).thenReturn(new UserDto());
        when(itemService.getItemById(1L, 1L)).thenReturn(itemDto);
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("descriptionDto"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("nameDto"));
    }
    @SneakyThrows
    @Test
    void patchTest() {
        when(userService.getUserById(1L)).thenReturn(new UserDto());
        when(itemService.getItemById(1L, 1L)).thenReturn(itemDto);
        when(itemService.update(anyLong(), anyLong(), any())).thenReturn(itemDto);
        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("descriptionDto"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("nameDto"));
    }
    @SneakyThrows
    @Test
    void findAllUsersItems() {
        when(userService.getUserById(1L)).thenReturn(new UserDto());
        Collection<ItemDto> items = new ArrayList<>();
        items.add(itemDto);
        when(itemService.getAllUsersItems(anyLong(), any())).thenReturn(items);

        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is(itemDto.getDescription())));
    }
    @SneakyThrows
    @Test
    void searchItemTest() {
        when(userService.getUserById(1L)).thenReturn(new UserDto());
        Collection<ItemDto> items = new ArrayList<>();
        items.add(itemDto);
        when(itemService.searchItem(eq(itemDto.getName()), any())).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "nameDto"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is(itemDto.getDescription())));
    }

    @SneakyThrows
    @Test
    void createCommentTest() {
        when(userService.getUserById(1L)).thenReturn(new UserDto());
        when(itemService.createComment(anyLong(), anyLong(), any())).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName").value("userName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("commentText"));
    }
}