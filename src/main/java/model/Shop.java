package model;

import java.util.List;
import java.util.Objects;

public class Shop {
    private Long id;
    private String name;
    private List<Category> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shop shop = (Shop) o;
        return Objects.equals(id, shop.id) && Objects.equals(name, shop.name)
                && Objects.equals(categories, shop.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, categories);
    }
}
