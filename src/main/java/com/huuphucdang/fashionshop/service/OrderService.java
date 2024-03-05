package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.OrderRequest;
import com.huuphucdang.fashionshop.model.payload.response.OrderResponse;
import com.huuphucdang.fashionshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private final AddressRepository addressRepository;
    private final OrderStatusRepository orderStatusRepository;
    public OrderResponse saveOrder(User user, OrderRequest body) {
        Order order = new Order();
        Date date = new Date();
        //set date
        order.setOrderDate(date);
        //set user
        order.setUser(user);
        //set payment
        PaymentType paymentType = paymentMethodRepository.findById(body.getPaymentId()).orElseThrow();
        order.setPayment(paymentType);
        //set shipping
        ShippingMethod shippingMethod = shippingMethodRepository.findById(body.getShippingId()).orElseThrow();
        order.setShippingMethod(shippingMethod);
        //set address
        Address address = addressRepository.findById(body.getAddressId()).orElseThrow();
        order.setAddress(address);
        //set status
        OrderStatus status = orderStatusRepository.findById(1).orElseThrow();
        order.setOrderStatus(status);
        order.setOrderTotal(0);
        orderRepository.save(order);
        return OrderResponse.builder().order(order).build();
    }

    public List<Order> getOrderByUser(User user) {
        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        return orderList;
    }

    public List<Order> getAll() {
        List<Order> orderList = orderRepository.findAll();
        return orderList;
    }

    public Order updateOrder(OrderRequest body, UUID id) {
        Order order = orderRepository.findById(id).orElseThrow();
        OrderStatus status = orderStatusRepository.findById(body.getStatusId()).orElseThrow();
        order.setOrderStatus(status);

        return orderRepository.save(order);
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }
}
