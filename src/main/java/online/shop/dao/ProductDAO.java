package online.shop.dao;

import online.shop.model.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    Product getProduct(int id);
    List<String> getCategories();
}
