package com.femmie.shoppingmall.controller;

import com.femmie.shoppingmall.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/checkout/{cartId}")
    public String createCheckoutSession(@PathVariable Long cartId) throws Exception {
        return paymentService.createCheckoutSession(cartId);
    }
}
