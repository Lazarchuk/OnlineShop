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

    @RequestMapping(method = RequestMethod.GET)
    public String initPage(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                           @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                           ModelMap model, HttpSession session){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

        categories = productDAO.getCategories();
        products = productDAO.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"category"})
    public String intitPage(@RequestParam("category") String category, ModelMap model, HttpSession session,
                            @CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                            @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

        if (category != null && !category.equals("All")){
            categories = productDAO.getCategories();
            products = productDAO.getProductsByCategory(category);
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            model.addAttribute("category", category);
        }
        else {
            initPage(emailCookie, passwordCookie, model, session);
        }
        return "index";
    }

}
