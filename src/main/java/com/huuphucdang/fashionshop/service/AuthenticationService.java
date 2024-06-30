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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final String FELink = "http://localhost:3000/changePassword?token=";

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
                .image("")
                .isActive(true)
                .build();
        var saveUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user,86400000);
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
        var jwtToken = jwtService.generateToken(user,86400000);
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
                var accessToken = jwtService.generateToken(user,86400000);
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
    public void changePasswordForgot(ChangePasswordRequest request, User user) {
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    public String sendEmail(String toEmail, String subject){
        try {
            var user = repository.findByEmail(toEmail).isPresent() ?repository.findByEmail(toEmail).orElseThrow() : null;
            if(user == null){
                return "Information provided doesn’t match our records";
            }
            var jwtToken = jwtService.generateToken(user,86400000/24);
            revokeAllUserTokens(user);
            saveUserToken(user,jwtToken);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("khugohyeah@gmail.com");
            message.setTo(toEmail);
            String body = "Hello,\n" +
                    "\n" +
                    "You recently requested to reset your password. Click on the link below to change your password.\n" +
                    "\n" +
                    FELink+jwtToken +
                    "\n" +
                    "If you did not make the request to reset your password or if you are in need of further assistance, please contact our Customer Service Team" +
                    "\n" +
                    "Thanks!";
            message.setText(body);
            message.setSubject(subject);

            mailSender.send(message);


        }catch (Exception e){
            System.out.println(e);
        }
        return "Email has been sent to your email address";
    }
}
