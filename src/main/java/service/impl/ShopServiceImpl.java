package service.impl;

import dao.ShopDao;
import java.util.List;
import lib.Inject;
import lib.Service;
import model.Category;
import model.Product;
import model.Shop;
import model.Status;
import service.ProductService;
import service.ShopService;

@Service
public class ShopServiceImpl implements ShopService {
    @Inject
    private ShopDao shopDao;
    @Inject
    private ProductService productService;

    @Override
    public Shop create(Shop shop) {
        return shopDao.create(shop);
    }

    @Override
    public Shop get(Long id) {
        return shopDao.get(id).orElseThrow();
    }

    @Override
    public List<Shop> getAll() {
        return shopDao.getAll();
    }

    @Override
    public Shop update(Shop shop) {
        return shopDao.update(shop);
    }

    @Override
    public boolean delete(Long id) {
        return shopDao.delete(id);
    }


    @Override
    public Product add(Product product) {
        return productService.create(product);
    }

    @Override
    public Status changeProductsStatusAtCategories(Category category) {
        return null;
    }

    @Override
    public List<Product> changePricesByStatus(Status status) {
        return null;
    }
}
