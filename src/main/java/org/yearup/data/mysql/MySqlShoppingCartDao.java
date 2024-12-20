package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String query = "SELECT sc.*, p.product_id, p.name, p.price, p.description, p.color, p.stock, p.image_url, p.featured " +
                "FROM shopping_cart sc " +
                "JOIN products p ON sc.product_id = p.product_id " +
                "WHERE sc.user_id = ?;";

        ShoppingCart cart = new ShoppingCart();

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, userId);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    int quantity = resultSet.getInt("quantity");
                    product.setProductId(resultSet.getInt("product_id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getBigDecimal("price"));
                    product.setCategoryId(resultSet.getInt("category_id"));
                    product.setDescription(resultSet.getString("description"));
                    product.setColor(resultSet.getString("color"));
                    product.setStock(resultSet.getInt("stock"));
                    product.setImageUrl(resultSet.getString("image_url"));
                    product.setFeatured(resultSet.getBoolean("featured"));

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                   // item.setQuantity(resultSet.getInt("quantity"));
                    item.setQuantity(quantity);
                    cart.add(item);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving shopping cart for " + userId, e);
        }
        return cart;
    }

    @Override
    public void addProductToCart(int userId, int productId) {
        String query = "INSERT INTO shopping_cart (user_id, product_id) " +
                       "VALUES (?,?,1) " +
                       "ON DUPLICATE KEY UPDATE quantity = quantity + 1";

        try(
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
                ) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding product " + productId, e);
        }
    }

    @Override
    public void updateProductQuantity(int userId, int productId, int quantity) {
        String query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try(
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
                ) {
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

        } catch (SQLException e) {
            throw new RuntimeException("Error updating quantity for product " + productId, e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
                ) {
            statement.setInt(1, userId);
            statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
