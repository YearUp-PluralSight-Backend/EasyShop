package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> c328679789e2e81b7e9582bb76d74bb9be478e63
import java.util.List;
import java.util.Optional;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {


    private static final Logger log = LoggerFactory.getLogger(MySqlOrderLineItemDao.class);

    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public List<OrderLineItem> getOrderLineItemsByOrderId(int id) {
<<<<<<< HEAD
        List<OrderLineItem> orderLineItems = new ArrayList<>(); // Initialize a list to store all items

        try (var connection = getConnection()) {
            var sql = "SELECT * FROM order_line_items WHERE order_id = ?";
            try (var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (var resultSet = statement.executeQuery()) {
                    while (resultSet.next()) { // Use `while` to process all rows
=======

        try(var connection = getConnection()) {
            var sql = "SELECT * FROM order_line_items WHERE order_id = ?";
            try(var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try(var resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
>>>>>>> c328679789e2e81b7e9582bb76d74bb9be478e63
                        var orderLineItem = new OrderLineItem(
                                resultSet.getInt("order_line_item_id"),
                                resultSet.getInt("order_id"),
                                resultSet.getInt("product_id"),
                                resultSet.getDouble("sales_price"),
                                resultSet.getInt("quantity"),
                                resultSet.getDouble("discount")
                        );
<<<<<<< HEAD
                        log.info("Order line item found: {}", orderLineItem);
                        orderLineItems.add(orderLineItem); // Add the item to the list
=======
                        log.info("Order line item found {}", orderLineItem);
                        return List.of(orderLineItem);
>>>>>>> c328679789e2e81b7e9582bb76d74bb9be478e63
                    }
                }
            }
        } catch (Exception e) {
<<<<<<< HEAD
            log.error("Error getting order line items by order_id", e);
        }

        return orderLineItems; // Return the full list of items
=======
            log.error("Error getting order line item by id", e);
            return List.of();
    }
        return List.of();
>>>>>>> c328679789e2e81b7e9582bb76d74bb9be478e63
    }

    @Override
    public void saveOrderLineItem(OrderLineItem orderLineItem) {

        try(var connection = getConnection()) {
            var sql = "INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount) VALUES (?, ?, ?, ?, ?)";
            try(var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, orderLineItem.getOrderId());
                statement.setInt(2, orderLineItem.getProductId());
                statement.setDouble(3, orderLineItem.getSalesPrice());
                statement.setInt(4, orderLineItem.getQuantity());
                statement.setDouble(5, orderLineItem.getDiscount());
                statement.executeUpdate();
                log.info("Order line item saved {}", orderLineItem);
            }
        } catch (Exception e) {
            log.error("Error saving order line item", e);
        }

    }

    @Override
    public void updateOrderLineItem(OrderLineItem orderLineItem) {

    }

    @Override
    public void deleteOrderLineItem(OrderLineItem orderLineItem) {

    }
}
