package cn.top.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品属性
 * </p>
 *
 * @author wbtest
 * @since 2019-05-15
 */
@TableName("t_specification")
public class Specification extends Model<Specification> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 规格名称
     */
    private String specName;
    private Long productTypeId;
    /**
     * 1-sku属性   2-显示属性
     */
    private Integer type;

    @TableField(exist = false)
    private String viewValue;//用于接受前台对这个属性输入的值：

    // skuValues:
    @TableField(exist = false)
    private List<String> skuValues = new ArrayList<>();//用于接受前台对这个属性输入的值：

    public List<String> getSkuValues() {
        return skuValues;
    }

    public void setSkuValues(List<String> skuValues) {
        this.skuValues = skuValues;
    }

    public String getViewValue() {
        return viewValue;
    }

    public void setViewValue(String viewValue) {
        this.viewValue = viewValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Specification{" +
        ", id=" + id +
        ", specName=" + specName +
        ", productTypeId=" + productTypeId +
        ", type=" + type +
        "}";
    }
}
