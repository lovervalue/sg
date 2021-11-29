package com.sofn.bigdata.model;
import java.io.Serializable;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("ALL")
@ApiModel("返回分页查询实体类")
@Data
public class PageResult<T> implements Serializable {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("描述信息")
    private String msg;

    @ApiModelProperty("总记录数")
    private Long total;
    @ApiModelProperty("每页显示条数")
    private Long pageSize;
    @ApiModelProperty("当前页码")
    private Long pageNum;
    @ApiModelProperty("查询数据集合")
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(Long total, Long pageSize, Long pageNum, List<T> rows) {
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.rows = rows;
    }

    public PageResult(Integer code, String msg, Long total, Long pageSize, Long pageNum, List<T> rows) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.rows = rows;
    }
}
