package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.payload.response.StatisticsResponse;
import com.huuphucdang.fashionshop.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService service;

    @GetMapping
    public StatisticsResponse getStatistics(
            @RequestParam(defaultValue = "") String start,
            @RequestParam(defaultValue = "") String end
    ){
        return service.getStatistics(start, end);
    }
}
