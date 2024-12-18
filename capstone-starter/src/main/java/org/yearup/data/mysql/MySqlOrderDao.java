package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.OrderLineItemDao;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.models.*;

import javax.crypto.spec.IvParameterSpec;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    private static final Logger log = LoggerFactory.getLogger(MySqlOrderDao.class);

    private ProfileDao profileDao;
    private OrderLineItemDao orderLineItemDao;
    private ProductDao productDao;

    @Autowired
    public MySqlOrderDao(DataSource dataSource, ProfileDao profileDao, OrderLineItemDao orderLineItemDao, ProductDao productDao) {
        super(dataSource);
        this.profileDao = profileDao;
        this.orderLineItemDao = orderLineItemDao;
        this.productDao = productDao;
    }

    @Override
    public Optional<OrderResult> create(ShoppingCart cart, int userId) {


        Optional<Profile> profileByUserId = profileDao.getProfileByUserId(userId);
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            statement.setString(3, profileByUserId.get().getAddress());
            statement.setString(4, profileByUserId.get().getCity());
            statement.setString(5, profileByUserId.get().getState());
            statement.setString(6, profileByUserId.get().getZip());
            statement.setBigDecimal(7, new BigDecimal("10.00"));
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    // cart items
                    Map<Integer, ShoppingCartItem> items = cart.getItems();

                    // save the order line items to the database
                    for (Integer item : items.keySet()) {
                        OrderLineItem orderLineItem = new OrderLineItem();
                        orderLineItem.setOrderId(orderId);
                        orderLineItem.setProductId(items.get(item).getProductId());
                        orderLineItem.setSalesPrice(productDao.getProductPrice(items.get(item).getProductId()));
                        orderLineItem.setQuantity(item);
                        orderLineItem.setDiscount(0);
                        orderLineItemDao.saveOrderLineItem(orderLineItem);
                    }
                    List<OrderLineItem> orderLineItemsByOrderId = orderLineItemDao.getOrderLineItemsByOrderId(orderId);
                    log.info("Order line items: {}", orderLineItemsByOrderId);
                    // get the newly inserted category
                    log.info("Order created: {}", orderId);
                    Optional<Order> byOrderId = getByOrderId(orderId);

                    return Optional.of(new OrderResult(cart.getItems()));
                }
            }
        } catch (Exception e) {
            log.error("Error creating order", e);
            return Optional.empty();
        }
        return Optional.empty();

    }


    @Override
    public Optional<Order> getByOrderId(int id) {

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders WHERE order_id = ?")) {
            statement.setInt(1, id);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    log.info("Order found: {}", resultSet);
                    return mapRow(resultSet);
                }
            } catch (Exception e) {
                log.error("Error getting order by id", e);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error getting order by id", e);
            return Optional.empty();
        }
        return Optional.empty();

    }


    private Optional<Order> mapRow(ResultSet resultSet) {
        try {
            Order order = new Order();
            order.setOrderId(resultSet.getInt("order_id"));
            order.setUserId(resultSet.getInt("user_id"));
            order.setOrderDate(resultSet.getDate("date"));
            order.setShippingAddress(new Address(resultSet.getString("address"), resultSet.getString("city"), resultSet.getString("state"), resultSet.getString("zip")));
            order.setShippingAmount(resultSet.getBigDecimal("shipping_amount"));
            log.info("Order: {}", order);
            return Optional.of(order);

        } catch (Exception e) {
            log.error("Error getting order from result set", e);
        }
        return Optional.empty();
    }
}
