package org.yearup.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.*;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {


    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ShoppingCartDao shoppingCartDao;
    private final ProfileDao profileDao;
    private final OrderLineItemDao orderLineItemDao;

    @Autowired
    public OrderController(OrderDao orderDao, UserDao userDao, ShoppingCartDao shoppingCartDao, ProfileDao profileDao, OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
        this.profileDao = profileDao;
        this.orderLineItemDao = orderLineItemDao;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<OrderResult> createOrder(Principal principal) {


        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        //go get the shopping cart data...
        Optional<ShoppingCart> byUserId = shoppingCartDao.getByUserId(userId);

        //go get the profile for the user
        Optional<Profile> profileByUserId = profileDao.getProfileByUserId(userId);

        if (byUserId.isEmpty() || profileByUserId.isEmpty()) {
            return ResponseEntity.status(400).build();
        } else {
            Address address = new Address();
            Map<Integer, ShoppingCartItem> items = byUserId.get().getItems();
            //build an order object from th profile
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderDate(new Date(System.currentTimeMillis()));
            order.setShippingAddress(address);
            order.setShippingAmount(new BigDecimal("6.99"));
            address.setStreet(profileByUserId.get().getAddress());
            address.setCity(profileByUserId.get().getCity());
            address.setState(profileByUserId.get().getState());
            address.setZipCode(profileByUserId.get().getZip());

            Optional<Order> order1 = orderDao.create(order);
            log.info("order1: {}", order1);

            //build a List<OrderLineItem> of orderitem obejcts from the shopping cart
            log.info("size of items in the cart: {}", items.size());
            for (Map.Entry<Integer, ShoppingCartItem> entry : items.entrySet()) {
                log.info("key: {} value: {}", entry.getKey(), entry.getValue());
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setOrderId(order1.get().getOrderId());
                orderLineItem.setProductId(entry.getValue().getProductId());
                orderLineItem.setSalesPrice(entry.getValue().getLineTotal().doubleValue());
                orderLineItem.setQuantity(entry.getValue().getQuantity());
                orderLineItem.setDiscount(0.0);
                orderLineItemDao.saveOrderLineItem(orderLineItem);

            }

            //clear the shopping cart
            shoppingCartDao.clearCart(userId);

            //return the order result
            OrderResult orderResult = new OrderResult();
            orderResult.setOrder(order1.get());
            orderResult.setItems(orderLineItemDao.getOrderLineItemsByOrderId(order1.get().getOrderId()));
            return ResponseEntity.ok(orderResult);


        }
    }


}
