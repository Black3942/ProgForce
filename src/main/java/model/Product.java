package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String title;
    private BigDecimal price;
    private String status;
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id)
                && Objects.equals(title, product.title)
                && Objects.equals(price, product.price)
                && Objects.equals(status, product.status)
                && Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, status, category);
    }
}
