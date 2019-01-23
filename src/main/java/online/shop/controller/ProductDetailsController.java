package online.shop.controller;

import online.shop.dao.impl.DataAbstractFactory;
import online.shop.dao.impl.ProductDAO;
import online.shop.dao.impl.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import online.shop.model.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/product-details")
public class ProductDetailsController {
    @Autowired
    private DataAbstractFactory factory;

    @RequestMapping(method = RequestMethod.GET)
    public String loadDetails(){
        return "redirect:home";
    }

    //load product details by ID
    @RequestMapping(method = RequestMethod.GET, params = {"prodId"})
    public String loadDetails(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                              @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                              @RequestParam("prodId") String prodId, ModelMap model, HttpSession session){
        ProductDAO productDAO = factory.getProductDAO();

        // Try to find user by cookie
        if (session.getAttribute("sessionUser") == null){
            UserDAO userDAO = factory.getUserDAO();
            User user = userDAO.getUser(emailCookie, passwordCookie);
            if (user != null){
                session.setAttribute("sessionUser", user);
            }
        }

        int productId = 0;
        List<String> categories = productDAO.getCategories();
        String maxPrice = productDAO.getMaxPrice();
        String upperPrice = maxPrice;
        String lowerPrice = "0";

        if (prodId != null){
            productId = Integer.parseInt(prodId);
        }
        Product product = productDAO.getProduct(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("lowerPrice", lowerPrice);
        model.addAttribute("upperPrice", upperPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "product-details";
    }
}
