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
@RequestMapping("/product-details")
public class ProductDetailsController {

    private DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
    private ProductDAO productDAO = factory.getProductDAO();

    @RequestMapping(method = RequestMethod.GET)
    public String loadDetails(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                              @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                              ModelMap model, HttpSession session){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

        List<String> categories = productDAO.getCategories();
        model.addAttribute("categories", categories);
        return "redirect:home";
    }

    //load product details by ID
    @RequestMapping(method = RequestMethod.GET, params = {"prodId"})
    public String loadDetails(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                              @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                              @RequestParam("prodId") String prodId, ModelMap model, HttpSession session){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

        int productId = 0;
        List<String> categories = productDAO.getCategories();

        if (prodId != null){
            productId = Integer.parseInt(prodId);
        }
        Product product = productDAO.getProduct(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "product-details";
    }
}
