package online.shop.dao;

import online.shop.dao.impl.DataAbstractFactory;
import online.shop.dao.impl.ProductDAO;
import online.shop.dao.impl.RegionDAO;
import online.shop.dao.impl.UserDAO;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLFactory extends DataAbstractFactory {
    private Connection connection;

    public MySQLFactory(DriverManagerDataSource dataSource) {
        try {
            connection = dataSource.getConnection();
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
