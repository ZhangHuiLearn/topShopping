package cn.top.controller;

import cn.top.client.EsClient;
import cn.top.common.ProductEs;
import cn.top.service.IEsService;
import cn.top.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/es")
public class EsController implements EsClient {

    @Autowired
    private IEsService esService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @Override
    public void save(@RequestBody ProductEs t) {

        esService.save(t);
    }

    @RequestMapping(value = "/saveAll",method = RequestMethod.POST)
    @Override
    public void saveAll(@RequestBody List<ProductEs> list)    {

        esService.saveAll(list);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Override
    public void delete(@RequestBody Long id) {
        esService.delete(id);

    }


    @RequestMapping(value = "/queryProductsFromEs", method = RequestMethod.POST)
    @Override
    public PageList<ProductEs> queryProductsFromEs(@RequestBody Map<String, Object> map) {
        return esService.queryProductsFromEs(map);
    }


}
