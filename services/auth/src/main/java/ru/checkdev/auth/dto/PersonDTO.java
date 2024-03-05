package ru.checkdev.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String email;
    private String username;
    private String password;
    private boolean privacy;
    private Calendar created;
    private Long chatId;
}