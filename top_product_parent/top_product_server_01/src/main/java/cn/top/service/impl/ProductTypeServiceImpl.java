package cn.top.service.impl;

import cn.top.client.PageClient;
import cn.top.client.RedisClient;
import cn.top.domain.Brand;
import cn.top.domain.ProductType;
import cn.top.mapper.BrandMapper;
import cn.top.mapper.ProductTypeMapper;
import cn.top.service.IProductTypeService;
import cn.top.util.TopConstants;
import cn.top.util.StrUtils;
import cn.top.util.TopConstants;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品目录 服务实现类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-09
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

    @Autowired
    private RedisClient redisClient;


    @Autowired
    private PageClient pageClient;

       @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private BrandMapper brandMapper;


    @Override
    public boolean updateById(ProductType entity) {
        //根据数据库
        boolean update = super.updateById(entity);
        //同步redis：
        List<ProductType> productTypes = flushRedis();
        //根据模板，使用数据，生成一个静态页面：
        List<ProductType> result=new ArrayList<>();
        makeStructure(result,productTypes);
        //先生成productType.html
        Map<String,Object> map = new HashMap<>();
        //设置三个参数：
        //需要的数据：就是List<ProductType>
        map.put(TopConstants.PAGE_MODEL,result);
        map.put(TopConstants.PAGE_TEMPLATEFILEPATHANDNAME,"E:\\ideaworkspace\\aigou_parent\\aigou_common_parent\\aigou_common_interface\\src\\main\\resources\\template\\product.type.vm");
        map.put(TopConstants.PAGE_TARGETFILEPATHANDNAME,"E:\\ideaworkspace\\aigou_parent\\aigou_common_parent\\aigou_common_interface\\src\\main\\resources\\template\\product.type.vm.html");
        pageClient.createPage(map);

        //再生成home.html
        Map<String,Object> homeMap = new HashMap<>();
        //设置三个参数：
        Map<String,String> staticRootMap=new HashMap<>();
        //根路径：
        // model.staticRoot
        staticRootMap.put("staticRoot","E:\\ideaworkspace\\aigou_parent\\aigou_common_parent\\aigou_common_interface\\src\\main\\resources\\");
        homeMap.put(TopConstants.PAGE_MODEL,staticRootMap);
        homeMap.put(TopConstants.PAGE_TEMPLATEFILEPATHANDNAME,"E:\\ideaworkspace\\aigou_parent\\aigou_common_parent\\aigou_common_interface\\src\\main\\resources\\template\\home.vm");
        homeMap.put(TopConstants.PAGE_TARGETFILEPATHANDNAME,"E:\\ideaworkspace\\vue_parent\\aigou_home\\home.html");
        pageClient.createPage(homeMap);

        return update;
    }

    @Override
    public boolean update(ProductType entity, Wrapper<ProductType> wrapper) {
        //根据数据库
        boolean update = super.update(entity, wrapper);
        //同步redis：
        flushRedis();
        return update;

    }

    @Override
    public List<ProductType> treeData() {
        //要实现树装结构：
        //return treeDataRecursion(0L);
        return treeDataLoop();
    }



    /**
     * 1：查询出所有的数据
     * 2：组装父子结构
     * @return
     */
    public List<ProductType> treeDataLoop() {

        //返回的一级菜单
        List<ProductType> result = new ArrayList<>();

        //1：查询出所有的数据
        List<ProductType> allProductTypes=null;
        //数据从redis中获取：
        // 0：先通过key去redis获取
        String productTypeJson = redisClient.getRedis(TopConstants.COMMON_PRODUCT_TYPE);
        // 0.1:redis没有这个key对应的数据：从数据库获取，并放入redis，返回
        if(StringUtils.isEmpty(productTypeJson)){
            //redis中没有：
            allProductTypes = flushRedis();
            System.out.println("==========from db==============");
        }else{
            // 0.2:redis有，就直接返回
            allProductTypes= JSON.parseArray(productTypeJson,ProductType.class);
            System.out.println("==========from cache==============");
        }
        makeStructure(result, allProductTypes);


        return result;
    }

    private void makeStructure(List<ProductType> result, List<ProductType> allProductTypes) {
        Map<Long,ProductType> map=new HashMap<>();
        //循环：把所有的对象，放到一个map：
        for (ProductType cur : allProductTypes) {
            map.put(cur.getId(),cur);
        }
        //2：组装父子结构
        for (ProductType current : allProductTypes) {
            //找一级菜单
            if(current.getPid()==0){
                result.add(current);
            }else{
                //你不是一级菜单，你就是儿子： 你是谁的儿子？？？ 你是你老子的儿子
                ProductType parent = null;//你老子
                /*
                 嵌套循环了：不嵌套
                for (ProductType cur : allProductTypes) {
                    if(cur.getId()==current.getPid()){
                        parent=cur;
                    }
                }*/
                parent = map.get(current.getPid());
               /* List<ProductType> children = parent.getChildren();//你老子的儿子
                children.add(current);//你自己是你老子的儿子*/
               parent.getChildren().add(current);
            }

        }
    }

    private List<ProductType> flushRedis() {
        List<ProductType> allProductTypes;//从数据库查询：
        allProductTypes = productTypeMapper.selectList(null);
        //放入redis：
        String jsonArrayString = JSON.toJSONString(allProductTypes);
        redisClient.setRedis(TopConstants.COMMON_PRODUCT_TYPE,jsonArrayString);
        return allProductTypes;
    }

    /**
     * 递归：
     *   自己方法里调用自己，但是必须有一个出口：没有儿子就出去;
     *  好么：
     *   不好，因为每次都要发送sql，就要发送很多的sql：
     *     要耗时；数据库的服务器要承受很多的访问量，压力大。
     *   ====》原因是发送了很多sql，我优化就少发送，至少发送1条。
     * @return
     */
    public List<ProductType> treeDataRecursion(Long pid) {
        //所有的一级菜单
        List<ProductType> allChildren = getAllChildren(pid);
        //出口：没有儿子
        if(allChildren==null||allChildren.size()==0){
            return  null;
        }
        //遍历：找当前遍历对象的儿子：
        for (ProductType current : allChildren) {
            //找当前对象的儿子
            List<ProductType> productTypes = treeDataRecursion(current.getId());
            current.setChildren(productTypes);
        }
        return allChildren;
    }

    /**
     * 查询数据的pid=pid的：
     *  // select * from t_product_type where pid = 2
     * @param pid
     * @return
     */
    public List<ProductType> getAllChildren(Long pid) {
        Wrapper<ProductType> wrapper = new EntityWrapper<>();
        wrapper.eq("pid",pid);
        return  productTypeMapper.selectList(wrapper);
    }

    public static void main(String[] args) {
        //2:获取自己的path： .1.2.3.
        String path =".1.2.3.";
        path = path.substring(1,path.length()-1);
        List<Long> ids = StrUtils.splitStr2LongArr(path, "\\.");
        System.out.println(ids);
    }
    @Override
    public List<Map<String, Object>> getCrumbs(Long productTypeId) {
        //返回结果：
        List<Map<String, Object>> result = new ArrayList<>();

        //1:通过productTypeId获取当前分类  ProductType :  id  name ....
        ProductType productType = productTypeMapper.selectById(productTypeId);
        //2:获取自己的path： .1.2.3.
        String path = productType.getPath();
        path = path.substring(1,path.length()-1);
        List<Long> ids = StrUtils.splitStr2LongArr(path, "\\.");
        // 3:通过path获取所有的层级
        List<ProductType> productTypes = productTypeMapper.selectBatchIds(ids);
        //4：遍历所有的层级，分表找自己和自己的兄弟的姐妹。
        for (ProductType currentType : productTypes) {
            // currentType: 1   2    3
            Map<String,Object> map = new HashMap<>();
            // 当前自己：ownerProductType
            map.put("ownerProductType",currentType);
            //兄弟姐妹：otherProductTypes  自己的老子的所有的儿子，删除自己：
            //获取它老子：
            if(currentType.getPid() == 0){
                //是一级菜单：单独获取：
                Wrapper<ProductType> wrapper = new EntityWrapper<>();
                // SELECT * FROM `t_product_type` where pid =0;
                wrapper.eq("pid",0);
                List<ProductType> firstBrothers = productTypeMapper.selectList(wrapper);
                //移除自己：
                for (ProductType firstBrother : firstBrothers) {
                    if(firstBrother.getId().equals(currentType.getId())){
                        firstBrothers.remove(firstBrother);
                        //兄弟姐妹：
                        map.put("otherProductTypes",firstBrothers);
                        break;
                    }
                }
                map.put("otherProductTypes",firstBrothers);
            }else{
                ProductType parentType = productTypeMapper.selectById(currentType.getPid());
                //获取老子的所有的儿子：select * from t_product_type where pid=id
                Wrapper<ProductType> wrapper = new EntityWrapper<>();
                wrapper.eq("pid",parentType.getId());
                List<ProductType> children = productTypeMapper.selectList(wrapper);
                //在所有的儿子中删除自己：
                Long currentTypeId = currentType.getId();
                for (ProductType child : children) {
                    if(child.getId().equals(currentTypeId)){
                        children.remove(child);
                        //兄弟姐妹：
                        map.put("otherProductTypes",children);
                        break;
                    }
                }
            }

            result.add(map);
        }
        return result;
    }

    @Override
    public List<Brand> getBrands(Long productTypeId) {
        //select * from t_brand where product_type_id = ??
        return brandMapper.selectList(new EntityWrapper<Brand>().eq("product_type_id",productTypeId));
    }
}
