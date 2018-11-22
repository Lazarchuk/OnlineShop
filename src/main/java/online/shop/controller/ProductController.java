package online.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import online.shop.model.*;
import online.shop.dao.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/home")
public class ProductController {
    private List<Product> products;
    private List<String> categories;
    private DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
    private ProductDAO productDAO = factory.getProductDAO();
    private String maxPrice;
    private String lowerPrice;
    private String upperPrice;

    @RequestMapping(method = RequestMethod.GET)
    public String initPage(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                           @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                           ModelMap model, HttpSession session){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

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
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"category"})
    public String productsByCategory(@RequestParam("category") String category, ModelMap model, HttpSession session,
                            @CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                            @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

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
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, params = "price_range")
    public String productsByPriceRange(@RequestParam("price_range") String range, HttpSession session, ModelMap model,
                                       @CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                                       @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

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
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"category", "price_range"})
    public String productsByCategoryAndPrice(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                                             @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                                             @RequestParam("category") String category, @RequestParam("price_range") String range,
                                             HttpSession session, ModelMap model){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

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
        else if (!category.equals("") && category.equals("All")){
            productsByPriceRange(range,session, model, emailCookie, passwordCookie);
        }
        else {
            initPage(emailCookie, passwordCookie, model, session);
        }
        return "index";
    }

}
