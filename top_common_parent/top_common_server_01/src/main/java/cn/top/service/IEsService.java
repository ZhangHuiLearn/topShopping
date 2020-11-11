package cn.top.service;

import cn.top.common.ProductEs;
import cn.top.util.PageList;

import java.util.List;
import java.util.Map;

public interface IEsService {

    //上架：写入es
    void save( ProductEs t);

    void saveAll( List<ProductEs> list);
    //下架：删除es
    void delete( Long id);

    PageList<ProductEs> queryProductsFromEs(Map<String, Object> map);

}
