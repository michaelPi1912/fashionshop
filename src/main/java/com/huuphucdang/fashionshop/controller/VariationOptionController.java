package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.entity.Promotion;
import com.huuphucdang.fashionshop.model.entity.VariationOption;
import com.huuphucdang.fashionshop.model.payload.request.PromotionRequest;
import com.huuphucdang.fashionshop.model.payload.request.VariationOptionRequest;
import com.huuphucdang.fashionshop.model.payload.response.CategoryResponse;
import com.huuphucdang.fashionshop.model.payload.response.OptionResponse;
import com.huuphucdang.fashionshop.model.payload.response.PromotionResponse;
import com.huuphucdang.fashionshop.service.VariationOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/option")
@RequiredArgsConstructor
public class VariationOptionController {

    private final VariationOptionService service;
    @PostMapping("/insert")
    public void savePromotion(
            @RequestBody VariationOptionRequest body
    ){
        service.saveVariationOption(body);
    }

    @GetMapping("/{variationId}/options")
    public ResponseEntity<OptionResponse> getAllOptionByVariationId(@PathVariable(value = "variationId") UUID variationId) {

        return ResponseEntity.ok(service.findOptionsByVariation(variationId));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<VariationOption> updateOption(@PathVariable("id") UUID id, @RequestBody VariationOptionRequest body) {

        return ResponseEntity.ok(service.updateOption(body, id));
    }


    @DeleteMapping("delete/{id}")
    public void deletePromotion(@PathVariable("id") UUID id) {
        service.deleteOption(id);
    }
}
