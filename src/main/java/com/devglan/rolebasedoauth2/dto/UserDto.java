package com.devglan.rolebasedoauth2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private List<String> role;

}
