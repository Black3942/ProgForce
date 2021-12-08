package dao.impl;

import dao.ProductDao;
import exception.DataProcessingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lib.Dao;
import model.Category;
import model.Product;
import util.ConnectionUtil;

@Dao
public class ProductDaoImpl implements ProductDao {
    @Override
    public Product create(Product product) {
        String query = "INSERT INTO products (title, price, status, category) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getTitle());
            statement.setBigDecimal(2, product.getPrice());
            statement.setString(3, product.getStatus());
            statement.setObject(4, product.getCategory());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setId(resultSet.getObject(1, Long.class));
            }
            return product;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create " + product + ". ", e);
        }
    }

    @Override
    public Optional<Product> get(Long id) {
        String query = "SELECT * FROM products WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = null;
            if (resultSet.next()) {
                product = getProduct(resultSet);
            }
            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get product by id " + id, e);
        }
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM products WHERE is_deleted = FALSE";
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get a list of products from DB.", e);
        }
    }

    @Override
    public Product update(Product product) {
        String query = "UPDATE products "
                + "SET title = ?, price = ?, status = ?, category = ? "
                + "WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getTitle());
            statement.setBigDecimal(2, product.getPrice());
            statement.setString(3, product.getStatus());
            statement.setObject(4, product.getCategory());
            statement.setLong(5, product.getId());
            statement.executeUpdate();
            return product;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update " + product + " in DB.", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE products SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete product with id " + id, e);
        }
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("id", Long.class);
        String title = resultSet.getString("title");
        BigDecimal price = resultSet.getBigDecimal("price");
        String status = resultSet.getString("status");
        Category category = (Category) resultSet.getObject("category");
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setStatus(status);
        product.setCategory(category);
        return product;
    }
}
