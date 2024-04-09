package ru.checkdev.auth.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.checkdev.auth.dto.PersonDTO;
import ru.checkdev.auth.service.PersonService;
import ru.checkdev.auth.service.RoleService;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @MockBean
    PersonService personService;
    @MockBean
    private RoleService roles;
    @Autowired
    private MockMvc mockMvc;

    private PersonController personController;

    Calendar created = new Calendar.Builder()
            .setDate(2023, 10, 23)
            .setTimeOfDay(20, 20, 20)
            .build();
    private final PersonDTO person = new PersonDTO("email@mail.ru", "www", "password", true, created, 1L);

    @Before
    public void initController() {
        this.personController = new PersonController(personService, roles);
    }

    @Test
    @WithMockUser
    public void whenGetPersonByChatIdThenReturnStatusOK() throws Exception {
        when(personService.findByChatId(person.getChatId())).thenReturn(Optional.of(person));
        mockMvc.perform(get("/person/telegram/{chatId}", person.getChatId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(person.getEmail()))
                .andExpect(jsonPath("$.password").value(person.getPassword()))
                .andExpect(jsonPath("$.username").value(person.getUsername()))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void whenGetPersonByChatIdNotFoundThenReturnStatusNotFound() throws Exception {
        when(personService.findByChatId(person.getChatId())).thenReturn(Optional.empty());
        mockMvc.perform(get("/person/telegram/{chatId}", anyInt()))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

/**
 @Test
 @WithMockUser public void testForgotPasswordByChatIdReturnOk() throws Exception {
 Map<String, String> response = new HashMap<>();
 response.put("password", person.getChatId().toString());
 when(personService.updatePassword(person.getChatId())).thenReturn(Optional.of(response));
 mockMvc.perform(MockMvcRequestBuilders.put("/person/updatePasswordTg")
 .content(new ObjectMapper().writeValueAsString(person.getChatId()))
 .contentType(MediaType.APPLICATION_JSON)
 .content(String.valueOf(person.getChatId())))
 .andExpect(status().isOk());
 verify(personService).updatePassword(person.getChatId());
 }
 */
}