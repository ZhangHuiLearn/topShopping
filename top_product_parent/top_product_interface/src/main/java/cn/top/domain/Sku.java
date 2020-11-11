package cn.top.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * SKU
 * </p>
 *
 * @author wbtest
 * @since 2019-05-17
 */
@TableName("t_sku")
public class Sku extends Model<Sku> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * SKU编码
     */
    private String skuCode;
    private String skuName;
    /**
     * 市场价
     */
    private Integer marketPrice;
    /**
     * 优惠价
     */
    private Integer price;
    /**
     * 成本价
     */
    private Integer costPrice;
    /**
     * 销量
     */
    private Integer saleCount;
    /**
     * 排序
     */
    private Integer sortIndex;
    /**
     * 可用库存
     */
    private Integer availableStock;
    /**
     * 锁定库存
     */
    private Integer frozenStock;
    /**
     * SKU属性摘要
     */
    private String skuProperties;
    /**
     * 预览图
     */
    private String skuMainPic;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Integer getFrozenStock() {
        return frozenStock;
    }

    public void setFrozenStock(Integer frozenStock) {
        this.frozenStock = frozenStock;
    }

    public String getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(String skuProperties) {
        this.skuProperties = skuProperties;
    }

    public String getSkuMainPic() {
        return skuMainPic;
    }

    public void setSkuMainPic(String skuMainPic) {
        this.skuMainPic = skuMainPic;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Sku{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", productId=" + productId +
        ", skuCode=" + skuCode +
        ", skuName=" + skuName +
        ", marketPrice=" + marketPrice +
        ", price=" + price +
        ", costPrice=" + costPrice +
        ", saleCount=" + saleCount +
        ", sortIndex=" + sortIndex +
        ", availableStock=" + availableStock +
        ", frozenStock=" + frozenStock +
        ", skuProperties=" + skuProperties +
        ", skuMainPic=" + skuMainPic +
        "}";
    }
}
