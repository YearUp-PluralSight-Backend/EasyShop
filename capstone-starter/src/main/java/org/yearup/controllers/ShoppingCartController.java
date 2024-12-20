package org.yearup.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController {
    private static final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    // add a constructor that takes in the shoppingCartDao, userDao, and productDao
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> getCart(Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            log.info("Retrieved shopping cart for user: {}", userId);
            return ResponseEntity.ok(shoppingCartDao.getByUserId(userId).orElse(null));
        } catch (Exception e) {
            log.error("Error getting cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> addProductToCart(Principal principal, @PathVariable int productId) {
        try {
            // Get the currently logged-in username
            String userName = principal.getName();
            // Find database user by username
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Check if the product already exists in the cart
            if (shoppingCartDao.checkExistingProductInCart(userId, productId)) {
                // If it exists, update the product quantity
                shoppingCartDao.updateProductInCart(userId, productId, shoppingCartDao.getQuantityOfProductInCart(userId, productId) + 1);
            } else {
                // If the product doesn't exist in the cart, add it
                log.info("Added product to cart for user: {}", userId);
                shoppingCartDao.addProductToCart(userId, productId);
            }

            // Return the updated shopping cart
            Optional<ShoppingCart> updatedCart = shoppingCartDao.getByUserId(userId); // Assuming this method exists
            return ResponseEntity.ok(updatedCart.get());

        } catch (Exception e) {
            log.error("Error adding product to cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Change to INTERNAL_SERVER_ERROR
        }
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> updateProductInCart(Principal principal, @PathVariable int productId, @RequestBody int quantity) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to update the product in the cart and return the cart
            log.info("Updated product in cart for user: {}", userId);
            return ResponseEntity.ok(shoppingCartDao.updateProductInCart(userId, productId, quantity).orElse(null));
        } catch (Exception e) {
            log.error("Error updating product in cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> clearCart(Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to clear the cart and return the cart
            log.info("Cleared cart for user: {}", userId);
            return ResponseEntity.ok(shoppingCartDao.clearCart(userId).orElse(null));
        } catch (Exception e) {
            log.error("Error clearing cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
