package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private final Logger log = LoggerFactory.getLogger(MySqlShoppingCartDao.class);
    private final MySqlProductDao productDao;


    public MySqlShoppingCartDao(DataSource dataSource, MySqlProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public Optional<ShoppingCart> getByUserId(int userId) {

        ShoppingCart cart = new ShoppingCart();
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM shopping_cart WHERE user_id = ?")) {
            // get all items in the cart and return the cart
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rs.getInt("user_id");
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");
                    cart.add(new ShoppingCartItem(productDao.getById(productId), quantity));
                }
            }

        } catch (Exception e) {
            log.error("Error occurred while getting shopping cart", e);
        }
        log.info("Retrieved shopping cart for user: {}", userId);
        return Optional.of(cart);
    }

    @Override
    public Optional<ShoppingCart> addProductToCart(int userId, int productId) {
        return Optional.empty();
    }

    @Override
    public Optional<ShoppingCart> updateProductInCart(int userId, int productId, int quantity) {
        return Optional.empty();
    }

    @Override
    public Optional<ShoppingCart> clearCart(int userId) {
        return Optional.empty();
    }
}