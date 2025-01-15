package org.shoppingcart.handler;

import org.shoppingcart.event.PaymentEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {
    @EventListener
    public void paymentProcessing(PaymentEvent event){
        System.out.print("Payment Processing");
    }
}
