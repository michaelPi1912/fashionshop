package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.*;
import com.huuphucdang.fashionshop.model.payload.request.CouponRequest;
import com.huuphucdang.fashionshop.model.payload.response.CouponResponse;
import com.huuphucdang.fashionshop.repository.CouponRepository;
import com.huuphucdang.fashionshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.huuphucdang.fashionshop.model.entity.DiscountType.FixedDiscount;
import static com.huuphucdang.fashionshop.model.entity.DiscountType.PercentageDiscount;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public Coupon saveCoupon(CouponRequest body) {
        Coupon coupon = new Coupon();
        coupon.setCode(body.getCode());
        coupon.setName(body.getName());
        coupon.setDescription(body.getDescription());
        if(body.getDiscountType().equals("percent")){
            coupon.setDiscountType(PercentageDiscount);
        }else {
            coupon.setDiscountType(FixedDiscount);
        }

        coupon.setAmount(body.getAmount());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(body.getStartDate());
            Date date2 = format.parse(body.getEndDate());
            coupon.setStartDate(date1);
            coupon.setEndDate(date2);
        } catch (ParseException e) {
            System.out.println(e);
        }
        coupon.setLimitUsage(body.getLimitUsage());
        coupon.setTimes(body.getTimes());
        coupon.setMaxValue(body.getMaxValue());

        return couponRepository.save(coupon);
    }

    public CouponResponse findAll(int page, int size, int status, String code) {
        try {
            List<Coupon> couponList;
            Specification<Coupon> spec = Specification.where(null);
            System.out.println(code);
            if(!code.isEmpty()){
                spec = spec.and((root, query, cb) -> cb.like(root.get("code"), "%"+code+"%"));
            }
            Date date = new Date();
            if(status == 1){
                spec = spec.and((root, query, cb) -> cb.lessThan(root.get("startDate"), date));
                spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("endDate"), date));
            }
            if(status == 2){
                spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("startDate"), date));
//                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("endDate"), date));
            }
            if(status == 3){
//                spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("startDate"), date));
                spec = spec.and((root, query, cb) -> cb.lessThan(root.get("endDate"), date));
            }

            Pageable paging = PageRequest.of(page, size);
            Page<Coupon> pageCategories = couponRepository.findAll(spec,paging);
            couponList = pageCategories.getContent();
            return CouponResponse
                    .builder()
                    .couponList(couponList)
                    .currentPage(pageCategories.getNumber())
                    .totalItems(pageCategories.getTotalElements())
                    .totalPages(pageCategories.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public void updateCoupon(CouponRequest body, UUID id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow();

        coupon.setName(body.getName());
        coupon.setDescription(body.getDescription());
        if(body.getDiscountType().equals("percent")){
            coupon.setDiscountType(PercentageDiscount);
        }else {
            coupon.setDiscountType(FixedDiscount);
        }

        coupon.setAmount(body.getAmount());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(body.getStartDate());
            Date date2 = format.parse(body.getEndDate());
            coupon.setStartDate(date1);
            coupon.setEndDate(date2);
        } catch (ParseException e) {
            System.out.println(e);
        }
        coupon.setLimitUsage(body.getLimitUsage());
        coupon.setTimes(body.getTimes());
        coupon.setMaxValue(body.getMaxValue());

        couponRepository.save(coupon);
    }


    public Coupon checkCoupon(User user, String code) {
        try{

            Coupon coupon = couponRepository.findByCode(code);
            Date date = new Date();

            if(date.after(coupon.getEndDate()) || date.before(coupon.getStartDate())){
                return new Coupon();
            }

            List<Order> orderList;
            Specification<Order> spec = Specification.where(null);

            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), user.getId()));
            spec = spec.and((root, query, cb) -> cb.notEqual(root.get("orderStatus").get("id"), 4));

            orderList = orderRepository.findAll(spec);
            int count = 0;
            for (Order order: orderList){
                if(order.getCoupons() != null){
                    for(String s: order.getCoupons()){
                        if(s.equals(code)){
                            count++;
                        }
                    }
                }

            }


            return coupon.getTimes() > count ? coupon : new Coupon();

        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public void deleteCoupon(UUID id) {
        couponRepository.deleteById(id);
    }
}
