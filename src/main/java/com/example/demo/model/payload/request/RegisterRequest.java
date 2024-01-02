package com.example.demo.model.payload.request;

import com.example.demo.model.entity.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;

    private String lastname;

    private String email;
    private String password;

    private Role role;
}
