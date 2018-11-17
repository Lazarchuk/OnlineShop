package online.shop.controller;

import online.shop.dao.DataAbstractFactory;
import online.shop.dao.UserDAO;
import online.shop.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    private boolean isDeniedAccess;
    private String message;
    DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");

    @RequestMapping(method = RequestMethod.GET)
    public String initPage(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                           @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                           ModelMap model, HttpSession session){
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

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
    public String submitLoginForm(@RequestParam("login") String email, @RequestParam("pass") String password,
                                  ModelMap model, HttpSession session){
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

    @RequestMapping(method = RequestMethod.POST, params = {"login", "pass", "keep_sign_in"})
    public String submitLoginForm(@RequestParam("login") String email, @RequestParam("pass") String password,
                                  @RequestParam("keep_sign_in") String checkbox, ModelMap model,
                                  HttpSession session, HttpServletResponse response){
        User user = login(email, password);
        if (user != null){
            session.setAttribute("sessionUser", user);

            // Save email and password in cookie
            if (checkbox.equals("check")){
                Cookie cookie1 = new Cookie("userEmailCookie", email);
                Cookie cookie2 = new Cookie("userPassCookie", password);
                cookie1.setMaxAge(1000);
                cookie2.setMaxAge(1000);
                cookie1.setPath("/OnlineShop");
                cookie2.setPath("/OnlineShop");
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }

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
