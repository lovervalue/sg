package com.sofn.bigdata.vo;

import com.sofn.bigdata.sysapi.bean.ColumnLineVo;
import com.sofn.common.map.MapViewData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhang Yi
 * @Date 2020/8/26 14:22
 * @Version 1.0
 * 秸秆柱状图和地图对象
 */
@Data
@ApiModel(value = "生态农业处-秸秆柱状图和地图对象")
public class DucssVo {

	@ApiModelProperty("柱线图数据")
	private MapViewData mapViewData;

	@ApiModelProperty("地图数据接口")
	private List<ColumnLineVo> lineVos;


}
