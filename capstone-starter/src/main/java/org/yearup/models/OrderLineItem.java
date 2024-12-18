package org.yearup.models;

import java.util.Objects;

public class OrderLineItem {

    private int orderItemId;
    private int orderId;
    private int productId;
    private double salesPrice;
    private int quantity;
    private double discount;


    public OrderLineItem() {
    }
    public OrderLineItem(int order_item_id, int order_id, int product_id, double salesPrice, int quantity, double discount) {
        this.orderItemId = order_item_id;
        this.orderId = order_id;
        this.productId = product_id;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItem that = (OrderLineItem) o;
        return getOrderItemId() == that.getOrderItemId() && getOrderId() == that.getOrderId() && getProductId() == that.getProductId() && Double.compare(getSalesPrice(), that.getSalesPrice()) == 0 && getQuantity() == that.getQuantity() && Double.compare(getDiscount(), that.getDiscount()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderItemId(), getOrderId(), getProductId(), getSalesPrice(), getQuantity(), getDiscount());
    }


    @Override
    public String toString() {
        return "OrderLineItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", salesPrice=" + salesPrice +
                ", quantity=" + quantity +
                ", discount=" + discount +
                '}';
    }
}
