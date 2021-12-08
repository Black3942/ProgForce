package dao.impl;

import dao.CategoryDao;
import exception.DataProcessingException;
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
import util.ConnectionUtil;

@Dao
public class CategoryDaoImpl implements CategoryDao {
    @Override
    public Category create(Category category) {
        String query = "INSERT INTO categories (name) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                category.setId(resultSet.getObject(1, Long.class));
            }
            return category;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create " + category + ". ", e);
        }
    }

    @Override
    public Optional<Category> get(Long id) {
        String query = "SELECT * FROM categories WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Category category = null;
            if (resultSet.next()) {
                category = getCategory(resultSet);
            }
            return Optional.ofNullable(category);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get category by id " + id, e);
        }
    }

    @Override
    public List<Category> getAll() {
        String query = "SELECT * FROM categories WHERE is_deleted = FALSE";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categories.add(getCategory(resultSet));
            }
            return categories;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get a list of categories from DB.", e);
        }
    }

    @Override
    public Category update(Category category) {
        String query = "UPDATE categories SET name = ? WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();
            return category;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update " + category + " in DB.", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE categories SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete category with id " + id, e);
        }
    }

    private Category getCategory(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }
}
