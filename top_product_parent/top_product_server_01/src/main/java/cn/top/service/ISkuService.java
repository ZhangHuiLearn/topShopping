package cn.top.service;

import cn.top.domain.Sku;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * SKU 服务类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-17
 */
public interface ISkuService extends IService<Sku> {

    String getStockById(Long id);
}
