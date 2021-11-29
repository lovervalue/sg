package com.sofn.bigdata.model;

/**
 * @Acthor Tao.Lee @date 2021/3/8 13:50
 * @Description 创建lucene实体类
 */
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Product {
    private Integer id;

    private Long cid1;

    private String cid1Name;

    private Double commission;

    private Double commissionShare;

    private Integer bindType;

    private String brandCode;

    private String brandName;

    private Long comments;

    private Double discount;

    private Integer eliteId;

    private String eliteName;

    private Date getEndTime;

    private Date getStartTime;

    private String link;

    private String materialUrl;

    private String owner;

    private Double pingouPrice;

    private Long pingouTmCount;

    private String pingouUrl;

    private Double price;

    private Double quota;

    private Long shopId;

    private String shopName;

    private Long skuId;

    @ApiModelProperty("文档路径必填")
    private String skuName;

    private Long spuid;

    private String url;

    private Date useEndTime;

    private Date useStartTime;

    private String clickurl;

    private Double discountRate;

    private Date updateTime;

    private Date createTime;
    private Integer page;
    private Integer limit;
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCid1() {
        return cid1;
    }

    public void setCid1(Long cid1) {
        this.cid1 = cid1;
    }

    public String getCid1Name() {
        return cid1Name;
    }

    public void setCid1Name(String cid1Name) {
        this.cid1Name = cid1Name;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommissionShare() {
        return commissionShare;
    }

    public void setCommissionShare(Double commissionShare) {
        this.commissionShare = commissionShare;
    }

    public Integer getBindType() {
        return bindType;
    }

    public void setBindType(Integer bindType) {
        this.bindType = bindType;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getEliteId() {
        return eliteId;
    }

    public void setEliteId(Integer eliteId) {
        this.eliteId = eliteId;
    }

    public String getEliteName() {
        return eliteName;
    }

    public void setEliteName(String eliteName) {
        this.eliteName = eliteName;
    }

    public Date getGetEndTime() {
        return getEndTime;
    }

    public void setGetEndTime(Date getEndTime) {
        this.getEndTime = getEndTime;
    }

    public Date getGetStartTime() {
        return getStartTime;
    }

    public void setGetStartTime(Date getStartTime) {
        this.getStartTime = getStartTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Double getPingouPrice() {
        return pingouPrice;
    }

    public void setPingouPrice(Double pingouPrice) {
        this.pingouPrice = pingouPrice;
    }

    public Long getPingouTmCount() {
        return pingouTmCount;
    }

    public void setPingouTmCount(Long pingouTmCount) {
        this.pingouTmCount = pingouTmCount;
    }

    public String getPingouUrl() {
        return pingouUrl;
    }

    public void setPingouUrl(String pingouUrl) {
        this.pingouUrl = pingouUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getSpuid() {
        return spuid;
    }

    public void setSpuid(Long spuid) {
        this.spuid = spuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Date useEndTime) {
        this.useEndTime = useEndTime;
    }

    public Date getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(Date useStartTime) {
        this.useStartTime = useStartTime;
    }

    public String getClickurl() {
        return clickurl;
    }

    public void setClickurl(String clickurl) {
        this.clickurl = clickurl;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
