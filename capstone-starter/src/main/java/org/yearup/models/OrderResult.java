package org.yearup.models;


import java.math.BigDecimal;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> c328679789e2e81b7e9582bb76d74bb9be478e63
import java.util.Map;

public class OrderResult {

<<<<<<< HEAD
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
=======
    private Map<Integer, ShoppingCartItem> items;
    private double total;


    public OrderResult(Map<Integer, ShoppingCartItem> items) {
        this.items = items;
        this.total = calculateTotal();
    }

    public Map<Integer, ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double calculateTotal() {
        double totalAmount = 0;

        for (Map.Entry<Integer, ShoppingCartItem> entry : items.entrySet()) {
            ShoppingCartItem item = entry.getValue();
            double lineTotal = item.getLineTotal().doubleValue();
            int quantity = item.getQuantity();
            // Calculate total for this item
            totalAmount += lineTotal * quantity;
        }
        return totalAmount;
    }


>>>>>>> c328679789e2e81b7e9582bb76d74bb9be478e63
}