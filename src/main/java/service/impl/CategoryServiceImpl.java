package service.impl;

import dao.CategoryDao;
import java.util.List;
import lib.Inject;
import lib.Service;
import model.Category;
import service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Inject
    private CategoryDao categoryDao;

    @Override
    public Category create(Category category) {
        return categoryDao.create(category);
    }

    @Override
    public Category get(Long id) {
        return categoryDao.get(id).orElseThrow();
    }

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
    }

    @Override
    public Category update(Category category) {
        return categoryDao.update(category);
    }

    @Override
    public boolean delete(Long id) {
        return categoryDao.delete(id);
    }
}
