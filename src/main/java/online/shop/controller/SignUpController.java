package online.shop.controller;

import online.shop.dao.DataAbstractFactory;
import online.shop.dao.RegionDAO;
import online.shop.dao.UserDAO;
import online.shop.model.User;
import online.shop.model.UserSignUp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private List<String> errors;
    private List<String> regions;
    private DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
    private RegionDAO regionDAO = factory.getRegionDAO();

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model, HttpSession session){
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
        UserDAO userDAO = factory.getUserDAO();
        regions = regionDAO.getRegions();
        boolean validForm = true;
        if (userDAO.findEmail(newUserForm.getEmail())){
            errors = new ArrayList<>();
            errors.add("This email already registered");
            validForm = false;
            model.addAttribute("errors", errors);
            model.addAttribute("regions", regions);
        }
        else {

            if (isValidRegUser(newUserForm)){
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
                model.addAttribute("errors", errors);
                model.addAttribute("regions", regions);
            }
        }
        model.addAttribute("validForm", validForm);
        model.addAttribute("newUserForm", newUserForm);
        return "signup";
    }

    private boolean isValidRegUser(UserSignUp newUserForm) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(newUserForm.getEmail());
        boolean valid = true;
        errors = new ArrayList<>();
        if (newUserForm.getEmail().length() == 0){
            errors.add("Enter your email");
            valid = false;
        }
        if (newUserForm.getEmail().length() > 30){
            errors.add("Email should consist up to 30 symbols");
            valid = false;
        }
        if (!matcher.matches() && newUserForm.getEmail().length() != 0){
            errors.add("Invalid email");
            valid = false;
        }
        if (newUserForm.getPassword().length() == 0){
            errors.add("Enter your password");
            valid = false;
        }
        if ((newUserForm.getPassword().length() < 8 || newUserForm.getPassword().length() > 50) &&
                newUserForm.getPassword().length() != 0){
            errors.add("Password should consist with 8 to 50 symbols");
            valid = false;
        }
        if (!newUserForm.getPassword().equals(newUserForm.getSubmitPass())){
            errors.add("Wrong submit password");
            valid = false;
        }
        if (newUserForm.getName().length() == 0){
            errors.add("Enter your name");
            valid = false;
        }
        if (newUserForm.getName().length() > 20){
            errors.add("Name should consist up to 20 symbols");
            valid = false;
        }
        if (newUserForm.getGender().equals("Unknown")){
            errors.add("Choose gender");
            valid = false;
        }
        if (newUserForm.getArea().equals("NONE")){
            errors.add("Choose region");
            valid = false;
        }
        if (newUserForm.getComment().length() == 0) {
            errors.add("Leave your comment");
            valid = false;
        }
        if (newUserForm.getComment().length() > 50){
            errors.add("Comment should consist up to 50 symbols");
            valid = false;
        }
        if (!newUserForm.isAgreement()){
            errors.add("You should to agree to the terms and conditions");
            valid = false;
        }
        return valid;
    }

}
