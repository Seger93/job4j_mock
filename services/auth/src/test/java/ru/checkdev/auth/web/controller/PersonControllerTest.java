package ru.checkdev.auth.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.PersonDTO;
import ru.checkdev.auth.service.PersonService;
import ru.checkdev.auth.service.RoleService;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @InjectMocks
    private PersonController personController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder encoding;

    Calendar created = new Calendar.Builder()
            .setDate(2023, 10, 23)
            .setTimeOfDay(20, 20, 20)
            .build();
    private final PersonDTO person = new PersonDTO("email@mail.ru", "www", "password", true, created, 1L);


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

    @Test
    @WithMockUser
    public void whenGetPersonByChatIdNotFoundThenReturnStatusTwoHundred() throws Exception {
        when(personService.findByChatId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/person/telegram/{chatId}", 12345L))
                .andExpect(status().isNotFound());
    }

    /**
    @Test
    @WithMockUser
    public void testForgotPasswordByChatIdReturnOk() throws Exception {
        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("chatId", person.getChatId());

        Map<String, String> response = new HashMap<>();
        response.put("password", person.getChatId().toString());

        when(personService.updatePassword(person.getChatId())).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders.put("/person/updatePasswordTg")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(personService).updatePassword(person.getChatId());
    }

    @Test
    @WithMockUser
    public void testUpdatePasswordBadRequest() throws Exception {
        Long chatId = null;
        mockMvc.perform(put("/person/updatePasswordTg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(chatId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePasswordNoContent() throws Exception {
        Long chatId = 54321L;
        given(personService.updatePassword(chatId)).willReturn(Optional.empty());
        mockMvc.perform(put("/person/updatePasswordTg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(chatId)))
                .andExpect(status().isNoContent());
    }
    */
}