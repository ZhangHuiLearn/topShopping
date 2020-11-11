package cn.top.common;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document( indexName = "top",type = "product")
public class ProductEs {
    @Id
    private Long id;
    ///字段：来自与你的需求：查询和展示的数据
    // 关键字 :标题和副标题(品牌的名字分类的名字)
    // 分类id(ProductTypeId)  品牌id(BrandId) 价格：最小值minPrice  最大值maxPrice
    // 排序：销量saleCount  评价commentCount 新品onSaleTime  人气viewCount 价格minPrice

    @Field(type = FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String queryContent;//  name +  subName +品牌的名字+ 分类的名字

    private Long productTypeId;
    private Long brandId;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer saleCount;
    private Integer commentCount;
    private Long onSaleTime;
    private Integer viewCount;
    //可以有一个图片地址：不是用来查询的，是用来返回的：目的是list列表商品的图片展示：图片的存储使用fastdfs
    //mysql和es存的是fastdfs返回的：groupName/fileName




    //分页返回数据：价格minPrice 已售多少saleCount  名字
    //这些：以后都从mysql中查询，生成静态页面，就不放在es中
    // 详情页面： 标题和副标题(Product)， 累计评价数：commentCount
    // sku相关的东西 显示属性（扩展表）
    // 图文详情(直接从数据获取)  媒体属性:媒体属性不用于查询，所以直接从数据库获取


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Long getOnSaleTime() {
        return onSaleTime;
    }

    public void setOnSaleTime(Long onSaleTime) {
        this.onSaleTime = onSaleTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "ProductEs{" +
                "id=" + id +
                ", queryContent='" + queryContent + '\'' +
                ", productTypeId=" + productTypeId +
                ", brandId=" + brandId +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", saleCount=" + saleCount +
                ", commentCount=" + commentCount +
                ", onSaleTime=" + onSaleTime +
                ", viewCount=" + viewCount +
                '}';
    }
}
