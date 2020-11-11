package cn.top.mapper;

import cn.top.domain.Product;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author wbtest
 * @since 2019-05-14
 */
public interface ProductMapper extends BaseMapper<Product> {

    void updateOnBatch(Map<String, Object> paramMap);
    void updateOffBatch(Map<String, Object> paramMap);
}
