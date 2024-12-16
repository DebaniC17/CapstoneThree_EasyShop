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
    public Category getById(int categoryId)
    {
        // get category by id
        return null;
    }

    @Override
    public Category create(Category category)
    {
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
