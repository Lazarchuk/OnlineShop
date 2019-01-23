package online.shop.dao.impl;

import online.shop.model.User;
import online.shop.model.UserAddInfo;
import online.shop.model.UserMainInfo;
import online.shop.model.UserPassword;

public interface UserDAO {
    User getUser(String email, String password);
    User findUserByCookie(String emailCookie, String passwordCookie);
    int insertUser(User user);
    String getHashPassword(int id);
    boolean findEmail(String email);
    int updateUserMainInfo(UserMainInfo userMainInfo, int userId);
    int updateUserAddInfo(UserAddInfo userAddInfo, int userId);
    int updateUserPassword(UserPassword userPassword, int userId);
}
