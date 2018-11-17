package online.shop.controller;

import online.shop.dao.DataAbstractFactory;
import online.shop.dao.UserDAO;
import online.shop.model.User;

import javax.servlet.http.HttpSession;

public class CookieController {
    private static DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
    public static void findUserByCookie(String emailCookie, String passwordCookie, HttpSession session){
        // Try to find user by cookie
        if (session.getAttribute("sessionUser") == null){
            if (!emailCookie.equals("default") || !passwordCookie.equals("default")){
                UserDAO userDAO = factory.getUserDAO();
                User user = userDAO.getUser(emailCookie, passwordCookie);
                if (user != null){
                    session.setAttribute("sessionUser", user);
                }
            }
        }
    }
}
