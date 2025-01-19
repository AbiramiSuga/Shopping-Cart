package org.shoppingcart.service;

import org.shoppingcart.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.shoppingcart.repository.CartRepository;
import org.shoppingcart.model.CartProduct;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    private static final String TOPIC = "inventory-topic";

    @Autowired
    private KafkaTemplate<String, CartProduct> kafkaTemplate;

    @Autowired
    private CartRepository cartRepository;


    public void processOrder(Long cartId) {
        Optional<org.shoppingcart.model.Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found with ID: " + cartId);
        }

        org.shoppingcart.model.Cart cart = cartOpt.get();

        // Extract CartProduct details
        List<CartProduct> cartProducts = cart.getCartProducts().stream()
                .map(cartProduct -> {
                    CartProduct sharedCartProduct = new CartProduct();
                    sharedCartProduct.setId(cartProduct.getId());
                    sharedCartProduct.setProduct(cartProduct.getProduct());
                    sharedCartProduct.setQuantity(cartProduct.getQuantity());
                    return sharedCartProduct;
                })
                .collect(Collectors.toList());

        try {
            // Send each CartProduct to Kafka
            for (CartProduct cartProduct : cartProducts) {
                kafkaTemplate.send(TOPIC, cartProduct);
            }
            System.out.println("Order sent to inventory: " + cartProducts);
            publisher.publishEvent(new PaymentEvent(this));

        } catch (Exception e) {
            throw new RuntimeException("Failed to process order", e);
        }
    }
}
