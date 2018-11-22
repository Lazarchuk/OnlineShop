package online.shop.dao;

import online.shop.model.Product;

import java.sql.*;
import java.util.*;

public class MySQLProduct implements ProductDAO {
    private Connection connection;
    public MySQLProduct(Connection connection) {
        this.connection = connection;
    }

    //Select one product from DB by ID
    @Override
    public Product getProduct(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        Product product = null;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getInt("price"));
                product.setCategory(resultSet.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    //Select all categories from DB
    @Override
    public List<String> getCategories() {
        String sql = "SELECT category FROM products";
        List<String> categories = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String item = resultSet.getString("category");
                if (!categories.contains(item)){
                    categories.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    //Select all products from DB
    @Override
    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, category FROM products";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getInt("price"));
                product.setCategory(resultSet.getString("category"));
                products.add(product);
            }
            resultSet.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    //Select products from DB according to category
    @Override
    public List<Product> getProductsByCategory(String category){
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price FROM products WHERE category=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, category);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getInt("price"));
                product.setCategory(category);
                products.add(product);
            }
            resultSet.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    // get max price of product which exist in DB
    @Override
    public String getMaxPrice(){
        // Default max price
        String maxPrice = "100000";
        String sql = "SELECT price FROM products";
        Set<Integer> prices = new HashSet<>();
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                prices.add(Integer.parseInt(resultSet.getString("price")));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (prices.size() > 0){
            maxPrice = Collections.max(prices).toString();
        }

        return maxPrice;
    }

    @Override
    public List<Product> getProductsByPriceRange(String lowerPrice, String upperPrice) {
        String sql = "SELECT id, name, description, price, category FROM products WHERE price BETWEEN ? and ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, Integer.parseInt(lowerPrice));
            ps.setInt(2, Integer.parseInt(upperPrice));
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getInt("price"));
                product.setCategory(resultSet.getString("category"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getProductsByCategoryAndPrice(String category, String lowerPrice, String upperPrice) {
        String sql = "SELECT id, name, description, price, category FROM products WHERE category=? AND price BETWEEN ? AND ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, category);
            ps.setInt(2, Integer.parseInt(lowerPrice));
            ps.setInt(3, Integer.parseInt(upperPrice));
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getInt("price"));
                product.setCategory(resultSet.getString("category"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
