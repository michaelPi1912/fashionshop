package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.ChangePasswordRequest;
import com.huuphucdang.fashionshop.repository.TokenRepository;
import com.huuphucdang.fashionshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User)((UsernamePasswordAuthenticationToken) connectedUser).getCredentials();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IllegalStateException("Wrong Password");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        tokenRepository.deleteAllTokenByUser(id);
        userRepository.deleteById(id);
    }

    public List<User> findAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }
}
