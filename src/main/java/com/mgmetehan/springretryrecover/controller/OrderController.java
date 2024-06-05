package com.mgmetehan.springretryrecover.controller;

import com.mgmetehan.springretryrecover.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/retryOrderProcess")
    public String retryOrderProcess() {
        try {
            orderService.processOrder("");
            return "Success";
        } catch (Exception e) {
            return "Failed";
        }
    }

    @GetMapping("/retryOrderProcessWithBackoff")
    public String retryOrderProcessWithBackoff() {
        try {
            orderService.processOrderWithBackoff("");
            return "Success";
        } catch (Exception e) {
            return "Failed";
        }
    }
}

