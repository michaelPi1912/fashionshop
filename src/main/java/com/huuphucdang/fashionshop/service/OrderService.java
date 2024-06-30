package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.OrderRequest;
import com.huuphucdang.fashionshop.model.payload.response.OrderResponse;
import com.huuphucdang.fashionshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ProductItemRepository productItemRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderLineService orderLineService;
    private final VariationOptionRepository variationOptionRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;

    public OrderResponse saveOrder(User user, OrderRequest body) {
        Order order = new Order();
        Date date = new Date();
        //set date
        order.setOrderDate(date);
        //set user
        order.setUser(user);
        //set payment
        order.setPaymentType(body.getPaymentType());
        //set shipping
        order.setShippingMethod(body.getShipmentMethod());
        // ship cost
        order.setShipCost(body.getShippingCost());
        //set address
        order.setAddress(body.getAddress());
        //set status
        OrderStatus status = orderStatusRepository.findById(1).orElseThrow();
        order.setOrderStatus(status);
        order.setOrderTotal(body.getTotal());
        order.setPhone(body.getPhone());
        String code = generateCode();
        while(orderRepository.checkCode(code).size() > 0){
            code = generateCode();
        }
        for (Coupon coupon : body.getCoupons()) {
            order.getCoupons().add(coupon.getCode());
            int newUsage = coupon.getLimitUsage() -1;
            coupon.setLimitUsage(newUsage);
            couponRepository.save(coupon);
        }
        order.setCode(code);
        orderRepository.save(order);
//set order line
        body.getItemRequests().forEach(orderLineRequest -> {
            OrderLine orderLine = new OrderLine();
            orderLine.setOrder(order);
            orderLine.setQuantity(orderLineRequest.getQuantity());
            ProductItem productItem = productItemRepository.findById(orderLineRequest.getItemId()).orElseThrow();
            orderLine.setProductItem(productItem);
            orderLineRepository.save(orderLine);
        });




        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        return OrderResponse.builder().orderList(orderList).build();
    }

    private String generateCode()
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public OrderResponse getOrderByUser(User user, int page, int size,int status, String startDate, String endDate) {
        try{
            List<Order> orderList;
            Specification<Order> spec = Specification.where(null);

            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), user.getId()));

            if(status != 0){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("orderStatus").get("id"), status));
            }
            if(!startDate.isEmpty() && !endDate.isEmpty()){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date1 = format.parse(startDate+" 00:00:00");
                Date date2 = format.parse(endDate + " 23:59:59");
                spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), date1,date2));

//                spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("orderDate"), date1));
//                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("orderDate"), date2));
            }
            Pageable paging = PageRequest.of(page, size,Sort.by("orderDate").descending());
            Page<Order> pageOrders = orderRepository.findAll(spec,paging);
            orderList = pageOrders.getContent();
            return OrderResponse
                    .builder()
                    .orderList(orderList)
                    .currentPage(pageOrders.getNumber())
                    .totalItems(pageOrders.getTotalElements())
                    .totalPages(pageOrders.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

    public OrderResponse getAll(int page, int size, int status, String code, String phone, String startDate, String endDate) {
        try{
            List<Order> orderList;
            Specification<Order> spec = Specification.where(null);
            if(status != 0){
                spec = spec.and((root, query, cb) -> cb.equal(root.get("orderStatus").get("id"), status));
            }
            if(!code.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("code"), "%"+code+"%"));
            }
            if(!phone.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("phone"), "%"+phone+"%"));
            }
            if(!startDate.isEmpty() && !endDate.isEmpty()){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date1 = format.parse(startDate+" 00:00:00");
                Date date2 = format.parse(endDate + " 23:59:59");
                spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), date1,date2));

//                spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("orderDate"), date1));
//                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("orderDate"), date2));
            }   
            Pageable paging = PageRequest.of(page, size, Sort.by("orderDate").descending());
            Page<Order> pageOrders = orderRepository.findAll(spec,paging);
            orderList = pageOrders.getContent();
            return OrderResponse
                    .builder()
                    .orderList(orderList)
                    .currentPage(pageOrders.getNumber())
                    .totalItems(pageOrders.getTotalElements())
                    .totalPages(pageOrders.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public Order updateOrder(UUID id, Integer statusId, String description) {
        Order order = orderRepository.findById(id).orElseThrow();
        OrderStatus status = orderStatusRepository.findById(statusId).orElseThrow();

        //  set stock when order in transit
        if(status.getStatus().equals("In Transit")  && order.getOrderStatus().getStatus().equals("Ordered")){
            Set<OrderLine> orderLines = orderLineService.getAllByOrderId(order.getId());

            orderLines.forEach(orderLine -> {
                ProductItem productItem = productItemRepository.findById(orderLine.getProductItem().getId()).orElseThrow();
                int curStock = productItem.getStock() - orderLine.getQuantity();
                productItem.setStock(curStock);
                productItemRepository.save(productItem);
            });
        } else if (status.getStatus().equals("Attempted delivery")  && order.getOrderStatus().getStatus().equals("In Transit")) {
            Set<OrderLine> orderLines = orderLineService.getAllByOrderId(order.getId());
            orderLines.forEach(orderLine -> {
                ProductItem productItem = productItemRepository.findById(orderLine.getProductItem().getId()).orElseThrow();
                int curStock = productItem.getStock() + orderLine.getQuantity();
                productItem.setStock(curStock);
                productItemRepository.save(productItem);
            });
            order.setDescription(description);
        }else if(status.getStatus().equals("Completed")){
            order.setDeliveryDate(new Date());
            Set<OrderLine> orderLines = orderLineService.getAllByOrderId(order.getId());

            orderLines.forEach(orderLine -> {
                ProductItem productItem = productItemRepository.findById(orderLine.getProductItem().getId()).orElseThrow();
                int sold = productItem.getSold() != null ? productItem.getSold(): 0;
                int curSold= sold + orderLine.getQuantity();
                productItem.setSold(curSold);
                productItemRepository.save(productItem);
            });
        }
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }


    public Order cancelOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow();
        OrderStatus status = orderStatusRepository.findById(4).orElseThrow();
        order.setOrderStatus(status);

        return  orderRepository.save(order);
    }

    public Order getById(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow();
        Set<OrderLine> orderLines = orderLineService.getAllByOrderId(order.getId());
        orderLines.forEach(orderLine -> {
            Set<VariationOption> options = variationOptionRepository.findByProductItemId(orderLine.getProductItem().getId());
            orderLine.getProductItem().setOptions(options);
        });
        order.setOrderLines(orderLines);

        return  order;
    }

    public OrderResponse getAllByStatus(int page, int size, String status) {
        try{
            List<Order> orderList;
            Pageable paging = PageRequest.of(page, size, Sort.by("orderDate").descending());
            Page<Order> pageOrders = orderRepository.findByStatus(paging, status);
            orderList = pageOrders.getContent();
            return OrderResponse
                    .builder()
                    .orderList(orderList)
                    .currentPage(pageOrders.getNumber())
                    .totalItems(pageOrders.getTotalElements())
                    .totalPages(pageOrders.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public OrderResponse checkOrderedByProductItemId(User user, UUID productId) {
        List<ProductItem> productItems = productItemRepository.findAllByProductId(productId);
        if (productItems.size() < 0){
            return  OrderResponse.builder().orderList(new ArrayList<>()).build();        }
        for(ProductItem item: productItems){
            Specification<Order> spec = Specification.where(null);

            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), user.getId()));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("orderStatus").get("id"), 3));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("orderLines").get("productItem").get("id"), item.getId()));

            List<Order> orderList = orderRepository.findAll(spec);
            if(orderList.size() > 0){
                return OrderResponse.builder().orderList(orderList).build();
            }

        }
        return  OrderResponse.builder().orderList(new ArrayList<>()).build();
    }

}
