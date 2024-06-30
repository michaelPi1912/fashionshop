package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Order;
import com.huuphucdang.fashionshop.model.payload.response.StatisticsResponse;
import com.huuphucdang.fashionshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;
    public StatisticsResponse getStatistics(String start, String end) {
        try{
            int sales =  0;
            int orders = 0;
            double avg = 0;
            List<Integer> saleDate = new ArrayList<>();
            List<Integer> saleDate2 = new ArrayList<>();
            List<String> dates = new ArrayList<>();
            List<Order> orderList;
            if(!start.isEmpty() && !end.isEmpty()){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date1 = format.parse(start+" 00:00:00");
                Date date2 = format.parse(end + " 23:59:59");
                Date temp = date1;
                int step =0;
                int step2 =1;
                System.out.println(date1.toString() + date2.toString());
//                spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), date1,date2));

                while (temp.compareTo(date2) <0) {
                    Specification<Order> spec = Specification.where(null);

                    String date  = LocalDate.parse(start).plusDays(step++).toString();
                    dates.add(date.substring(5,7)+"/"+date.substring(8,10));

                    Date first = format.parse(date+" 00:00:00");
                    Date last = format.parse(date+" 23:59:59");
                    spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), first,last));

                    orderList = orderRepository.findAll(spec);
                    int sale = 0;
                    int salePaypal =0;
                    for(Order order: orderList){
                        if(order.getOrderStatus().getStatus() != "Cancelled" ||  order.getOrderStatus().getStatus() != "Declined"||order.getOrderStatus().getStatus() != "Refunded"||order.getOrderStatus().getStatus() != "Attempted delivery"){
                            orders += 1;
                            sales += order.getOrderTotal();
                            if(order.getPaymentType().equals("COD")){
                                sale += order.getOrderTotal();
                            }else{
                                salePaypal += order.getOrderTotal();
                            }
                        }
                    }
                    saleDate.add(sale);
                    saleDate2.add(salePaypal);
                    temp = last;
                };
            }
//            List<Order> orderList = orderRepository.findAll(spec);
//            System.out.println(orderList.size());
////            int sale = 0;
//            for(Order order: orderList){
//                if(order.getOrderStatus().getStatus() != "Cancelled" ||  order.getOrderStatus().getStatus() != "Declined"||order.getOrderStatus().getStatus() != "Refunded"||order.getOrderStatus().getStatus() != "Attempted delivery"){
//                    orders += 1;
//                    sales += order.getOrderTotal();
////                    sale += order.getOrderTotal();
//                }
//            }
            if(orders !=0){
                avg = (sales/(1000*orders))*1000;

            }

            return StatisticsResponse.builder()
                    .sale(sales)
                    .avg(avg)
                    .orders(orders)
                    .dates(dates)
                    .saleDate(saleDate)
                    .saleDatePayPal(saleDate2)
                    .build();

        }catch (Exception e){
            return null;

        }
    }
}
