package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.payload.request.AuthenticationRequest;
import com.huuphucdang.fashionshop.model.payload.request.ChangePasswordRequest;
import com.huuphucdang.fashionshop.model.payload.request.RegisterRequest;
import com.huuphucdang.fashionshop.model.payload.response.AuthenticationResponse;
import com.huuphucdang.fashionshop.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
    //send Link to mail
    //chua test
    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<?> changePasswordForgot(
            @RequestBody ChangePasswordRequest request,
            @PathVariable("email") String email
    ){
        service.changePasswordForgot(request, email);
        return ResponseEntity.ok().build();
    }
}
