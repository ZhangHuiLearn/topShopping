package cn.top.service.impl;

import cn.top.domain.Brand;
import cn.top.mapper.BrandMapper;
import cn.top.query.BrandQuery;
import cn.top.service.IBrandService;
import cn.top.util.PageList;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 品牌信息 服务实现类
 * </p>
 *
 * @author wbtest
 * @since 2019-05-09
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageList<Brand> queryPage(BrandQuery brandQuery) {
        PageList<Brand>  pageList =null;
        long count= brandMapper.queryPageCount(brandQuery);
        if(count>0){
            //只有有数据的时候，才发送查询sql，没有就不发送，可以节约一条sql
            List<Brand> brands = brandMapper.queryPageList(brandQuery);
            pageList= new PageList<>(count,brands);
        }else {
            pageList=new PageList<>();
        }
        return pageList;
    }
}
