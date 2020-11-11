package cn.top.service.impl;

import cn.top.common.ProductEs;
import cn.top.repository.EsRepository;
import cn.top.service.IEsService;
import cn.top.util.PageList;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class EsServiceImpl implements IEsService {
    @Autowired
    private EsRepository esRepository;


    //在这里调用es的方法：
    @Override
    public void save(ProductEs t) {
        esRepository.save(t);
    }

    @Override
    public void saveAll(List<ProductEs> list) {
        esRepository.saveAll(list);
    }

    @Override
    public void delete(Long id) {
        esRepository.deleteById(id);
    }

    @Override
    public PageList<ProductEs> queryProductsFromEs(Map<String, Object> map) {

       //1:接受参数
        String keyword=map.get("keyword")==null?null:map.get("keyword").toString();
        Long productType=map.get("productType")==null?null:Long.valueOf(map.get("productType")+"");
        Long brandId=map.get("brandId")==null?null:Long.valueOf(map.get("brandId")+"");
        Integer priceMin=map.get("priceMin")==null?null:Integer.valueOf(map.get("priceMin")+"");
        Integer priceMax=map.get("priceMax")==null?null:Integer.valueOf(map.get("priceMax")+"");
        String sortField=map.get("sortField")==null?null:map.get("sortField").toString();
        String sortType=map.get("sortType")==null?null:map.get("sortType").toString();
        Integer page=map.get("page")==null?1:Integer.valueOf(map.get("page")+"");
        Integer rows=map.get("rows")==null?10:Integer.valueOf(map.get("rows")+"");
        //2：创建builder
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //3:设置条件

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //must:关键字的匹配：
        if(!StringUtils.isEmpty(keyword)){
            boolQueryBuilder.must(QueryBuilders.matchQuery("queryContent",keyword));
        }
        //filter  lt: less than <  lte： <=
        List<QueryBuilder> filter = boolQueryBuilder.filter();
        // productType brandId  priceMin priceMax
        if(productType!=null&&productType>0){
            filter.add(QueryBuilders.termsQuery("productTypeId",productType+""));
        }
        if(brandId!=null&&brandId>0){
            filter.add(QueryBuilders.termsQuery("brandId",brandId+""));
        }
        // 钱： 都是分：数据库存的钱的单位是分===》es中的钱是分： 但是前台页面传过来的是元 100进值
        // qian:max>=hou:min     &&  qian:mix <=hou:max
        if(priceMin!=null){
            if(priceMin<0){
                priceMin=0;
            }
            filter.add(QueryBuilders.rangeQuery("maxPrice").gte(priceMin*100));
        }
        // qian:max>=hou:min
        if(priceMax!=null){
            if(priceMax<0){
                priceMax=0;
            }
            filter.add(QueryBuilders.rangeQuery("minPrice").lte(priceMax*100));
        }
        builder.withQuery(boolQueryBuilder);


        //size from :分页
        builder.withPageable(PageRequest.of(page-1,rows));

        //sort 排序 : SortBuilders找方法
        if(!StringUtils.isEmpty(sortField)){
            //排序方式：
            SortOrder defaultSortType=SortOrder.DESC;
            if(!StringUtils.isEmpty(sortType)){
                if("asc".equals(sortType)){
                    defaultSortType=SortOrder.ASC;
                }
            }
            //排序字段
            if("xl".equals(sortField)){
                builder.withSort(SortBuilders.fieldSort("saleCount").order(defaultSortType));
            }

            if("xp".equals(sortField)){
                builder.withSort(SortBuilders.fieldSort("onSaleTime").order(defaultSortType));
            }
            if("jg".equals(sortField)){
                builder.withSort(SortBuilders.fieldSort("minPrice").order(defaultSortType));
            }
            if("pl".equals(sortField)){
                builder.withSort(SortBuilders.fieldSort("commentCount").order(defaultSortType));
            }
            if("rq".equals(sortField)){
                builder.withSort(SortBuilders.fieldSort("viewCount").order(defaultSortType));
            }
        }
        //4:得到query
        NativeSearchQuery query = builder.build();
        //5：执行查询
       // template.queryForPage()
        Page<ProductEs> pageEs =  esRepository.search(query);

        //6：分页对象的封装
        long totalElements = pageEs.getTotalElements();
        PageList<ProductEs> pageList=null;
        if(totalElements>0){
            pageList = new PageList<>();
            List<ProductEs> content = pageEs.getContent();
            pageList.setRows(content);
            pageList.setTotal(totalElements);
        }else {
            pageList= new PageList<>();
            pageList.setTotal(0);
        }
        return pageList;
    }
}
