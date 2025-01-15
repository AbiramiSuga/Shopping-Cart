package org.shoppingcart.event;

import org.springframework.context.ApplicationEvent;

public class PaymentEvent extends ApplicationEvent {
     public PaymentEvent(Object source){
         super(source);
     }

}
