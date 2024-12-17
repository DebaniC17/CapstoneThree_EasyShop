package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.controllers.ProductsController;
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
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
  //  public List<Category> getAllCategories()
      public List<Category> findAll(String sort) {
        if (sort == null) {
            sort = "category_id";
        }

        String [] acceptableSorts = {"category_id", "name", "description"};
        boolean isAcceptable = false;

        for (String sortName : acceptableSorts) {
            if (sortName.equals(sort)) {
                isAcceptable = true;
            }
        }
        if (!isAcceptable) {
            System.out.println("Sort value not acceptable");
            return null;
        }

        String query = "SELECT * FROM categories ORDER BY " + sort + ";";

        List<Category> categories = new ArrayList<>();
        try (
                Connection connection = this.dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                ) {
            while (resultSet.next()) {
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);

            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String query = "INSERT INTO categories(category_id, name, description)" + " VALUES (?,?,?);";

        try (
                Connection connection = getConnection()
                ) {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    return getById(orderId);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
