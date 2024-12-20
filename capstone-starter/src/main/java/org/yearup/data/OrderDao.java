package org.yearup.data;

import org.springframework.stereotype.Component;
import org.yearup.models.Order;
import org.yearup.models.OrderResult;
import org.yearup.models.ShoppingCart;

import java.util.Optional;


@Component
public interface OrderDao {

    Optional<Order> create(Order order);
    Optional<Order> getByOrderId(int id);
//    Optional<Order> update(Order order);
//    Optional<Order> delete(int id);

}
