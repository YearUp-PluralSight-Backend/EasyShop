package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    private static final Logger log = LoggerFactory.getLogger(MySqlCategoryDao.class);

    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM categories")) {
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    Category category = mapRow(rows);
                    categories.add(category);

                }
            }

        } catch (SQLException e) {
            log.error("Error occurred while getting all categories", e);

        }
        log.info("Retrieved all categories:  Size of categories: {}", categories.size());
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT category_id, name, description  FROM categories WHERE category_id = ?")) {
            statement.setInt(1, categoryId);
            try (ResultSet rows = statement.executeQuery()) {
                if (rows.next()) {
                    log.info("Retrieved category by id: {}", categoryId);
                    return mapRow(rows);
                }
            }
            return null;
        } catch (SQLException e) {
            log.error("Error occurred while getting category by id", e);
            return null;
        }
    }

    @Override
    public Category create(Category category) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO categories (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    log.info("Created product with id: {}", orderId);
                    return getById(orderId);
                }
            }
            return null;
        } catch (SQLException e) {
            log.error("Error occurred while creating category", e);
            return null;
        }

    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        try (Connection connection = super.getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE categories SET name = ?, description = ? WHERE category_id = ?")) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);
            statement.executeUpdate();
            log.info("Category updated successfully with id: {}", categoryId);
        } catch (SQLException e) {
            log.error("Error occurred while updating category", e);
        }


    }

    @Override
    public void delete(int categoryId) {
        // delete category
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM categories WHERE category_id = ?")) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();
            log.info("Category deleted successfully with id: {}", categoryId);
        } catch (SQLException e) {
            log.error("Error occurred while deleting category", e);
        }

    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        return new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};
    }

}
