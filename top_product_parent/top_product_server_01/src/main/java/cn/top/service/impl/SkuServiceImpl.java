package cn.top.service.impl;

import cn.top.domain.Sku;
import cn.top.mapper.SkuMapper;
import cn.top.service.ISkuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * SKU 服务实现类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-17
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements ISkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public String getStockById(Long id) {
        Sku sku = skuMapper.selectById(id);
        Integer availableStock = sku.getAvailableStock();
        return "库存"+availableStock+"件";
    }
}
