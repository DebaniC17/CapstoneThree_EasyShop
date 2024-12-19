package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.controllers.ProductsController;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    //  public List<Category> getAllCategories()
    public List<Category> findAll(String sort) {

        String[] acceptableSorts = {"category_id", "name", "description"};
        if (sort == null || !List.of(acceptableSorts).contains(sort)) {
            sort = "category_id";
        }

        String query = "SELECT * FROM categories ORDER BY " + sort + ";";
        List<Category> categories = new ArrayList<>();

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                categories.add(mapRow(resultSet));
                // categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving categories.", e);
        }
        // get all categories
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        String query = "SELECT * FROM categories WHERE category_id = ?;";
        // try (
//                Connection connection = this.dataSource.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(query);
//
//                ) {
//            preparedStatement.setInt(1, categoryId);
//            try (
//                    ResultSet resultSet = preparedStatement.executeQuery();
//
//                    ) {
//                if (resultSet.next()) {
//                    return mapRow(resultSet);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving category by ID.", e);
        }
        return null;
    }

    @Override
    public Category create(Category category) {
        String query = "INSERT INTO categories(name, description) VALUES (?,?);";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {


            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return getById(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating category.", e);
        }
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        String query = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getCategoryId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating category.", e);
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String query = "DELETE FROM categories WHERE category_id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category.", e);
        }
    }
//        private Category mapRow(ResultSet row) throws SQLException {
//        int categoryId = row.getInt("category_id");
//        String name = row.getString("name");
//        String description = row.getString("description");
//
//        Category category = new Category() {{
//            setCategoryId(categoryId);
//            setName(name);
//            setDescription(description);
//        }};
//        return category;
//    }
    private Category mapRow(ResultSet row) throws SQLException {
        return new Category(
                row.getInt("category_id"),
                row.getString("name"),
                row.getString("description")
        );
    }
}
