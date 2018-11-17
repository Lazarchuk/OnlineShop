package online.shop.controller;

import online.shop.dao.*;
import online.shop.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    private DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
    private ProductDAO productDAO = factory.getProductDAO();

    //loads cart content
    @RequestMapping(method = RequestMethod.GET)
    public String initPage(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                           @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                           HttpSession session , ModelMap model){

        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

        Cart cart = null;
        if (session.getAttribute("cart") != null){
            cart = (Cart) session.getAttribute("cart");
        }
        session.setAttribute("cart", cart);
        if (cart != null) {
            model.addAttribute("totalAmount", cart.getTotalAmount());
            model.addAttribute("totalPrice", cart.getTotalPrice());
        }
        return "cart";
    }

    //get product from DB and add it to the cart
    @RequestMapping(method = RequestMethod.POST, params = {"productId", "prodAmount"})
    public String loadCart(@RequestParam("productId") String productId, @RequestParam("prodAmount") String prodAmount,
                           HttpSession session){
        int productAmount = 0;
        if (prodAmount != null){
            productAmount = Integer.parseInt(prodAmount);
        }

        Cart cart;
        if (productId != null){
            Product product = productDAO.getProduct(Integer.parseInt(productId));
            if (session.getAttribute("cart") == null) {
                cart = new Cart();
            } else {
                cart = (Cart)session.getAttribute("cart");
            }
            cart.addItem(product, productAmount);
            session.setAttribute("cart", cart);
        }

        return "cart";
    }

    //edit amount of product which already exist in cart by it ID and new amount of the product
    @RequestMapping(method = RequestMethod.POST, params = {"reWriteId", "prodAmount"})
    public String loadCart(@RequestParam("reWriteId") String productId, @RequestParam("prodAmount") String prodAmount,
                           HttpSession session, ModelMap model){
        Cart cart = (Cart) session.getAttribute("cart");
        int id = 0;
        int amount = 0;
        if (productId != null){
            id = Integer.parseInt(productId);
        }
        if (prodAmount != null){
            amount = Integer.parseInt(prodAmount);
        }
        cart.reWriteAmount(id, amount);
        session.setAttribute("cart", cart);
        model.addAttribute("totalAmount", cart.getTotalAmount());
        model.addAttribute("totalPrice", cart.getTotalPrice());

        return "cart";
    }

    //delete product from cart by ID
    @RequestMapping(method = RequestMethod.POST, params = {"deleteId"})
    public String deleteFromCart(@RequestParam("deleteId") String delete, HttpSession session,
                                 ModelMap model){
        int deleteId = Integer.parseInt(delete);
        Cart cart = (Cart) session.getAttribute("cart");
        cart.deleteItem(deleteId);
        session.setAttribute("cart", cart);
        model.addAttribute("totalAmount", cart.getTotalAmount());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart";
    }
}
