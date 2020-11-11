package cn.top.repository;

import cn.top.common.ProductEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository  // controller  repository  service  component?区别
public interface EsRepository extends ElasticsearchRepository<ProductEs,Long> {
}
