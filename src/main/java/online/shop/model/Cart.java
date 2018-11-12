package online.shop.model;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {
    //product in cart and it amount
    private Map<Product, Integer> cartItems;
    //total amount of products in cart
    private int totalAmount;
    //total price of order
    private int totalPrice;
    //temporary key (product)
    private Product tempKey;

    public Cart() {
        cartItems = new ConcurrentHashMap<>();
    }

    public Map<Product, Integer> getCartItems() {
        return cartItems;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    //add item to the cart
    public void addItem(Product product, int productAmount){
        if (!containsProduct(product)){
            cartItems.put(product, productAmount);
        }
        else {
            int cartProdAmount = getProdCartAmount(product);
            cartItems.put(product, cartProdAmount+productAmount);
            cartItems.remove(tempKey);
        }
        totalAmount+=productAmount;
        totalPrice = totalPrice + productAmount*product.getPrice();
    }

    //delete item from cart by id
    public void deleteItem(int id){
        Set<Product> products = cartItems.keySet();
        for (Product pr: products) {
            if (pr.getId()==id){
                totalAmount-=cartItems.get(pr);
                totalPrice-=cartItems.get(pr)*pr.getPrice();
                cartItems.remove(pr);
            }
        }
    }

    //whet adding a product necessary to rewrite amount of products, total amount in catr and total
    //price of cart
    public void reWriteAmount(int id, int amount){
        Set<Product> cartProducts = null;
        if (cartItems.size() != 0) {
            cartProducts = cartItems.keySet();
        }
        if (cartProducts != null) {
            for (Product prod : cartProducts) {
                if (id == prod.getId()) {
                    totalAmount = totalAmount - cartItems.get(prod) + amount;
                    totalPrice = totalPrice - cartItems.get(prod)*prod.getPrice() + amount*prod.getPrice();
                    cartItems.put(prod, amount);
                }
            }
        }
    }

    //check whether the cart contains product whick adding to cart
    private boolean containsProduct(Product product){
        boolean contains = false;
        Set<Product> cartProducts = null;
        if (cartItems.size() != 0) {
            cartProducts = cartItems.keySet();
        }
        if (cartProducts != null) {
            for (Product prod : cartProducts) {
                if (product.getId() == prod.getId()) {
                    contains = true;
                }
            }
        }
        return contains;
    }

    //get product amount in cart
    private int getProdCartAmount(Product product){
        int amount = 0;
        Set<Product> cartProducts = null;
        if (cartItems.size() != 0) {
            cartProducts = cartItems.keySet();
        }
        if (cartProducts != null) {
            for (Product prod : cartProducts) {
                if (product.getId() == prod.getId()) {
                    amount = cartItems.get(prod);
                    tempKey = prod;
                }
            }
        }
        return amount;
    }
}
