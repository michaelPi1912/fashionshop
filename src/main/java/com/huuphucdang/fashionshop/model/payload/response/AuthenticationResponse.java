package com.huuphucdang.fashionshop.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huuphucdang.fashionshop.model.entity.User;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private User user;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
