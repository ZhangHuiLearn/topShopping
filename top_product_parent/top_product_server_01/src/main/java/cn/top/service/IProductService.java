package cn.top.service;

import cn.top.common.ProductEs;
import cn.top.domain.Product;
import cn.top.domain.Specification;
import cn.top.util.AjaxResult;
import cn.top.util.PageList;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-14
 */
public interface IProductService extends IService<Product> {

    /**
     *
     * @param productId
     * @param skuProperties
     * @param skuDatas
     * @return
     */
    AjaxResult managerSkuProperties(Long productId, List<Specification> skuProperties, List<Map<String, Object>> skuDatas);

    void offSale(List<Long> ids);

    void onSale(List<Long> ids);

    PageList<ProductEs> queryProductsFromEs(Map<String, Object> map);
}
