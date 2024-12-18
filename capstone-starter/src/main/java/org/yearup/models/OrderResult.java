package org.yearup.models;


import java.math.BigDecimal;
import java.util.Map;

public class OrderResult {

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


}