package online.shop.controller;

import online.shop.dao.impl.DataAbstractFactory;
import online.shop.dao.impl.ProductDAO;
import online.shop.dao.impl.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import online.shop.model.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private DataAbstractFactory factory;
    private List<Product> products;
    private List<String> categories;
    private String maxPrice;
    private String lowerPrice;
    private String upperPrice;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ModelAndView initPage(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                                 @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                                 ModelMap model, HttpSession session){
        ProductDAO productDAO = factory.getProductDAO();

        // Try to find user by cookie
        if (session.getAttribute("sessionUser") == null){
            UserDAO userDAO = factory.getUserDAO();
            User user = userDAO.getUser(emailCookie, passwordCookie);
            if (user != null){
                session.setAttribute("sessionUser", user);
            }
        }

        categories = productDAO.getCategories();
        products = productDAO.getAllProducts();
        maxPrice = productDAO.getMaxPrice();
        upperPrice = maxPrice;
        lowerPrice = "0";
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("lowerPrice", lowerPrice);
        model.addAttribute("upperPrice", upperPrice);
        model.addAttribute("maxPrice", maxPrice);
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET, params = {"category"})
    public ModelAndView productsByCategory(@RequestParam("category") String category, ModelMap model, HttpSession session,
                            @CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                            @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie){
        ProductDAO productDAO = factory.getProductDAO();

        if (!category.equals("") && !category.equals("All")){
            categories = productDAO.getCategories();
            products = productDAO.getProductsByCategory(category);
            maxPrice = productDAO.getMaxPrice();
            upperPrice = maxPrice;
            lowerPrice = "0";
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            model.addAttribute("category", category);
            model.addAttribute("lowerPrice", lowerPrice);
            model.addAttribute("upperPrice", upperPrice);
            model.addAttribute("maxPrice", maxPrice);
        }
        else {
            initPage(emailCookie, passwordCookie, model, session);
        }
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET, params = "price_range")
    public ModelAndView productsByPriceRange(@RequestParam("price_range") String range, HttpSession session, ModelMap model,
                                       @CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                                       @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie){
        ProductDAO productDAO = factory.getProductDAO();

        if (!range.equals("")){
            String[] priceValues = range.split(" : ");
            lowerPrice = priceValues[0];
            upperPrice = priceValues[1];
            maxPrice = productDAO.getMaxPrice();
            categories = productDAO.getCategories();
            products = productDAO.getProductsByPriceRange(lowerPrice, upperPrice);
            model.addAttribute("lowerPrice", lowerPrice);
            model.addAttribute("upperPrice", upperPrice);
            model.addAttribute("maxPrice", maxPrice);
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
        }
        else {
            initPage(emailCookie, passwordCookie, model, session);
        }
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET, params = {"category", "price_range"})
    public ModelAndView productsByCategoryAndPrice(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                                             @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                                             @RequestParam("category") String category, @RequestParam("price_range") String range,
                                             HttpSession session, ModelMap model){
        ProductDAO productDAO = factory.getProductDAO();

        if (!range.equals("") && !category.equals("") && !category.equals("All")){
            String[] priceValues = range.split(" : ");
            lowerPrice = priceValues[0];
            upperPrice = priceValues[1];
            products = productDAO.getProductsByCategoryAndPrice(category, lowerPrice, upperPrice);
            categories = productDAO.getCategories();
            maxPrice = productDAO.getMaxPrice();
            model.addAttribute("lowerPrice", lowerPrice);
            model.addAttribute("upperPrice", upperPrice);
            model.addAttribute("maxPrice", maxPrice);
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            model.addAttribute("category", category);
        }
        else if (!range.equals("") && !category.equals("") && category.equals("All")){
            String[] priceValues = range.split(" : ");
            lowerPrice = priceValues[0];
            upperPrice = priceValues[1];
            products = productDAO.getProductsByPriceRange(lowerPrice, upperPrice);
            categories = productDAO.getCategories();
            maxPrice = productDAO.getMaxPrice();
            model.addAttribute("lowerPrice", lowerPrice);
            model.addAttribute("upperPrice", upperPrice);
            model.addAttribute("maxPrice", maxPrice);
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            model.addAttribute("category", category);
        }
        else {
            initPage(emailCookie, passwordCookie, model, session);
        }
        return new ModelAndView("index");
    }

    @RequestMapping("/products/details/{id}")
    public ModelAndView productDetails(@PathVariable("id") String id, ModelMap model){
        ProductDAO productDAO = factory.getProductDAO();
        int productId = Integer.parseInt(id);
        List<String> categories = productDAO.getCategories();
        String maxPrice = productDAO.getMaxPrice();
        String upperPrice = maxPrice;
        String lowerPrice = "0";

        Product product = productDAO.getProduct(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("lowerPrice", lowerPrice);
        model.addAttribute("upperPrice", upperPrice);
        model.addAttribute("maxPrice", maxPrice);
        return new ModelAndView("product-details");
    }

}
