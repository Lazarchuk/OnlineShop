package online.shop.controller;

import online.shop.dao.impl.DataAbstractFactory;
import online.shop.dao.impl.RegionDAO;
import online.shop.dao.impl.UserDAO;
import online.shop.model.springforms.UserAddInfo;
import online.shop.model.springforms.UserMainInfo;
import online.shop.model.springforms.UserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import online.shop.model.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private DataAbstractFactory factory;

    private UserDAO userDAO;
    private RegionDAO regionDAO;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                           @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                           HttpSession session, ModelMap model) {
        // Try to find user by cookie
        if (session.getAttribute("sessionUser") == null){
            UserDAO userDAO = factory.getUserDAO();
            User user = userDAO.getUser(emailCookie, passwordCookie);
            if (user != null){
                session.setAttribute("sessionUser", user);
            }
        }

        if (session.getAttribute("sessionUser") != null){
            regionDAO = factory.getRegionDAO();
            List<String> regions = regionDAO.getRegions();
            User sessionUser = (User)session.getAttribute("sessionUser");

            UserMainInfo userMainInfo = new UserMainInfo();
            userMainInfo.setName(sessionUser.getName());
            userMainInfo.setEmail(sessionUser.getEmail());
            userMainInfo.setGender(sessionUser.getGender());

            UserAddInfo userAddInfo = new UserAddInfo();
            userAddInfo.setRegion(sessionUser.getArea());
            userAddInfo.setComment(sessionUser.getComment());
            UserPassword userPassword = new UserPassword();

            model.addAttribute("userMainInfo", userMainInfo);
            model.addAttribute("userAddInfo", userAddInfo);
            model.addAttribute("userPassword", userPassword);
            model.addAttribute("regions", regions);
            return "profile";
        }
        else{
            return "redirect:login";
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "logout")
    public String logoutForm(@RequestParam("logout") String logout, HttpSession session,
                             HttpServletResponse response, HttpServletRequest request) {
        if (logout != null) {
            session.removeAttribute("sessionUser");
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("userEmailCookie")){
                    cookie.setValue("default");
                    response.addCookie(cookie);
                }
                if (cookie.getName().equals("userPassCookie")){
                    cookie.setValue("default");
                    response.addCookie(cookie);
                }
            }
        }
        return "redirect:login";
    }

    @RequestMapping(method = RequestMethod.POST, params = "mainInfo_hidden")
    public String editMainInfo(HttpSession session, ModelMap model, UserMainInfo userMainInfo){
        boolean validEditForm;
        boolean updated = false;
        userDAO = factory.getUserDAO();
        User sessionUser = (User) session.getAttribute("sessionUser");
        FormValidator validator = new FormValidator();
        if (validator.isValidMainInfo(userMainInfo, sessionUser, userDAO)){
            validEditForm = true;
            int a = userDAO.updateUserMainInfo(userMainInfo, sessionUser.getId());
            if (a == 1){
                updated = true;
                sessionUser.setName(userMainInfo.getName());
                sessionUser.setEmail(userMainInfo.getEmail());
                sessionUser.setGender(userMainInfo.getGender());
                session.setAttribute("sessionUser", sessionUser);
            }
        }
        else{
            validEditForm = false;
            model.addAttribute("errors", validator.getErrors());
        }
        model.addAttribute("validEditForm", validEditForm);
        model.addAttribute("updated", updated);
        model.addAttribute("userMainInfo", userMainInfo);
        return "profile";
    }

    @RequestMapping(method = RequestMethod.POST, params = "addInfo_hidden")
    public String editAddInfo(HttpSession session, ModelMap model, UserAddInfo userAddInfo){
        boolean validEditForm;
        boolean updated = false;
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<String> regions = regionDAO.getRegions();
        FormValidator validator = new FormValidator();
        if (validator.isValidAddInfo(userAddInfo)){
            validEditForm = true;
            userDAO = factory.getUserDAO();
            int a = userDAO.updateUserAddInfo(userAddInfo, sessionUser.getId());
            if (a == 1){
                updated = true;
                sessionUser.setArea(userAddInfo.getRegion());
                sessionUser.setComment(userAddInfo.getComment());
                session.setAttribute("sessionUser", sessionUser);
            }
        }
        else{
            validEditForm = false;
            model.addAttribute("errors", validator.getErrors());
        }
        model.addAttribute("validEditForm", validEditForm);
        model.addAttribute("updated", updated);
        model.addAttribute("regions", regions);
        model.addAttribute("userAddInfo", userAddInfo);
        return "profile";
    }

    @RequestMapping(method = RequestMethod.POST, params = "password_hidden")
    public String editPassword(HttpSession session, ModelMap model, UserPassword userPassword){
        boolean validEditForm;
        boolean updated = false;
        User sessionUser = (User) session.getAttribute("sessionUser");
        userDAO = factory.getUserDAO();
        String sessionUserHashPassword = userDAO.getHashPassword(sessionUser.getId());
        FormValidator validator = new FormValidator();
        if (validator.isValidUserPassword(userPassword, sessionUserHashPassword)){
            validEditForm = true;
            int a = userDAO.updateUserPassword(userPassword, sessionUser.getId());
            if (a == 1){
                updated = true;
            }
        }
        else{
            validEditForm = false;
            model.addAttribute("errors", validator.getErrors());
        }
        model.addAttribute("validEditForm", validEditForm);
        model.addAttribute("updated", updated);
        return "profile";
    }

}
