package online.shop.controller;

import online.shop.dao.DataAbstractFactory;
import online.shop.dao.UserDAO;
import online.shop.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    private boolean isDeniedAccess;
    private String message;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model, HttpSession session){
        if (session.getAttribute("sessionUser") == null) {
            isDeniedAccess = false;
            model.addAttribute("isDeniedAccess", isDeniedAccess);
            return "login";
        }
        else {
            return "redirect:profile";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = {"login", "pass"})
    public String submitLoginForm(@RequestParam("login") String email, @RequestParam("pass") String password, ModelMap model, HttpSession session){
        User user = login(email, password);
        if (user != null){
            session.setAttribute("sessionUser", user);
            return "redirect:profile";
        }
        else {
            isDeniedAccess = true;
            model.addAttribute("isDeniedAccess", isDeniedAccess);
            model.addAttribute("deniedMessage", message);
            model.addAttribute("login", email);
            return "login";
        }
    }

    private User login(String email, String password){
        DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
        UserDAO userDAO = factory.getUserDAO();
        User user = userDAO.getUser(email, password);
        if (user == null){
            if (userDAO.findEmail(email)){
                message = "Wrong password";
            }
            else {
                message = "No such user";
            }
        }
        return user;
    }
}
