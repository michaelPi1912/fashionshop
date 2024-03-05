package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.CartItem;
import com.huuphucdang.fashionshop.model.entity.Order;
import com.huuphucdang.fashionshop.model.entity.OrderLine;
import com.huuphucdang.fashionshop.model.payload.request.OrderLineRequest;
import com.huuphucdang.fashionshop.repository.CartItemRepository;
import com.huuphucdang.fashionshop.repository.CartRepository;
import com.huuphucdang.fashionshop.repository.OrderLineRepository;
import com.huuphucdang.fashionshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderLine saveOrderLine(OrderLineRequest body) {
        OrderLine orderLine = new OrderLine();
        CartItem item = cartItemRepository.findById(body.getCartItemId()).orElseThrow();
        Order order = orderRepository.findById(body.getOrderId()).orElseThrow();
        orderLine.setOrder(order);
        orderLine.setProduct(item.getProduct());
        orderLine.setQuantity(item.getQuantity());
        orderLine.setPrice(item.getQuantity()*item.getProduct().getPrice());
        return orderLineRepository.save(orderLine);
    }

    public List<OrderLine> getAllByOrderId(UUID orderId) {
        List<OrderLine> orderLines = orderLineRepository.findAllByOrderId(orderId);
        return orderLines;
    }

    public void deleteOrderLine(UUID id) {
        orderLineRepository.deleteById(id);
    }
    //
    public OrderLine updateOrderLine(OrderLineRequest body, UUID id) {
        OrderLine orderLine = orderLineRepository.findById(id).orElseThrow();
        CartItem item = cartItemRepository.findById(body.getCartItemId()).orElseThrow();
        orderLine.setQuantity(body.getQuantity());
        orderLine.setPrice(body.getQuantity()*item.getProduct().getPrice());
        return orderLineRepository.save(orderLine);
    }
}
