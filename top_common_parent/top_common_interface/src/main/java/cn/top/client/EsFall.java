package cn.top.client;

import cn.top.common.ProductEs;
import cn.top.util.PageList;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EsFall implements  EsClient{

    @Override
    public void save(ProductEs o) {

    }

    @Override
    public void saveAll(List<ProductEs> list) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public PageList<ProductEs> queryProductsFromEs(Map<String, Object> map) {
        return null;
    }

}
