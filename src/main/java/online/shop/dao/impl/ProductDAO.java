package online.shop.dao.impl;

import online.shop.model.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByPriceRange(String lowerPrice, String upperPrice);
    List<Product> getProductsByCategoryAndPrice(String category, String lowerPrice, String upperPrice);
    Product getProduct(int id);
    List<String> getCategories();
    String getMaxPrice();
}
