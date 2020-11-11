package cn.top.service;

import cn.top.domain.Brand;
import cn.top.domain.ProductType;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品目录 服务类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-09
 */
public interface IProductTypeService extends IService<ProductType> {

    /**
     * 获取树状结构
     * @return
     */
    List<ProductType> treeData();

    /**
     * 通过分类id获取面包屑
     * @param productTypeId  商品分类的id
     * @return
     */
    List<Map<String,Object>> getCrumbs(Long productTypeId);

    List<Brand> getBrands(Long productTypeId);
}
