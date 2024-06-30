package com.huuphucdang.fashionshop.controller;

import com.huuphucdang.fashionshop.model.payload.request.PaymentRequest;
import com.huuphucdang.fashionshop.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService service;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest body){
        try {
//            System.out.println(mode + clientId + clientSecret);
            String cancelUrl = "http://localhost:3000/payment/cancel";
            String successUrl = "http://localhost:3000/payment/success";
            Payment payment = service.createPayment(
                    body.getTotal()/25000,
                    "USD",
                    "paypal",
                    "sale",
                    body.getDescription(), 
                    cancelUrl,
                    successUrl
            );

            for (Links links: payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return ResponseEntity.ok(links.getHref()+"&useraction=commit");
                }
            }
        }catch (PayPalRESTException e){
            System.out.println(e);
        }

        return ResponseEntity.ok("Error");
    }

    @GetMapping("/success")
    public String paymentSuccess(
        @RequestParam("paymentId") String paymentId,
        @RequestParam("payerId") String payerId
    ){
        try {
            Payment payment = service.executePayment(paymentId,payerId);
            if(payment.getState().equals("approved")){
                return "success";
            }
        }catch (PayPalRESTException e){
            System.out.println(e);
        }

        return  "error";
    }
}
