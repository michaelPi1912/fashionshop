package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.ChangePasswordRequest;
import com.huuphucdang.fashionshop.model.payload.response.AddressResponse;
import com.huuphucdang.fashionshop.model.payload.response.UsersResponse;
import com.huuphucdang.fashionshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
        @RequestBody ChangePasswordRequest request,
        Principal connectedUser
    ){
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<UsersResponse> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){

        return ResponseEntity.ok(service.findAllUser(page, size));
    }
    @PutMapping("/block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void blockUser(@PathVariable("id") UUID id){
        service.blockUser(id);
    }
    @PutMapping("/un-block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void unBlockUser(@PathVariable("id") UUID id){
        service.unBlockUser(id);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("id") UUID id){
        service.deleteUser(id);
    }

}
