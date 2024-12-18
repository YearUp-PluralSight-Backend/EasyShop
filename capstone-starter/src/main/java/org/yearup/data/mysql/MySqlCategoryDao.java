package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
    }

    @Override
    public void delete(int categoryId) {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
