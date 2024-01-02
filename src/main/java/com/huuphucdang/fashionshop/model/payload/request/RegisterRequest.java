package com.huuphucdang.fashionshop.model.payload.request;

import com.huuphucdang.fashionshop.model.entity.Role;
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
