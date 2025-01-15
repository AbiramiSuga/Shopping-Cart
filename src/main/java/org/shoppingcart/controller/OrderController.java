package org.shoppingcart.controller;

import org.shoppingcart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{cart_id}")
    public ResponseEntity<String> placeOrder(@PathVariable("cart_id") Long cartId) {
        orderService.processOrder(cartId);
        return ResponseEntity.ok("Order placed successfully.");
    }
}
