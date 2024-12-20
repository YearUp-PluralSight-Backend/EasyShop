package org.yearup.data;

import org.springframework.stereotype.Component;
import org.yearup.models.Order;
import org.yearup.models.OrderResult;
import org.yearup.models.ShoppingCart;

import java.util.Optional;


@Component
public interface OrderDao {

<<<<<<< HEAD
    Optional<Order> create(Order order);
=======
    Optional<OrderResult> create(ShoppingCart shoppingCart, int userId);
>>>>>>> c328679789e2e81b7e9582bb76d74bb9be478e63
    Optional<Order> getByOrderId(int id);
//    Optional<Order> update(Order order);
//    Optional<Order> delete(int id);

}
