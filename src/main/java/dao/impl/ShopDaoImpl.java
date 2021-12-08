package dao.impl;

import dao.ShopDao;
import exception.DataProcessingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lib.Dao;
import model.Category;
import model.Shop;
import util.ConnectionUtil;

@Dao
public class ShopDaoImpl implements ShopDao {
    private static final int ZERO_PLACEHOLDER = 0;
    private static final int SHIFT = 2;

    @Override
    public Shop create(Shop shop) {
        String query = "INSERT INTO shops (name)"
                + "VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                             query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, shop.getName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                shop.setId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create shop " + shop, e);
        }
        insertAllCategories(shop);
        return shop;
    }

    @Override
    public Optional<Shop> get(Long id) {
        String query = "SELECT * FROM shops WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Shop shop = null;
            if (resultSet.next()) {
                shop = getShop(resultSet);
            }
            return Optional.ofNullable(shop);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get category by id " + id, e);
        }
    }

    @Override
    public List<Shop> getAll() {
        String query = "SELECT * FROM shops WHERE is_deleted = FALSE";
        List<Shop> shops = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shops.add(getShop(resultSet));
            }
            return shops;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get a list of shops from DB.", e);
        }
    }

    @Override
    public Shop update(Shop shop) {
        String query = "UPDATE shops SET name = ? WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, shop.getName());
            statement.executeUpdate();
            return shop;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update " + shop + " in DB.", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE shops SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete shop with id " + id, e);
        }
    }

    private void insertAllCategories(Shop shop) {
        Long shopId = shop.getId();
        List<Category> categories = shop.getCategories();
        if (categories.size() == 0) {
            return;
        }
        String query = "INSERT INTO shops_categories (shop_id, category_id) VALUES "
                + categories.stream().map(category -> "(?, ?)").collect(Collectors.joining(", "))
                + " ON DUPLICATE KEY UPDATE shop_id = shop_id";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                statement.setLong((i * SHIFT) + 1, shopId);
                statement.setLong((i * SHIFT) + 2, category.getId());
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't insert categories " + categories, e);
        }
    }

    private void deleteAllCategoriesExceptList(Shop shop) {
        Long shopId = shop.getId();
        List<Category> exceptions = shop.getCategories();
        int size = exceptions.size();
        String query = "DELETE FROM shops_categories WHERE shop_id = ? "
                + "AND NOT category_id IN ("
                + ZERO_PLACEHOLDER + ", ?".repeat(size)
                + ");";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, shopId);
            for (int i = 0; i < size; i++) {
                Category category = exceptions.get(i);
                statement.setLong((i) + SHIFT, category.getId());
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete categories " + exceptions, e);
        }
    }

    private Shop getShop(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        Shop shop = new Shop();
        shop.setId(id);
        shop.setName(name);
        return shop;
    }
}
