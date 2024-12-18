package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.models.Address;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;


@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    private static final Logger log = LoggerFactory.getLogger(MySqlOrderDao.class);

    private ProfileDao profileDao;

    @Autowired
    public MySqlOrderDao(DataSource dataSource, ProfileDao profileDao) {
        super(dataSource);
        this.profileDao = profileDao;
    }

    @Override
    public Optional<Order> create(ShoppingCart cart, int userId) {

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
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category

                    log.info("Order created: {}", orderId);
                    return getByOrderId(orderId);}
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

    @Override
    public Optional<Order> update(Order order) {

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE orders SET user_id = ?, date = ?, address = ?, city = ?, state = ?, zip = ?, shipping_amount = ? WHERE order_id = ?")) {
            statement.setInt(1, order.getUserId());
            statement.setDate(2, order.getOrderDate());
            statement.setString(3, order.getShippingAddress().getStreet());
            statement.setString(4, order.getShippingAddress().getCity());
            statement.setString(5, order.getShippingAddress().getState());
            statement.setString(6, order.getShippingAddress().getZipCode());
            statement.setBigDecimal(7, order.getShippingAmount());
            statement.setInt(8, order.getOrderId());
            statement.executeUpdate();
            log.info("Order updated: {}", order);
            Optional<Order> byOrderId = getByOrderId(order.getOrderId());
            if (byOrderId.isPresent()) {
                return byOrderId;
            }
        } catch (Exception e) {
            log.error("Error updating order", e);
            return Optional.empty();
        }
        return Optional.of(order);
    }

    @Override
    public Optional<Order> delete(int id) {
        Optional<Order> orderToDelete = getByOrderId(id);
        if (orderToDelete.isPresent()) {
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE order_id = ?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
                log.info("Order deleted: {}", orderToDelete);
                return orderToDelete;
            } catch (Exception e) {
                log.error("Error deleting order", e);
            }
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
