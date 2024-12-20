package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private final Logger log = LoggerFactory.getLogger(MySqlShoppingCartDao.class);
    private final MySqlProductDao productDao;


    public MySqlShoppingCartDao(DataSource dataSource, MySqlProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public Optional<ShoppingCart> getByUserId(int userId) {

        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?";
        ShoppingCart cart = new ShoppingCart();
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
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

        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();

            log.info("Added product to cart for user: {}", userId);
            return getByUserId(userId);

        } catch (Exception e) {
            log.error("Error occurred while adding product to cart", e);
            return Optional.empty();
        }
    }


    @Override
    public Optional<ShoppingCart> updateProductInCart(int userId, int productId, int quantity) {

        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();

            log.info("Updated product in cart for user: {}", userId);
            return getByUserId(userId);

        } catch (Exception e) {
            log.error("Error occurred while updating product in cart", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ShoppingCart> clearCart(int userId) {

        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();

            log.info("Cleared cart for user: {}", userId);
            return Optional.of(new ShoppingCart());

        } catch (Exception e) {
            log.error("Error occurred while clearing cart", e);
            return Optional.empty();
        }
    }
}
