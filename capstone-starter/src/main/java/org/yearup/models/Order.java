package org.yearup.models;

import java.math.BigDecimal;
import java.sql.Date;

public class Order {

    private int orderId;
    private int userId;
    private Date orderDate;
    private Address shippingAddress;
    private BigDecimal shippingAmount;


    public Order() {
    }


    public Order(int orderId, int userId, Date orderDate, Address shippingAddress, BigDecimal shippingAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.shippingAmount = shippingAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", shippingAddress=" + shippingAddress +
                ", shippingAmount=" + shippingAmount +
                '}';
    }
}
