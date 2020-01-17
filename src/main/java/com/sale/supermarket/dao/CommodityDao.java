package com.sale.supermarket.dao;

import com.sale.supermarket.pojo.Commodity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommodityDao {
    void delete(int id);
    void update(Commodity param);
    List<Commodity> getCommodities();
    void add(Commodity commodity);
    Commodity getCommodity(@Param("id") int id);
}
