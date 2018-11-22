package online.shop.dao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import online.shop.model.Config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLFactory extends DataAbstractFactory {
    private Connection connection;

    public MySQLFactory() {
        try {
            Context initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/myshop");
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDAO getUserDAO() {
        return new MySQLUser(connection);
    }

    @Override
    public ProductDAO getProductDAO() {
        return new MySQLProduct(connection);
    }

    @Override
    public RegionDAO getRegionDAO(){return new MySQLRegion(connection);}
}
