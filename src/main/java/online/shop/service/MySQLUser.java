package online.shop.service;

import online.shop.dao.impl.UserDAO;
import online.shop.model.User;
import online.shop.model.springforms.UserAddInfo;
import online.shop.model.springforms.UserMainInfo;
import online.shop.model.springforms.UserPassword;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUser implements UserDAO {
    private Connection connection;

    public MySQLUser(Connection connection) {
        this.connection = connection;
    }

    //Get user from DB by email and password
    @Override
    public User getUser(String email, String password) {
        String hashPass = generateMD5(password);
        String select = "SELECT * FROM users WHERE email=? AND password=?";
        User user = null;

        try {
            PreparedStatement ps = connection.prepareStatement(select);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, hashPass);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setName(resultSet.getString("name"));
                user.setGender(resultSet.getString("gender"));
                user.setArea(resultSet.getString("area"));
                user.setComment(resultSet.getString("comment"));
            }

            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User findUserByCookie(String emailCookie, String passwordCookie){
        User user = null;
        /*if (!emailCookie.equals("default") || !passwordCookie.equals("default")){
            UserDAO userDAO = factory.getUserDAO();
            user = userDAO.getUser(emailCookie, passwordCookie);
            if (user != null){
                session.setAttribute("sessionUser", user);
            }
        }*/
        return user;
    }

    //Check if DB contains specified email address
    @Override
    public boolean findEmail(String email) {
        String select = "SELECT * FROM users WHERE email=?";
        try {
            PreparedStatement ps = connection.prepareStatement(select);
            ps.setString(1, email.toLowerCase());
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return true;
            }

            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //Insert user profile into DB
    @Override
    public int insertUser(User user) {
        String hashPass = generateMD5(user.getPassword());
        String insertUser = "INSERT INTO users (email, password, name, gender, area, comment) VALUES (?, ?, ?, ?, ?, ?)";
        int userId = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(insertUser);
            ps.setString(1, user.getEmail().toLowerCase());
            ps.setString(2, hashPass);
            ps.setString(3, user.getName());
            ps.setString(4, user.getGender());
            ps.setString(5, user.getArea());
            ps.setString(6, user.getComment());
            ps.execute();
            userId = getUserId(user.getEmail(), hashPass);

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }


    @Override
    public int updateUserMainInfo(UserMainInfo userMainInfo, int userId){
        int a = 0;
        String sql = "UPDATE users SET email=?, name=?, gender=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userMainInfo.getEmail().toLowerCase());
            ps.setString(2, userMainInfo.getName());
            ps.setString(3, userMainInfo.getGender());
            ps.setInt(4, userId);
            a = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public int updateUserAddInfo(UserAddInfo userAddInfo, int userId){
        int a = 0;
        String sql = "UPDATE users SET area=?, comment=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userAddInfo.getRegion());
            ps.setString(2, userAddInfo.getComment());
            ps.setInt(3, userId);
            a = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public int updateUserPassword(UserPassword userPassword, int userId){
        int a = 0;
        String sql = "UPDATE users SET password=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, generateMD5(userPassword.getNewPassword()));
            ps.setInt(2, userId);
            a = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public String getHashPassword(int id){
        String sql = "SELECT password FROM users WHERE id=?";
        String hashPassword = "";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                hashPassword = resultSet.getString("password");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hashPassword;
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

    //Get user ID in DB by email and password
    private int getUserId(String email, String password) {
        String select = "SELECT * FROM users WHERE email=? AND password=?";
        int id = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(select);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                id = resultSet.getInt("id");
            }

            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

}
