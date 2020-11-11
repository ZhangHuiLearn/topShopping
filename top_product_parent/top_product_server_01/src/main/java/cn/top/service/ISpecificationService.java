package cn.top.service;

import cn.top.domain.Specification;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-15
 */
public interface ISpecificationService extends IService<Specification> {
    List<Specification> queryViewProperties(Long productTypeId,Long productId);

    List<Specification> querySkuProperties(Long productTypeId);
}
