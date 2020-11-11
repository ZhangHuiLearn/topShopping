package cn.top.service;

import cn.top.domain.Brand;
import cn.top.query.BrandQuery;
import cn.top.util.PageList;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 品牌信息 服务类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-09
 */
public interface IBrandService extends IService<Brand> {

    PageList<Brand> queryPage(BrandQuery brandQuery);

}
