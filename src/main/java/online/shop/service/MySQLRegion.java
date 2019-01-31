package online.shop.service;

import online.shop.dao.impl.RegionDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLRegion implements RegionDAO {
    private Connection connection;
    public MySQLRegion(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<String> getRegions(){
        String sql = "SELECT region FROM regions";
        List<String> regions = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                regions.add(resultSet.getString("region"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regions;
    }

    @Override
    public String getRegion(int id){
        String region = "NONE";
        String sql = "SELECT region FROM regions WHERE id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                region = resultSet.getString("region");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return region;
    }
}
