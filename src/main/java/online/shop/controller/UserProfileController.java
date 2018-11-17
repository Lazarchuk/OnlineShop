package online.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import online.shop.model.*;
import online.shop.dao.*;

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
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //private boolean isDeniedAccess;
    //private String message;
    private DataAbstractFactory factory = DataAbstractFactory.getFactory("mysql");
    private UserDAO userDAO;
    private RegionDAO regionDAO;
    private List<String> errors;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(@CookieValue(value = "userEmailCookie", defaultValue = "default") String emailCookie,
                           @CookieValue(value = "userPassCookie", defaultValue = "default") String passwordCookie,
                           HttpSession session, ModelMap model) {
        // Try to find user by cookie
        CookieController.findUserByCookie(emailCookie, passwordCookie, session);

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
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (isValidMainInfo(userMainInfo, sessionUser)){
            validEditForm = true;
            userDAO = factory.getUserDAO();
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
            model.addAttribute("errors", errors);
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
        if (isValidAddInfo(userAddInfo)){
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
            model.addAttribute("errors", errors);
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
        if (isValidUserPassword(userPassword, sessionUserHashPassword)){
            validEditForm = true;
            int a = userDAO.updateUserPassword(userPassword, sessionUser.getId());
            if (a == 1){
                updated = true;
            }
        }
        else{
            validEditForm = false;
            model.addAttribute("errors", errors);
        }
        model.addAttribute("validEditForm", validEditForm);
        model.addAttribute("updated", updated);
        return "profile";
    }

    private boolean isValidMainInfo(UserMainInfo userMainInfo, User sessionUser){
        boolean valid = true;
        errors = new ArrayList<>();
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(userMainInfo.getEmail());
        if (!userMainInfo.getEmail().toLowerCase().equals(sessionUser.getEmail())) {
            userDAO = factory.getUserDAO();
            if (userDAO.findEmail(userMainInfo.getEmail())) {
                errors.add("This email already registered");
                valid = false;
            }
        }
        if (userMainInfo.getEmail().length() == 0) {
            errors.add("Enter your email");
            valid = false;
        }
        if (userMainInfo.getEmail().length() > 30) {
            errors.add("Email should consist up to 30 symbols");
            valid = false;
        }
        if (!matcher.matches() && userMainInfo.getEmail().length() != 0) {
            errors.add("Invalid email");
            valid = false;
        }
        if (userMainInfo.getName().length() == 0) {
            errors.add("Enter your name");
            valid = false;
        }
        if (userMainInfo.getName().length() > 20) {
            errors.add("Name should consist up to 20 symbols");
            valid = false;
        }
        if (userMainInfo.getGender() == null) {
            errors.add("Choose gender");
            valid = false;
        }
        return valid;
    }

    private boolean isValidAddInfo(UserAddInfo userAddInfo){
        boolean valid = true;
        errors = new ArrayList<>();
        if (userAddInfo.getRegion().equals("NONE")) {
            errors.add("Choose region");
            valid = false;
        }
        if (userAddInfo.getComment().length() == 0) {
            errors.add("Leave your comment");
            valid = false;
        }
        if (userAddInfo.getComment().length() > 50) {
            errors.add("Comment should consist up to 50 symbols");
            valid = false;
        }
        return valid;
    }

    private boolean isValidUserPassword(UserPassword userPassword, String sessionUserHashPassword){
        boolean valid = true;
        errors = new ArrayList<>();
        if (userPassword.getOldPassword().length() != 0){
            String hashPassword = generateMD5(userPassword.getOldPassword());
            if (!hashPassword.equals(sessionUserHashPassword)){
                errors.add("Wrong old password");
                valid = false;
            }
        }
        if (userPassword.getOldPassword().length() == 0){
            errors.add("Enter old password");
            valid = false;
        }
        if (userPassword.getNewPassword().length() == 0){
            errors.add("Enter new password");
            valid = false;
        }
        if (userPassword.getSubmitPassword().length() == 0){
            errors.add("Submit new password");
            valid = false;
        }
        if ((userPassword.getNewPassword().length() < 8 || userPassword.getNewPassword().length() > 50) &&
                userPassword.getNewPassword().length() != 0){
            errors.add("Password should consist with 8 to 50 symbols");
            valid = false;
        }
        if (userPassword.getNewPassword().length() != 0 && userPassword.getSubmitPassword().length() != 0 &&
                !userPassword.getNewPassword().equals(userPassword.getSubmitPassword())){
            errors.add("Wrong submit password");
            valid = false;
        }
        return valid;
    }

    //Generate MD5 encrypted user password
    private String generateMD5(String password){
        String hashPass = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(password));
            hashPass = String.format("%032x", new BigInteger(md5.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashPass;
    }

}
