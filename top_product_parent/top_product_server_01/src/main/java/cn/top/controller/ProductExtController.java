package cn.top.controller;

import cn.top.domain.Specification;
import cn.top.query.ProductExtQuery;
import cn.top.service.IProductExtService;
import cn.top.domain.ProductExt;
import cn.top.util.AjaxResult;
import cn.top.util.PageList;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productExt")
public class ProductExtController {
    @Autowired
    public IProductExtService productExtService;

    /**
    * 保存和修改公用的
    * @param productExt  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody ProductExt productExt){
        try {
            if(productExt.getId()!=null){
                productExtService.updateById(productExt);
            }else{
                productExtService.insert(productExt);
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
            productExtService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMsg("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ProductExt get(@RequestParam(value="id",required=true) Long id)
    {
        return productExtService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<ProductExt> list(){

        return productExtService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<ProductExt> json(@RequestBody ProductExtQuery query)
    {
        Page<ProductExt> page = new Page<ProductExt>(query.getPage(),query.getRows());
            page = productExtService.selectPage(page);
            return new PageList<ProductExt>(page.getTotal(),page.getRecords());
    }

    // "/product/productExt/updateViewProperties";
// var params ={"productId":productId,"viewProperties":this.viewProperties};
    //后台接收前台的参数：面试：springmvc：后台怎么接收前台的参数？？？？？？
    // 答：1):request.getParameter("name")  2):形参的申明名字和前台对应上，自动封装：参数比较少的时候
    // 3)参数比较多的时候：就封装成一个对象domain：同名原则：自动封装到对象的属性上
    // 4):有时候，传参数是一些临时，可能后台就没有这样的一个domain：要么自己写一个domain(一般不愿意写)；直接使用map

    //现在我们怎么办：
    // 1):@RequestParam("productId") Long productId,
    //    @RequestParam("viewProperties") List<Specification> viewProperties
    // 2):@RequestBody Map<String,Object> map
    @RequestMapping(value = "/updateViewProperties",method = RequestMethod.POST)
    public AjaxResult updateViewProperties(@RequestBody Map<String,Object> map)
    {
        try {
            System.out.println(map.get("productId"));
            System.out.println(map.get("viewProperties"));
            // :java.lang.Integer cannot be cast to java.lang.Long
            Long productId = Long.valueOf(map.get("productId")+"");
            List<Specification> viewProperties = (List<Specification>) map.get("viewProperties");
            String jsonString = JSON.toJSONString(viewProperties);
            // 应该逻辑写在service中：我这里方便步梯；
            ProductExt entity=new ProductExt();
            entity.setViewProperties(jsonString);
            Wrapper<ProductExt> wrapper=new EntityWrapper<>();
            wrapper.eq("productId",productId);
            productExtService.update(entity,wrapper);
            return AjaxResult.me().setSuccess(true).setMsg("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMsg("操作失败:"+e.getMessage());
        }
    }
}
