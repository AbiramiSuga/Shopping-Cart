package org.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
//@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class CartProduct {

    @EmbeddedId
    private CartProductKey id;


    @ManyToOne
    @MapsId("cartId") // Maps this field to the cartId part of the composite key
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonBackReference
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId") // Maps this field to the productId part of the composite key
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private Integer quantity;
}


