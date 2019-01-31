package online.shop.model;

import online.shop.dao.impl.UserDAO;
import online.shop.model.springforms.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public boolean isValidRegUser(UserSignUp newUserForm, UserDAO userDAO) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(newUserForm.getEmail());
        boolean valid = true;
        errors = new ArrayList<>();
        if (userDAO.findEmail(newUserForm.getEmail())) {
            errors.add("This email already registered");
            valid = false;
        }
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

    public boolean isValidMainInfo(UserMainInfo userMainInfo, User sessionUser, UserDAO userDAO){
        boolean valid = true;
        errors = new ArrayList<>();
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(userMainInfo.getEmail());
        if (!userMainInfo.getEmail().toLowerCase().equals(sessionUser.getEmail())) {
            /*userDAO = factory.getUserDAO();*/
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

    public boolean isValidAddInfo(UserAddInfo userAddInfo){
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

    public boolean isValidUserPassword(UserPassword userPassword, String sessionUserHashPassword){
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
