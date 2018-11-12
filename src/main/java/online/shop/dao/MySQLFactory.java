package online.shop.dao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import online.shop.model.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLFactory extends DataAbstractFactory {
    private Connection connection;

    private Config config;
    public MySQLFactory() {
        config = new Config();
        Class[] classes = new Class[]{Config.class};
        XStream xs = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xs);
        xs.allowTypes(classes);
        try {
            xs.fromXML(new FileInputStream(new File("config.xml")), config);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(config.getHost(), config.getUsername(), config.getPassword());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
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
