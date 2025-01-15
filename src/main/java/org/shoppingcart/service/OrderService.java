package org.shoppingcart.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.shoppingcart.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.shoppingcart.repository.*;
import org.shoppingcart.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher){
        this.publisher=publisher;
    }

    private static final String TOPIC = "inventory-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void processOrder(Long cartId) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart not found with ID: " + cartId);
        }

        Cart cart = cartOpt.get();

        // Extract productId, name, and quantity for each product in the cart
        List<Object> productDetails = cart.getCartProducts().stream()
                .map(cartProduct -> {
                    Product product = cartProduct.getProduct();
                    return new Object() { // Inline DTO with only required fields
                        public final Long productId = product.getId();
                        public final String name = product.getName();
                        public final Integer quantity = cartProduct.getQuantity();
                    };
                })
                .collect(Collectors.toList());

        try {
            // Convert the product details to JSON
            String message = objectMapper.writeValueAsString(productDetails);
            kafkaTemplate.send(TOPIC, message); // Send to Kafka topic
            System.out.println("Order sent to inventory: " + message);
            publisher.publishEvent(new PaymentEvent(this));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize product details", e);
        }
    }
}
