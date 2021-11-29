package com.sofn.bigdata.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("县级基本信息图形分析Vo")
@Data
public class ShowDataVo {
    /**
     * 基础信息-农户总数
     */
    @ApiModelProperty("基础信息-农户总数")
    private String jcxxNhzs;

    /**
     * 基础信息-农村劳动力数量
     */
    @ApiModelProperty("基础信息-农村劳动力数量")
    private String jcxxNcldlsl;

    /**
     * 基础信息-耕地和园地总面积
     */
    @ApiModelProperty("基础信息-耕地和园地总面积")
    private String jcxxGdhydzmj;

    /**
     * 规模种植主体情况-数量
     */
    @ApiModelProperty("规模种植主体情况-数量")
    private String gmzzztqkSl;

    /**
     * 规模种植主体情况-总面积
     */
    @ApiModelProperty("规模种植主体情况-总面积")
    private String gmzzztqkZmj;

    /**
     * 规模种植主体情况-其中粮食作物
     */
    @ApiModelProperty("规模种植主体情况-其中粮食作物")
    private String gmzzztqkQzlszw;

    /**
     * 规模种植主体情况-其中经济作物
     */
    @ApiModelProperty("规模种植主体情况-其中经济作物")
    private String gmzzztqkQzjjzw;

    /**
     * 规模种植主体情况-其中蔬菜瓜果
     */
    @ApiModelProperty("规模种植主体情况-其中蔬菜瓜果")
    private String gmzzztqkQzscgg;

    /**
     * 规模种植主体情况-其中园地
     */
    @ApiModelProperty("规模种植主体情况-其中园地")
    private String gmzzztqkQzyd;

    /**
     * 不同坡度耕地和园地面积-平地
     */
    @ApiModelProperty("不同坡度耕地和园地面积-平地")
    private String btpdgdhydmjPd;

    /**
     * 不同坡度耕地和园地面积-缓坡地
     */
    @ApiModelProperty("不同坡度耕地和园地面积-缓坡地")
    private String btpdgdhydmjHpd;

    /**
     * 不同坡度耕地和园地面积-陡坡地
     */
    @ApiModelProperty("不同坡度耕地和园地面积-陡坡地")
    private String btpdgdhydmjDpd;

    /**
     * 耕地面积-耕地总面积
     */
    @ApiModelProperty("耕地面积-耕地总面积")
    private String gdmjGdzmj;

    /**
     * 耕地面积-旱地
     */
    @ApiModelProperty("耕地面积-旱地")
    private String gdmjHd;

    /**
     * 耕地面积-水田
     */
    @ApiModelProperty("耕地面积-水田")
    private String gdmjSt;

    /**
     * 菜地面积-露地
     */
    @ApiModelProperty("菜地面积-露地")
    private String cdmjLd;

    /**
     * 菜地面积-保护地
     */
    @ApiModelProperty("菜地面积-保护地")
    private String cdmjBhd;

    /**
     * 园地面积-园地总面积
     */
    @ApiModelProperty("园地面积-园地总面积")
    private String ydmjYdzmj;

    /**
     * 园地面积-果园
     */
    @ApiModelProperty("园地面积-果园")
    private String ydmjGy;

    /**
     * 园地面积-茶园
     */
    @ApiModelProperty("园地面积-茶园")
    private String ydmjCy;

    /**
     * 园地面积-桑园
     */
    @ApiModelProperty("园地面积-桑园")
    private String ydmjSy;

    /**
     * 园地面积-其他
     */
    @ApiModelProperty("园地面积-其他")
    private String ydmjQt;

    /**
     * 果园面积-苹果
     */
    @ApiModelProperty("果园面积-苹果")
    private String gymjPg;

    /**
     * 果园面积-梨
     */
    @ApiModelProperty("果园面积-梨")
    private String gymjL;

    /**
     * 果园面积-葡萄
     */
    @ApiModelProperty("果园面积-葡萄")
    private String gymjPt;

    /**
     * 果园面积-桃
     */
    @ApiModelProperty("果园面积-桃")
    private String gymjT;

    /**
     * 果园面积-柑桔
     */
    @ApiModelProperty("果园面积-柑桔")
    private String gymjGj;

    /**
     * 果园面积-香蕉
     */
    @ApiModelProperty("果园面积-香蕉")
    private String gymjXj;

    /**
     * 果园面积-菠萝
     */
    @ApiModelProperty("果园面积-菠萝")
    private String gymjBl;

    /**
     * 果园面积-其他果树
     */
    @ApiModelProperty("果园面积-其他果树")
    private String gymjQtgs;

    /**
     * 粮食作物播种面积-小麦
     */
    @ApiModelProperty("粮食作物播种面积-小麦")
    private String lszwbzmjXm;

    /**
     * 粮食作物播种面积-玉米
     */
    @ApiModelProperty("粮食作物播种面积-玉米")
    private String lszwbzmjYm;

    /**
     * 粮食作物播种面积-水稻
     */
    @ApiModelProperty("粮食作物播种面积-水稻")
    private String lszwbzmjSd;

    /**
     * 粮食作物播种面积-马铃薯
     */
    @ApiModelProperty("粮食作物播种面积-马铃薯")
    private String lszwbzmjMls;

    /**
     * 粮食作物播种面积-豆类
     */
    @ApiModelProperty("粮食作物播种面积-豆类")
    private String lszwbzmjDl;

    /**
     * 粮食作物播种面积-其他粮食作物
     */
    @ApiModelProperty("粮食作物播种面积-其他粮食作物")
    private String lszwbzmjQtlszw;

    /**
     * 经济作物播种面积-油料作物
     */
    @ApiModelProperty("经济作物播种面积-油料作物")
    private String jjzwbzmjYlzw;

    /**
     * 经济作物播种面积-棉麻
     */
    @ApiModelProperty("经济作物播种面积-棉麻")
    private String jjzwbzmjMm;

    /**
     * 经济作物播种面积-糖料
     */
    @ApiModelProperty("经济作物播种面积-糖料")
    private String jjzwbzmjTl;

    /**
     * 经济作物播种面积-烟叶
     */
    @ApiModelProperty("经济作物播种面积-烟叶")
    private String jjzwbzmjYy;

    /**
     * 蔬菜瓜果播种面积-蔬菜
     */
    @ApiModelProperty("蔬菜瓜果播种面积-蔬菜")
    private String scggbzmjSc;

    /**
     * 蔬菜瓜果播种面积-瓜果
     */
    @ApiModelProperty("蔬菜瓜果播种面积-瓜果")
    private String scggbzmjGg;

    /**
     * 区域code
     */
    @ApiModelProperty("区域code")
    private String adRegionCode;

    /**
     * 区域code
     */
    @ApiModelProperty("区域名称")
    private String adRegionName;

    /**
     * 省code
     */
    @ApiModelProperty("省code")
    private String jcxxProvince;

    /**
     * 市code
     */
    @ApiModelProperty("市code")
    private String jcxxCity;

    /**
     * 县code
     */
    @ApiModelProperty("县code")
    private String jcxxCounty;


}
