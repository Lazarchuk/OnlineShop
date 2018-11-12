package online.shop.dao;

import java.util.List;

public interface RegionDAO {
    List<String> getRegions();
    String getRegion(int id);
}
