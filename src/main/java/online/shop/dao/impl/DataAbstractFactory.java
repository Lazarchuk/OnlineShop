package online.shop.dao.impl;

import online.shop.service.MySQLFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public abstract class DataAbstractFactory {
    public abstract UserDAO getUserDAO();
    public abstract ProductDAO getProductDAO();
    public abstract RegionDAO getRegionDAO();

    public static DataAbstractFactory getFactory(DriverManagerDataSource dataSource){
        return new MySQLFactory(dataSource);
    }
}
