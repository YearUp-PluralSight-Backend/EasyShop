package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {


    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ShoppingCartDao shoppingCartDao;

    @Autowired
    public OrderController(OrderDao orderDao, UserDao userDao, ShoppingCartDao shoppingCartDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Order> createOrder(Principal principal) {

        Optional<Order> newOrder = Optional.empty();
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        Optional<ShoppingCart> byUserId = shoppingCartDao.getByUserId(userId);
        if (byUserId.isEmpty()) {
            return ResponseEntity.status(400).build();
        } else {
            newOrder = orderDao.create(byUserId.get(), userId);

        }
        return newOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(500).build());

    }


}
