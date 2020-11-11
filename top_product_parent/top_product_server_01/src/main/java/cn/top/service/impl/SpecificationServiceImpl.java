package cn.top.service.impl;

import cn.top.domain.ProductExt;
import cn.top.domain.Specification;
import cn.top.mapper.ProductExtMapper;
import cn.top.mapper.SpecificationMapper;
import cn.top.service.ISpecificationService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-15
 */
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements ISpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private ProductExtMapper productExtMapper;
    @Override
    public List<Specification> queryViewProperties(Long productTypeId,Long productId) {
        //添加显示属性：select * from t_specification where productTypeId = ? and type = 2
        // List<Specification>==>Specification:没有viewValue值的

        //回显显示属性：在做添加的时候，已经保存到t_product_ext的viewProperties:
        //[{"id":1,"specName":"容量","productTypeId":3,"type":2,"viewValue":"18"},
        //{"id":2,"specName":"产品名称","productTypeId":3,"type":2,"viewValue":"大杯子"},
        //{"id":5,"specName":"颜色分类","productTypeId":3,"type":2,"viewValue":"pink"}]
        //把这个字符串数组使用FastJson转换成list对象：

        //怎么判断是添加还是修改显示属性呢？由于我们在添加商品的时候，就已经在扩展表中插入了数据了：
        //所以：我们可以先通过商品id去扩展表查询显示属性字段：
        // select * from t_product_ext where productId =?
        //判断：是否有显示属性：有就是编辑，没有就是添加

        List<Specification> result = new ArrayList<>();
        //1:查询扩展表
        ProductExt ext=new ProductExt();
        ext.setProductId(productId);
        ext=  productExtMapper.selectOne(ext);
        //2:判断是否有 显示属性：
        if(ext!=null){
            String viewProperties = ext.getViewProperties();
            if(StringUtils.isEmpty(viewProperties)){
                //4：没有：就是以前的查询
                Wrapper<Specification> wrapper = new EntityWrapper<>();
                wrapper.eq("productTypeId",productTypeId);
                wrapper.eq("type",2);
                result =specificationMapper.selectList(wrapper);
            }else{
                // 3:有：使用FastJson转
            result =    JSON.parseArray(viewProperties,Specification.class);
            }
        }

        return result;
    }

    @Override
    public List<Specification> querySkuProperties(Long productTypeId) {
        Wrapper<Specification> wrapper = new EntityWrapper<>();
        wrapper.eq("productTypeId",productTypeId);
        wrapper.eq("type",1);
      return specificationMapper.selectList(wrapper);
    }
}
