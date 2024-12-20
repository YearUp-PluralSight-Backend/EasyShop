package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {


    private static final Logger log = LoggerFactory.getLogger(MySqlOrderLineItemDao.class);

    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public List<OrderLineItem> getOrderLineItemsByOrderId(int id) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        try (var connection = getConnection()) {
            var sql = "SELECT * FROM order_line_items WHERE order_id = ?";
            try (var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (var resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        var orderLineItem = new OrderLineItem(
                                resultSet.getInt("order_line_item_id"),
                                resultSet.getInt("order_id"),
                                resultSet.getInt("product_id"),
                                resultSet.getDouble("sales_price"),
                                resultSet.getInt("quantity"),
                                resultSet.getDouble("discount")
                        );
                        log.info("Order line item found: {}", orderLineItem);
                        orderLineItems.add(orderLineItem);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting order line items by order_id: {}", id, e);
        }
        return orderLineItems;
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

        try(Connection connection = getConnection()) {
            String sql = "UPDATE order_line_items SET order_id = ?, product_id = ?, sales_price = ?, quantity = ?, discount = ? WHERE order_line_item_id = ?";
            try(var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, orderLineItem.getOrderId());
                statement.setInt(2, orderLineItem.getProductId());
                statement.setDouble(3, orderLineItem.getSalesPrice());
                statement.setInt(4, orderLineItem.getQuantity());
                statement.setDouble(5, orderLineItem.getDiscount());
                statement.setInt(6, orderLineItem.getOrderItemId());
                statement.executeUpdate();
                log.info("Order line item updated {}", orderLineItem);
            }
        } catch (Exception e) {
            log.error("Error updating order line item", e);
        }

    }

    @Override
    public void deleteOrderLineItem(int orderLineItemId) {

        try(Connection connection = getConnection()) {
            String sql = "DELETE FROM order_line_items WHERE order_line_item_id = ?";
            try(var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, orderLineItemId);
                statement.executeUpdate();
                log.info("Order line item deleted {}", orderLineItemId);
            }
        } catch (Exception e) {
            log.error("Error deleting order line item", e);
        }

    }
}
