package online.shop.controller;

import online.shop.dao.impl.DataAbstractFactory;
import online.shop.dao.impl.RegionDAO;
import online.shop.dao.impl.UserDAO;
import online.shop.model.FormValidator;
import online.shop.model.User;
import online.shop.model.springforms.UserSignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    @Autowired
    private DataAbstractFactory factory;

    private List<String> regions;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                           @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                           ModelMap model, HttpSession session){
        RegionDAO regionDAO = factory.getRegionDAO();

        // Try to find user by cookie
        if (session.getAttribute("sessionUser") == null){
            UserDAO userDAO = factory.getUserDAO();
            User user = userDAO.getUser(emailCookie, passwordCookie);
            if (user != null){
                session.setAttribute("sessionUser", user);
            }
        }

        if (session.getAttribute("sessionUser") != null){
            return "redirect:profile";
        }
        else {
            UserSignUp newUserForm = new UserSignUp();
            regions = regionDAO.getRegions();
            model.addAttribute("newUserForm", newUserForm);
            model.addAttribute("regions", regions);
            return "signup";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String regUser(HttpSession session, ModelMap model, UserSignUp newUserForm){
        RegionDAO regionDAO = factory.getRegionDAO();
        UserDAO userDAO = factory.getUserDAO();
        regions = regionDAO.getRegions();
        boolean validForm = true;
        FormValidator validator = new FormValidator();
        if (userDAO.findEmail(newUserForm.getEmail())){
            List<String> errors = new ArrayList<>();
            errors.add("This email already registered");
            validForm = false;
            model.addAttribute("errors", errors);
            model.addAttribute("regions", regions);
        }
        else {
            if (validator.isValidRegUser(newUserForm, userDAO)){
                User user = new User();
                user.setEmail(newUserForm.getEmail().toLowerCase());
                user.setPassword(newUserForm.getPassword());
                user.setName(newUserForm.getName());
                user.setGender(newUserForm.getGender());
                user.setArea(newUserForm.getArea());
                user.setComment(newUserForm.getComment());

                int id = userDAO.insertUser(user);
                if (id != 0){
                    user.setId(id);
                    session.setAttribute("sessionUser", user);
                    return "redirect:profile";
                }
            }
            else {
                validForm = false;
                model.addAttribute("errors", validator.getErrors());
                model.addAttribute("regions", regions);
            }
        }
        model.addAttribute("validForm", validForm);
        model.addAttribute("newUserForm", newUserForm);
        return "signup";
    }

}
