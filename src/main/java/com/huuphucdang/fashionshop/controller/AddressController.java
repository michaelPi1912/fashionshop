package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.AddressRequest;
import com.huuphucdang.fashionshop.model.payload.response.AddressResponse;
import com.huuphucdang.fashionshop.service.AddressService;
import com.huuphucdang.fashionshop.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;
    private final AuthenticationService auth;
    @PostMapping("/insert")
    public void saveAddress(
            @RequestBody AddressRequest body,
            @AuthenticationPrincipal User user
    ){
        service.saveAddress(body, user);
    }

    @GetMapping("/all")
    public ResponseEntity<AddressResponse> getAddressByUser(HttpServletRequest request){

        return ResponseEntity.ok(service.findAllByUser(auth.getUserEmailByToken(request)));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAddress(@PathVariable("id") UUID id){
        service.deleteAddress(id);
    }
}
