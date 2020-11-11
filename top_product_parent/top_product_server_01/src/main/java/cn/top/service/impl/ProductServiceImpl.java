package cn.top.service.impl;

import cn.top.client.EsClient;
import cn.top.client.PageClient;
import cn.top.common.ProductEs;
import cn.top.domain.Product;
import cn.top.domain.ProductExt;
import cn.top.domain.Sku;
import cn.top.domain.Specification;
import cn.top.mapper.ProductExtMapper;
import cn.top.mapper.ProductMapper;
import cn.top.mapper.SkuMapper;
import cn.top.service.IProductService;
import cn.top.service.IProductTypeService;
import cn.top.util.AjaxResult;
import cn.top.util.PageList;
import cn.top.util.TopConstants;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-14
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductExtMapper productExtMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EsClient esClient;

    @Autowired
    private PageClient pageClient;

    @Autowired
    private IProductTypeService productTypeService;

    @Override
    public boolean insert(Product entity) {
        //先保存主表：mybatis保存后，可以设置获取主键？？？？面试题
        entity.setCreateTime(new Date().getTime());
        boolean result = super.insert(entity);

        //再保存关联表：要初始化关联表：
        ProductExt productExt = entity.getProductExt();
        if(productExt!=null){
            Long productId = entity.getId();
            productExt.setProductId(productId);
            //有前台提交描述字段或者图文详情字段
           // productExtMapper.insert(productExt);
        }else{
            productExt=new ProductExt();
            Long productId = entity.getId();
            productExt.setProductId(productId);
            //  没有前台提交描述字段或者图文详情字段
           // productExtMapper.insert(productExt);
        }
        productExtMapper.insert(productExt);


        return result;
    }

    /**
     *
     * @param productId   56
     * @param skuProperties
     * [{"id": 6,"productTypeId": 3,"specName": "型号","type": 1,"skuValues": [0, 1]},{"id": 67,"productTypeId": 3,"specName": "套餐类型","type": 1,"skuValues": ["dabeizi"]}]
     * @param skuDatas
     * [{"套餐类型": "dabeizi","型号": "xx","availableStock": 20,"price": 100}, {"套餐类型": "dabeizi","型号": "xxl","alailableStock": 2,"price": 10}]
     * @return
     */
    @Override
    public AjaxResult managerSkuProperties(Long productId, List<Specification> skuProperties, List<Map<String, Object>> skuDatas) {
        //1：ext表的更新
        ProductExt ext= new ProductExt();
        // skuProperties==>字符串
        ext.setSkuProperties(JSON.toJSONString(skuProperties));
        Wrapper<ProductExt> wrapper =new EntityWrapper<>();
        wrapper.eq("productId",productId);
        productExtMapper.update(ext,wrapper);
     //2：sku表的保存：多条数据
        for (Map<String, Object> skuData : skuDatas) {
            //  {"套餐类型": "dabeizi","型号": "xx","alailableStock": 20,"price": 100}
          //2.1:每一个skuData就应该封装到一个sku对象：
             Sku sku = new Sku();
           //2.2：封装这个sku对象值
            //2.2.1:基本字段的设置
            sku.setProductId(productId);
            sku.setCreateTime(new Date().getTime());
            //2.2.2:根据skuDatas的值来设置：
            // 每一个skuData:{"套餐类型": "dabeizi","型号": "xx","alailableStock": 20,"price": 100}
            Set<Map.Entry<String, Object>> entrySet = skuData.entrySet();

            //用来存其它属性的map的数据：
            List<Map<String,Object>> otherList = new ArrayList<>();
            /**
             *
             * [{"套餐类型": "dabeizi"},{"型号": "xx"}]
             *
             * ===>
             * [{"id":1,"key":"套餐类型","value":"dabeizi"},{"id":2,"key":"型号","value":"xx"}]
             *
             */
            for (Map.Entry<String, Object> es : entrySet) {
                // 遍历的map：  套餐类型": "dabeizi",    "型号": "xx"  。。。
                String key = es.getKey();
                Object value = es.getValue();
                if("price".equals(key)){
                    sku.setPrice(Integer.valueOf(value+""));
                }else if("availableStock".equals(key)){
                    // availableStock
                    sku.setAvailableStock(Integer.valueOf(value+""));
                }else{
                    //其它的需要构造的属性值： skuCode,skuName, skuProperties
                    // "套餐类型": "dabeizi",
                    Map<String,Object> otherMap = new HashMap<>();
                   // {"id":1,"key":"套餐类型","value":"dabeizi"}
                    otherMap.put("id",getSpeId(key,skuProperties));
                    otherMap.put("key",key);
                    otherMap.put("value",value);
                    otherList.add(otherMap);
                }
            }
            //otherList:[{"id":1,"key":"套餐类型","value":"dabeizi"},{"id":2,"key":"型号","value":"xx"}]
            // 2.2.2.1：设置skuProperties
            sku.setSkuProperties(JSON.toJSONString(otherList));

            // 2.2.2.2：设置skuCode: 遍历otherList,通过id和skuProperties比对，获取到一条数据的skuValues:再遍历skuValues，得到下标：
           StringBuffer skuCode = new StringBuffer();
            StringBuffer skuName = new StringBuffer();
            for (Map<String, Object> map : otherList) {
                getCodeAndName(skuProperties, skuCode, skuName, map);
            }
            //删除skuCode的最后一个_:   0_1_2_
            String skuCodeString = getStringSub(skuCode);
            sku.setSkuCode(skuCodeString);

            String skuNameString = getStringSub(skuName);
            sku.setSkuName(skuNameString);

            //2.3:保存这个sku对象
            skuMapper.insert(sku);
        }

        return AjaxResult.me();
    }



    /**
     * 去掉给定字符串的最后一个_字符
     * @param string  "0_1_"
     * @return "0_1"
     */
    private String getStringSub(StringBuffer string) {
        String skuString =  string.toString();
        skuString = skuString.substring(0,skuString.length()-1);
        return skuString;
    }

    /**
     * 构造skuCode(0_1_) 和skuName(z_l_)
     * @param skuProperties  前台传过来的值：
     * @param skuCode 最终需要的skuCode
     * @param skuName 最终需要的skuName
     * @param map
     */
    private void getCodeAndName(List<Specification> skuProperties, StringBuffer skuCode, StringBuffer skuName, Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id")+"");
        String value = map.get("value")+"";
        // [{"id": 6,"productTypeId": 3,"specName": "型号","type": 1,"skuValues": [0, 1]},
        //{"id": 67,"productTypeId": 3,"specName": "套餐类型","type": 1,"skuValues": ["dabeizi"]}]
        for (Specification skuProperty : skuProperties) {
            // {"id": 6,"productTypeId": 3,"specName": "型号","type": 1,"skuValues": [0, 1]}
            Long speId = skuProperty.getId();
            if(id.equals(speId)){
                //获取skuValues:
                List<String> skuValues = skuProperty.getSkuValues();
                int j=0;
                for (String skuValueString : skuValues) {
                    if(value.equals(skuValueString)){
                        //链式编程：
                        skuCode.append(j).append("_");
                        skuName.append(value).append("_");
                        return ;
                    }
                    j++;
                }
            }
        }
    }

    /**
     * 获取属性的id
     * @param key  sku的属性
     * @param skuProperties 前台提交的skuProperties
     *                      [{"id": 6,"productTypeId": 3,"specName": "型号","type": 1,"skuValues": [0, 1]},
     *                      {"id": 67,"productTypeId": 3,"specName": "套餐类型","type": 1,"skuValues": ["dabeizi"]}]
     * @return
     */
    private Long getSpeId(String key, List<Specification> skuProperties) {
        System.out.println("9999999999999999999999999");

        for (Specification skuProperty : skuProperties) {
            // {"id": 6,"productTypeId": 3,"specName": "型号","type": 1,"skuValues": [0, 1]}
            String specName = skuProperty.getSpecName();
            if(specName.equals(key)){
                return skuProperty.getId();
            }
        }
        return null;
    }


    @Override
    public void offSale(List<Long> ids) {

        //1:更新数据的状态和时间  sql语句：有一个批量处理
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("offSaleTime",new Date().getTime());
        paramMap.put("ids",ids);
        productMapper.updateOffBatch(paramMap);
        //2:删除数据es中的：
        for (Long id : ids) {
         esClient.delete(id);
        }


    }

    @Override
    public void onSale(List<Long> ids) {
         //1:更新数据的状态和时间  sql语句：有一个批量处理
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("onSaleTime",new Date().getTime());
        paramMap.put("ids",ids);
        productMapper.updateOnBatch(paramMap);

        //2:从数据库查询数据：转换成es需要的document对象
        List<Product> productDb= productMapper.selectBatchIds(ids);
        List<ProductEs> productEsList = new ArrayList();
        for (Product product : productDb) {
            //每一个商品生产静态页面：
            Map<String, Object> map=getDetailPageMap(product);
            pageClient.createPage(map);
            // product===>ProductEs
            ProductEs productEs = productDbToes(product);
            productEsList.add(productEs);
        }
        esClient.saveAll(productEsList);
    }

    /**
     * 通过product获取生成详情页面的数据
     * @param product 选择的商品
     * @return
     */
    private Map<String,Object> getDetailPageMap(Product product) {
        //调用封装的工具类：
        //Object model=map.get(TopConstants.PAGE_MODEL);
        //String templateFilePathAndName=(String)map.get(TopConstants.PAGE_TEMPLATEFILEPATHANDNAME);
        //String targetFilePathAndName=(String)map.get(TopConstants.PAGE_TARGETFILEPATHANDNAME);
        Map<String,Object> map  = new HashMap<>();
        Map<String,Object> detailDataMap  = getDetailDataMap(product);
        map.put(TopConstants.PAGE_MODEL,detailDataMap);
        map.put(TopConstants.PAGE_TEMPLATEFILEPATHANDNAME,"E:\\ideaworkspace\\aigou_parent\\aigou_common_parent\\aigou_common_interface\\src\\main\\resources\\template\\product-detail.vm");
        map.put(TopConstants.PAGE_TARGETFILEPATHANDNAME,"E:\\ideaworkspace\\vue_parent\\aigou_home\\"+product.getId()+".html");
        return map;
    }

    /**
     * 通过product获取详情页面需要的数据
     * @param product 选择的商品
     * @return
     */
    private Map<String,Object> getDetailDataMap(Product product) {
        Map<String,Object> map = new HashMap<>();
        Long productId = product.getId();
        // 项目的根地址：
        map.put("staticRoot","E:\\ideaworkspace\\aigou_parent\\aigou_common_parent\\aigou_common_interface\\src\\main\\resources\\");
        //面包屑：
        Long productTypeId = product.getProductType();
        List<Map<String, Object>> crumbs = productTypeService.getCrumbs(productTypeId);
        map.put("crumbs",crumbs);

        //基本属性：
        map.put("product",product);
        // 媒体属性： 和商品是： * ：1 ，通过外键查询
        //todo:以后你应该通过外键去查询media的表的数据，这里我们就直接写死它的图片的地址就ok
        List<List<String>> mediaList= new ArrayList<>();
        List<String> listString1 = new ArrayList<>();
        // 数据库存的是一个图片，页面需要的是三个图片：三个图片的不同点就是大小不一样：

        //解决方案：1：在存图片的时候，我就每一个图片存三个 2：七牛云存储：存的时候，是存的原图一张，你可以调用api：传入尺寸大小，它会截取后返回你需要的尺寸
        //3:如果我们只存一个图片，返回给前台，它自己去截取它的尺寸。
        listString1.add("img/test/TB2K2cAaICO.eBjSZFzXXaRiVXa_!!1739732836(2).jpg");
        listString1.add("img/test/TB2K2cAaICO.eBjSZFzXXaRiVXa_!!1739732836.jpg");
        listString1.add("img/test/TB2K2cAaICO.eBjSZFzXXaRiVXa_!!1739732836(1).jpg");
        mediaList.add(listString1);

        List<String> listString2 = new ArrayList<>();
        listString2.add("img/test/TB2K2cAaICO.eBjSZFzXXaRiVXa_!!1739732836(2).jpg");
        listString2.add("img/test/TB2K2cAaICO.eBjSZFzXXaRiVXa_!!1739732836.jpg");
        listString2.add("img/test/TB2K2cAaICO.eBjSZFzXXaRiVXa_!!1739732836(1).jpg");
        mediaList.add(listString2);
        //要转成字符串，因为前台需要的就是字符串的格式
        map.put("medias",JSON.toJSONString(mediaList));

        //  图文详情 ext和product是一对一的：
        ProductExt productExt = new ProductExt();
        productExt.setProductId(productId);
        productExt = productExtMapper.selectOne(productExt);
        map.put("productExt",productExt);

        //  显示属性
        String viewProperties = productExt.getViewProperties();
        map.put("viewProperties",JSON.parseArray(viewProperties,Specification.class));


        //skus：这个商品的所有的sku的集合: 来自sku表：一个商品有多个sku记录：
       Wrapper<Sku> skuWrapper = new EntityWrapper<>();
        skuWrapper.eq("productId", productId);
        List<Sku> skus = skuMapper.selectList(skuWrapper);
        map.put("skus", JSON.toJSONString(skus));
        //skuOptionStrs：这个商品的sku属性：ext表，字符串
        map.put("skuOptionStrs",productExt.getSkuProperties());
        //skuOptions：这个商品的sku属性：ext表，是一个数组
        map.put("skuOptions",JSON.parseArray(productExt.getSkuProperties(),Specification.class));

        return map;

    }


    /**
     * 把product 转换成 ProductEs
     * @param product
     * @return
     */
    private ProductEs productDbToes(Product product) {
        ProductEs productEs = new ProductEs();
        productEs.setId(product.getId());
        productEs.setQueryContent(product.getName()+" "+product.getSubName());
        productEs.setProductTypeId(product.getProductType());
        productEs.setBrandId(product.getBrandId());
        productEs.setMinPrice(product.getMinPrice());
        productEs.setMaxPrice(product.getMaxPrice());
        productEs.setSaleCount(product.getSaleCount());
        productEs.setCommentCount(product.getCommentCount());
        productEs.setOnSaleTime(product.getOnSaleTime());
        productEs.setViewCount(product.getViewCount());
        return productEs;
    }


    @Override
    public PageList<ProductEs> queryProductsFromEs(Map<String, Object> map) {
        return esClient.queryProductsFromEs(map);
    }
}
