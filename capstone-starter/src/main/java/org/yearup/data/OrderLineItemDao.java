package org.yearup.data;

import org.springframework.stereotype.Component;
import org.yearup.models.OrderLineItem;

import java.util.List;
import java.util.Optional;


@Component
public interface OrderLineItemDao {

    List<OrderLineItem> getOrderLineItemsByOrderId(int id);
    void saveOrderLineItem(OrderLineItem orderLineItem);
    void updateOrderLineItem(OrderLineItem orderLineItem);
    void deleteOrderLineItem(OrderLineItem orderLineItem);
}
