package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    void createUserWhenUserIsValid() {
        UserDto userDtoToCreate = UserDto.builder()
                .email("email@email.com")
                .name("name")
                .build();

        when(userService.addUser(userDtoToCreate)).thenReturn(userDtoToCreate);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDtoToCreate), result);
    }

    /*@Test
    @SneakyThrows
    void createUserWheUserEmailIsNotValidShouldReturnBadRequest() {
        UserDto userDtoToCreate = UserDto.builder()
                .email("email.com")
                .name("name")
                .build();

        when(userService.addUser(userDtoToCreate)).thenReturn(userDtoToCreate);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToCreate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(userDtoToCreate);
    }*/

    /*@Test
    @SneakyThrows
    void createUserWheNameIsNotValidShouldReturnBadRequest() {
        UserDto userDtoToCreate = UserDto.builder()
                .email("email@email.com")
                .name("     ")
                .build();

        when(userService.addUser(userDtoToCreate)).thenReturn(userDtoToCreate);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToCreate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(userDtoToCreate);
    }*/

    @Test
    @SneakyThrows
    void updateUserWhenUserIsValid() {
        UserDto userDtoToUpdate = UserDto.builder()
                .id(1L)
                .email("update@update.com")
                .name("update")
                .build();
        UserDto userDtoToUpdateNew = UserDto.builder()
                .id(1L)
                .email("cc@update.com")
                .name("cc")
                .build();
        when(userService.updateUser(userDtoToUpdateNew)).thenReturn(userDtoToUpdateNew);
        Long userId = userDtoToUpdate.getId();
        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToUpdateNew)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDtoToUpdateNew), result);
    }

    /*@Test
    @SneakyThrows
    void updateUserWheUserEmailIsNotValidShouldReturnBadRequest() {
        UserDto userDtoToUpdate = UserDto.builder()
                .email("update.com")
                .name("update")
                .build();

        when(userService.updateUser(userDtoToUpdate)).thenReturn(userDtoToUpdate);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDtoToUpdate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(userDtoToUpdate);
    }*/

    @Test
    @SneakyThrows
    void get() {
        long userId = 2L;

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getUser(userId);
    }

    @Test
    @SneakyThrows
    void findAll() {
        List<UserDto> usersDtoToExpect = List.of(UserDto.builder().name("name").email("email@email.com").build());

        when(userService.getUsers()).thenReturn(usersDtoToExpect);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(usersDtoToExpect), result);
    }

    @Test
    @SneakyThrows
    void delete() {
        long userId = 0L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userId);
    }
}