package cn.top.controller;

import cn.top.domain.Brand;
import cn.top.query.ProductTypeQuery;
import cn.top.service.IProductTypeService;
import cn.top.domain.ProductType;
import cn.top.util.AjaxResult;
import cn.top.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productType")
public class ProductTypeController {
    @Autowired
    public IProductTypeService productTypeService;

    /**
    * 保存和修改公用的
    * @param productType  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody ProductType productType){
        try {
            if(productType.getId()!=null){
                productTypeService.updateById(productType);
            }else{
                productTypeService.insert(productType);
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
            productTypeService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMsg("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ProductType get(@RequestParam(value="id",required=true) Long id)
    {
        return productTypeService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<ProductType> list(){

        return productTypeService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<ProductType> json(@RequestBody ProductTypeQuery query)
    {
        Page<ProductType> page = new Page<ProductType>(query.getPage(),query.getRows());
            page = productTypeService.selectPage(page);
            return new PageList<ProductType>(page.getTotal(),page.getRecords());
    }


    // /productType/treeData
    @RequestMapping(value = "/treeData",method = RequestMethod.GET)
    public List<ProductType> treeData() {

        return  productTypeService.treeData();
    }

    /**
     * 获取面包屑：
     * @param productTypeId 选择的当前的商品分类
     * @return
     */
    @RequestMapping(value = "/crumbs/{productTypeId}",method = RequestMethod.GET)
    public List<Map<String,Object>> crumbs(@PathVariable("productTypeId") Long productTypeId) {


        return  productTypeService.getCrumbs(productTypeId);
    }

    /**
     * 获取品牌
     * @param productTypeId
     * @return
     */
    @RequestMapping(value = "/brands/{productTypeId}",method = RequestMethod.GET)
    public List<Brand> brands(@PathVariable("productTypeId") Long productTypeId) {

        return  productTypeService.getBrands(productTypeId);
    }
}
