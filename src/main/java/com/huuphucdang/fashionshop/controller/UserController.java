package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.ChangePasswordRequest;
import com.huuphucdang.fashionshop.model.payload.request.RegisterRequest;
import com.huuphucdang.fashionshop.model.payload.response.AddressResponse;
import com.huuphucdang.fashionshop.model.payload.response.UsersResponse;
import com.huuphucdang.fashionshop.service.AuthenticationService;
import com.huuphucdang.fashionshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final AuthenticationService authenticationService;

    @PutMapping("/change-password")
    public ResponseEntity<User> changePassword(
        @RequestBody ChangePasswordRequest request,
        @AuthenticationPrincipal User user
    ){

        return ResponseEntity.ok(service.changePassword(request, user));
    }

    @PutMapping("/update")
    public ResponseEntity<User> changeProfile(
            @RequestBody RegisterRequest body,
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(service.updateProfile(body, user));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<UsersResponse> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int active,
            @RequestParam(defaultValue = "0") int role
    ){

        return ResponseEntity.ok(service.findAllUser(page, size,  email, name, active, role));
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

    @PostMapping("/wish-list")
    public Product addProduct(
            @AuthenticationPrincipal User user,
            @RequestBody Product productRequest
    ){
        return service.addProduct(user.getId(), productRequest);
    }

    @DeleteMapping("/wish-list/{id}")
    public void deleteProduct(
            @AuthenticationPrincipal User user,
            @PathVariable("id") UUID id
    ){
        service.deleteProduct(user.getId(), id);
    }

    @GetMapping("/wish-list/check/{id}")
    public ResponseEntity<Product> checkWishList(
            @AuthenticationPrincipal User user,
            @PathVariable("id") UUID id
    ){
        return ResponseEntity.ok(service.checkWishList(user, id));
    }

}
