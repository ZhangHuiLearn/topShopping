package cn.top.client;

import cn.top.common.ProductEs;
import cn.top.util.PageList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(value = "common-provider", fallback = EsFall.class)
public interface EsClient {

    //上架：写入es
    @RequestMapping(value = "/common/es/save", method = RequestMethod.POST)
    void save(@RequestBody ProductEs t);


    @RequestMapping(value = "/common/es/saveAll", method = RequestMethod.POST)
    void saveAll(@RequestBody List<ProductEs> list);

    //下架：删除es
    @RequestMapping(value = "/common/es/delete", method = RequestMethod.POST)
    void delete(@RequestBody Long id);


    //下架：删除es
   /* @RequestMapping(value = "/es/deleteAll",method = RequestMethod.POST)
     void deleteAll(@RequestBody List<Long> ids);*/

    @RequestMapping(value = "/common/es/queryProductsFromEs", method = RequestMethod.POST)
    PageList<ProductEs> queryProductsFromEs(@RequestBody Map<String, Object> map);


}
