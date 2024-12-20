package org.yearup.models;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderResult {

    private Order order;
    private List<OrderLineItem> items;

    public OrderResult() {
    }

    public OrderResult(Order order, List<OrderLineItem> orderLineItems) {
        this.order = order;
        this.items = orderLineItems;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderLineItem> getItems() {
        return items;
    }

    public void setItems(List<OrderLineItem> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (OrderLineItem orderLineItem : items) {
            total = total.add(new BigDecimal(orderLineItem.getSalesPrice() * orderLineItem.getQuantity()));
        }
        return total;
    }

    public Map<String, Object> toMap() {
        return Map.of("order", order, "orderLineItems", items, "total", getTotal());
    }

    @Override
    public String toString() {
        return "OrderResult{" +
                "order=" + order +
                ", orderLineItems=" + items +
                '}';
    }
}