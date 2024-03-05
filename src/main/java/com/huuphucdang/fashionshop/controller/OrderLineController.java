package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.OrderLine;
import com.huuphucdang.fashionshop.model.payload.request.CartItemRequest;
import com.huuphucdang.fashionshop.model.payload.request.OrderLineRequest;
import com.huuphucdang.fashionshop.service.CartItemService;
import com.huuphucdang.fashionshop.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orderLine")
@RequiredArgsConstructor
public class OrderLineController {
    private final OrderLineService service;

    @PostMapping("/insert")
    public void saveOrderLine(
            @RequestBody OrderLineRequest body
    ){
        service.saveOrderLine(body);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderLine>> getItemByCart(@PathVariable("orderId") UUID orderId){
        return ResponseEntity.ok(service.getAllByOrderId(orderId));
    }

    @PutMapping("/update/{id}")
    public void updateAddress(
            @RequestBody OrderLineRequest body,
            @PathVariable("id") UUID id
    ){
        service.updateOrderLine(body, id);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteOrderLine(@PathVariable("id") UUID id){
        service.deleteOrderLine(id);
    }
}
