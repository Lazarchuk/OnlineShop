package online.shop.dao;

public abstract class DataAbstractFactory {
    public abstract UserDAO getUserDAO();
    public abstract ProductDAO getProductDAO();
    public abstract RegionDAO getRegionDAO();

    public static DataAbstractFactory getFactory(String source){
        switch (source) {
            case "mysql":
                return new MySQLFactory();
            default:
                return null;
        }
    }
}
