package org.yearup.models;

import java.util.Objects;

public class Order_Line_Item {

    private int order_item_id;
    private int order_id;
    private int product_id;
    private double salesPrice;
    private int quantity;
    private double discount;

    public int getOrder_item_id() {
        return order_item_id;
    }

    public Order_Line_Item(int order_item_id, int order_id, int product_id, double salesPrice, int quantity, double discount) {
        this.order_item_id = order_item_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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
        Order_Line_Item that = (Order_Line_Item) o;
        return getOrder_item_id() == that.getOrder_item_id() && getOrder_id() == that.getOrder_id() && getProduct_id() == that.getProduct_id() && Double.compare(getSalesPrice(), that.getSalesPrice()) == 0 && getQuantity() == that.getQuantity() && Double.compare(getDiscount(), that.getDiscount()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder_item_id(), getOrder_id(), getProduct_id(), getSalesPrice(), getQuantity(), getDiscount());
    }


}
