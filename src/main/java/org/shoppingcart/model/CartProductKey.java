package org.shoppingcart.model;

import lombok.*;


import java.io.Serializable;
import javax.persistence.Embeddable;


@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CartProductKey implements Serializable {

    private Long cartId;
    private Long productId;

}

