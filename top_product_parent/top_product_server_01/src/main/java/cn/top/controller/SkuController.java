package cn.top.controller;

import cn.top.query.SkuQuery;
import cn.top.service.ISkuService;
import cn.top.domain.Sku;
import cn.top.util.AjaxResult;
import cn.top.util.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    public ISkuService skuService;

    /**
    * 保存和修改公用的
    * @param sku  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Sku sku){
        try {
            if(sku.getId()!=null){
                skuService.updateById(sku);
            }else{
                skuService.insert(sku);
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
            skuService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMsg("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Sku get(@RequestParam(value="id",required=true) Long id)
    {
        return skuService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Sku> list(){

        return skuService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Sku> json(@RequestBody SkuQuery query)
    {
        Page<Sku> page = new Page<Sku>(query.getPage(),query.getRows());
            page = skuService.selectPage(page);
            return new PageList<Sku>(page.getTotal(),page.getRecords());
    }

    @RequestMapping(value = "/stock/{id}",method = RequestMethod.GET)
    public String  stock(@PathVariable("id") Long id)
    {
        return skuService.getStockById(id);
    }
}
