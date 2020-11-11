package cn.top.controller;

import cn.top.common.ProductEs;
import cn.top.domain.Specification;
import cn.top.query.ProductQuery;
import cn.top.service.IProductService;
import cn.top.domain.Product;
import cn.top.util.*;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    public IProductService productService;

    /**
    * 保存和修改公用的
    * @param product  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Product product){
        try {
            if(product.getId()!=null){
                productService.updateById(product);
            }else{
                productService.insert(product);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMsg("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            productService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMsg("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Product get(@RequestParam(value="id",required=true) Long id)
    {
        return productService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Product> list(){

        return productService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Product> json(@RequestBody ProductQuery query)
    {
        Page<Product> page = new Page<Product>(query.getPage(),query.getRows());
            page = productService.selectPage(page);
            return new PageList<Product>(page.getTotal(),page.getRecords());
    }

    /**
     * sku的添加管理：
     * var params ={"productId":productId,"skuProperties":this.skuProperties,"skuDatas":this.skuDatas};
     * @param map
     * @return
     */
    @RequestMapping(value = "/managerSkuProperties",method = RequestMethod.POST)
    public AjaxResult managerSkuProperties(@RequestBody Map<String,Object> map)
    {
      Long  productId = Long.valueOf(map.get("productId")+"");
      //[{"id": 6,"productTypeId": 3,"specName": "型号","type": 1,"skuValues": [0, 1]},
        // {"id": 67,"productTypeId": 3,"specName": "套餐类型","type": 1,"skuValues": ["dabeizi"]}]
        List<Map<String,Object>> skuMapList = (List<Map<String, Object>>) map.get("skuProperties");

        List<Specification>  skuProperties=new ArrayList<>();
        /*for (Map<String, Object> stringObjectMap : skuMapList) {
            //每一个Map对应到一个Specificaiton对象：
            Specification specification = new Specification();
            Set<Map.Entry<String, Object>> entries = stringObjectMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                String key = entry.getKey();
                Object value = entry.getValue();
                // productTypeId   value: 3
                if(key.equals("id")){
                    specification.setId(Long.valueOf(value+""));
                }else if(key.equals("productTypeId")){
                    specification.setProductTypeId(Long.valueOf(value+""));
                }else if(key.equals("specName")){
                    specification.setSpecName(value.toString());
                }else if(key.equals("type")){
                    specification.setType(Integer.valueOf(value+""));
                }else if(key.equals("skuValues")){
                    List<String> listStr = (List<String>)value;
                    specification.setSkuValues(listStr);
                }
            }
            skuProperties.add(specification);
        }*/

        for (Map<String, Object> stringObjectMap : skuMapList) {
            //每一个Map对应到一个Specificaiton对象：
            Specification specification = new Specification();
            Map2Bean.transMap2Bean2(stringObjectMap,specification);
            skuProperties.add(specification);
        }
       // List<Specification>  skuProperties = (List<Specification>) map.get("skuProperties");
      List<Map<String,Object>> skuDatas = (List<Map<String, Object>>) map.get("skuDatas");
        System.out.println("=============================");

        System.out.println("productId:"+productId);
        System.out.println("=============================");
        System.out.println("skuProperties:"+skuProperties);
        System.out.println("=============================");
        System.out.println("skuDatas:"+skuDatas);
        System.out.println("=============================");
        return productService.managerSkuProperties(productId,skuProperties,skuDatas);
    }

   // List<Long> ids,Integer type 标识：1下架  2上架
    @RequestMapping(value = "/dealSale",method = RequestMethod.POST)
    public AjaxResult dealSale(@RequestBody Map<String,Object> map)
    {
        try {
            //1:获取参数：
            Integer type = Integer.valueOf(map.get("type")+"");
            String  ids = map.get("ids")+"";// 13,12,11
            List<Long> longs = StrUtils.splitStr2LongArr(ids);
            //2：调用对应的方法：
            if(type.equals(1)){
                //下架
                productService.offSale(longs);
            }else if(type.equals(2)){
                //上架：
                productService.onSale(longs);
            }

            return AjaxResult.me().setSuccess(true).setMsg("操作成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMsg("操作失败:"+e.getMessage());
        }

    }

    //商品的查询："/product/product/queryProducts"
    // 参数接受：封装一个Query；再一种：直接使用map接受。
    @RequestMapping(value = "/queryProductsFromEs",method = RequestMethod.POST)
    public PageList<ProductEs> queryProductsFromEs(@RequestBody Map<String,Object> map)
    {
        return productService.queryProductsFromEs(map);

    }


}
