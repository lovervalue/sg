package com.sofn.bigdata.vo.pieChart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: pcnpl-service
 * @author: zhaopeng
 * @create: 2020-07-29 15:11
 **/
@Data
@ApiModel(value="不同坡度耕地园地")
public class Btpdgdyd {
    @ApiModelProperty(value = "平地面积")
    private String gdYdSizePd;
    @ApiModelProperty(value = "缓坡地面积")
    private String gdYdSizeHpd;
    @ApiModelProperty(value = "陡坡地面积")
    private String gdYdSizeDpd;

}
