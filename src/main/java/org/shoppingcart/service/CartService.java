package org.shoppingcart.service;

import org.shoppingcart.model.Cart;
import org.shoppingcart.model.CartProduct;
import org.shoppingcart.model.CartProductKey;
import org.shoppingcart.model.Product;
import org.shoppingcart.repository.CartProductRepository;
import org.shoppingcart.repository.CartRepository;
import org.shoppingcart.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }

    public Cart createCart(Long id) {
        Cart cart=new Cart();
        cart.setUserId(id);
        return cartRepository.save(cart);
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public ResponseEntity<Cart> getCartById(Long id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            Cart existingCart = cart.get();
            // Calculate total price if not set
//            if (existingCart.getTotalPrice() == 0) {
                double totalPrice = calculateTotalPrice(existingCart);
                existingCart.setTotalPrice(totalPrice);
                cartRepository.save(existingCart);
                System.out.println("===========================>"+totalPrice);
                cartRepository.save(existingCart); // Save updated cart with total price
//            }

            return ResponseEntity.ok(existingCart);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Cart> updateCart(Long id, Cart updatedCart) {
        return cartRepository.findById(id)
                .map(existingCart -> {
                    existingCart.setUserId(updatedCart.getUserId());
                    existingCart.setTotalPrice(updatedCart.getTotalPrice());
                    return ResponseEntity.ok(cartRepository.save(existingCart));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteCart(Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Cart> addProductToCart(Long cartId, Long productId, Integer quantity) {
        Optional<Cart> cartOpt = cartRepository.findById(cartId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (cartOpt.isPresent() && productOpt.isPresent()) {
            Cart cart = cartOpt.get();
            Product product = productOpt.get();

            // Check if the product already exists in the cart
            CartProduct existingCartProduct = cart.getCartProducts().stream()
                    .filter(cp -> cp.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);


            if (existingCartProduct != null) {
                // Update the quantity of the existing product
                existingCartProduct.setQuantity(existingCartProduct.getQuantity() + quantity);
                cartProductRepository.save(existingCartProduct);
            } else {
                // Add new CartProduct if it doesn't already exist
                CartProductKey cartProductKey = new CartProductKey(cartId, productId);
                CartProduct newCartProduct = new CartProduct();
                newCartProduct.setId(cartProductKey);
                newCartProduct.setCart(cart);
                newCartProduct.setProduct(product);
                newCartProduct.setQuantity(quantity);

                cartProductRepository.save(newCartProduct);
            }

            // Recalculate total price
            double newTotalPrice = calculateTotalPrice(cart);
            cart.setTotalPrice(newTotalPrice);
            cartRepository.save(cart);

            return ResponseEntity.ok(cart);
        }

        return ResponseEntity.notFound().build();
    }


    @Transactional
    public ResponseEntity<Cart> removeProductFromCart(Long cartId, Long productId) {
        Optional<Cart> cart = cartRepository.findById(cartId);

        if (cart.isPresent()) {
            List<CartProduct> cartProducts = cartProductRepository.findAll();
            cartProducts.stream()
                    .filter(cp -> cp.getCart().getId().equals(cartId) && cp.getProduct().getId().equals(productId))
                    .findFirst()
                    .ifPresent(cartProduct -> {
                        double productCost = cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
                        cart.get().setTotalPrice(cart.get().getTotalPrice() - productCost);
                        cartProductRepository.delete(cartProduct);
                        cartRepository.save(cart.get());
                    });

            return ResponseEntity.ok(cart.get());
        }
        return ResponseEntity.notFound().build();
    }
    private double calculateTotalPrice(Cart cart) {
        return cart.getCartProducts().stream()
                .mapToDouble(cp -> cp.getProduct().getPrice() * cp.getQuantity())
                .sum();
    }
}
