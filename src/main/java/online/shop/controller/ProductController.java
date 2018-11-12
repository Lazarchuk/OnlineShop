package online.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import online.shop.model.*;
import online.shop.dao.*;

import java.util.List;

@Controller
@RequestMapping("/home")
public class ProductController {
    private List<Product> products;
    private List<String> categories;
    private DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
    private ProductDAO productDAO = factory.getProductDAO();

    @RequestMapping(method = RequestMethod.GET)
    public String loadHome(ModelMap model){
        categories = productDAO.getCategories();
        products = productDAO.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"category"})
    public String loadHome(@RequestParam("category") String category, ModelMap model){
        if (category != null && !category.equals("All")){
            categories = productDAO.getCategories();
            products = productDAO.getProductsByCategory(category);
            model.addAttribute("products", products);
            model.addAttribute("categories", categories);
            model.addAttribute("category", category);
        }
        else {
            loadHome(model);
        }
        return "index";
    }

}
