package org.yearup.data;

import org.springframework.stereotype.Component;
import org.yearup.models.ShoppingCart;

import java.util.Optional;

@Component
public interface ShoppingCartDao {
    Optional<ShoppingCart> getByUserId(int userId);

    // add additional method signatures here
    Optional<ShoppingCart> addProductToCart(int userId, int productId);

    Optional<ShoppingCart> updateProductInCart(int userId, int productId, int quantity);

    Optional<ShoppingCart> clearCart(int userId);

}
