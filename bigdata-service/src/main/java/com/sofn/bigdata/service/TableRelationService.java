package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.TableRelation;
import com.sofn.bigdata.vo.ColumnVo;
import com.sofn.bigdata.vo.DataEnumVo;
import com.sofn.bigdata.vo.TableRelationVo;
import com.sofn.bigdata.vo.TableVo;
import com.sofn.common.utils.PageUtils;

import java.util.List;

public interface TableRelationService extends IService<TableRelation> {

    void saveTableRelation(TableRelationVo tableRelationVo);

    PageUtils<TableRelationVo> getTableRelationList(Integer pageNo, Integer pageSize);

    void delTableRelation(String tbid);

    void updateTableRelation(TableRelationVo tableRelationVo);

    List<DataEnumVo> getUnionType();

    List<TableVo> getAllTablesBySourceId(String dbid);

    List<ColumnVo> getAllColumnsByIdAndTbName(String dbid, String tbname);
}
