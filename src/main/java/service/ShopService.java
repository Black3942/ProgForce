package service;

import model.Category;
import model.Product;
import model.Shop;
import model.Status;

import java.util.List;

public interface ShopService extends GenericService<Shop> {
    Product add(Product product);

    Status changeProductsStatusAtCategories(Category category);

    List<Product> changePricesByStatus(Status status);
}
