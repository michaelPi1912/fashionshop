package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.payload.request.AuthenticationRequest;
import com.huuphucdang.fashionshop.config.JwtService;
import com.huuphucdang.fashionshop.model.entity.Token;
import com.huuphucdang.fashionshop.model.entity.TokenType;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.ChangePasswordRequest;
import com.huuphucdang.fashionshop.model.payload.request.RegisterRequest;
import com.huuphucdang.fashionshop.model.payload.response.AuthenticationResponse;
import com.huuphucdang.fashionshop.repository.TokenRepository;
import com.huuphucdang.fashionshop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User
                .builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .phone(request.getPhone())
                .gender(request.getGender())
                .build();
        var saveUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(saveUser, jwtToken);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user,jwtToken);
        return AuthenticationResponse
                .builder()
                .user(user)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    private void revokeAllUserTokens(User user){
        var validUserToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserToken.isEmpty()){
            return;
        }

        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserToken);
    }
    private void saveUserToken(User saveUser, String jwtToken) {
        var token = Token.builder()
                .user(saveUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)){
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public String getUserEmailByToken(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return null;
        }

        jwt = authHeader.substring(7);
        return jwtService.extractUsername(jwt);
    }

    //reset password
    public void changePasswordForgot(ChangePasswordRequest request, String email) {
        var user = (User) userRepository.findByEmail(email).orElseThrow();
//        make a reset password api
//        var jwtToken = jwtService.generateToken(user);
//        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user,jwtToken);
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
