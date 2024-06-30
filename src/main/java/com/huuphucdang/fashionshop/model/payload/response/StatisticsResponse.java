package com.huuphucdang.fashionshop.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {
    private Integer sale;
    private Integer orders;
    private Double avg;
    private List<Integer> saleDate;
    private List<Integer> saleDatePayPal;
    private List<String> dates;
}
