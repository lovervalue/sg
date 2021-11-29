package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.enums.MysqlDataTypeEnum;
import com.sofn.bigdata.enums.RelationEnum;
import com.sofn.bigdata.mapper.BigDataSourceMapper;
import com.sofn.bigdata.mapper.TableRelationMapper;
import com.sofn.bigdata.model.BigDataSource;
import com.sofn.bigdata.model.TableRelation;
import com.sofn.bigdata.service.TableRelationService;
import com.sofn.bigdata.vo.ColumnVo;
import com.sofn.bigdata.vo.DataEnumVo;
import com.sofn.bigdata.vo.TableRelationVo;
import com.sofn.bigdata.vo.TableVo;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TableRelationServiceImpl extends BaseService<TableRelationMapper, TableRelation> implements TableRelationService {

    @Autowired
    private TableRelationMapper tableRelationDao;
    @Autowired
    private BigDataSourceMapper bigDataSourceDao;

    @Override
    public void saveTableRelation(TableRelationVo tableRelationVo) {

        TableRelation relation = new TableRelation();
        BeanUtils.copyProperties(tableRelationVo,relation);
        relation.setDelFlag("N");
        relation.setId(IdUtil.getUUId());
        relation.setCreateTime(new Date());
        relation.setCreateUserId(UserUtil.getLoginUserId());
        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(relation));

        tableRelationDao.saveTableRelation(params);
    }

    @Override
    public PageUtils<TableRelationVo> getTableRelationList(Integer pageNo, Integer pageSize) {

        if(pageNo != null && pageSize != null){
            PageHelper.offsetPage(pageNo, pageSize);
        }
        List<TableRelation> res = tableRelationDao.getTableRelationList();
        PageInfo<TableRelation> pageInfo = new PageInfo<>(res);
        List<TableRelation> resList = pageInfo.getList();

        List<TableRelationVo> relationVoList = new ArrayList<>();
        for(TableRelation relation : resList){
            TableRelationVo vo = new TableRelationVo();
            BeanUtils.copyProperties(relation,vo);
            relationVoList.add(vo);
        }

        PageInfo<TableRelationVo> voPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,voPageInfo);
        voPageInfo.setList(relationVoList);

        return PageUtils.getPageUtils(voPageInfo);
    }

    @Override
    public void delTableRelation(String tbid) {
        tableRelationDao.deleteTableRelation(tbid);
    }

    @Override
    public void updateTableRelation(TableRelationVo tableRelationVo) {

        TableRelation relation = new TableRelation();
        BeanUtils.copyProperties(tableRelationVo,relation);
        relation.setDelFlag("N");
        relation.setUpdateTime(new Date());
        relation.setUpdateUserId(UserUtil.getLoginUserId());
        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(relation));

        tableRelationDao.updateTableRelation(params);
    }

    @Override
    public List<DataEnumVo> getUnionType() {
        return RelationEnum.getEnumInfo();
    }

    @Override
    public List<TableVo> getAllTablesBySourceId(String dbid) {

        List<TableVo> list = new ArrayList<>();

        BigDataSource bigDataSource = bigDataSourceDao.selectById(dbid);
        String name = bigDataSource.getName();
        String url = bigDataSource.getConString();
        String user = bigDataSource.getUsername();
        String password = bigDataSource.getPassword();
        String driverClass = bigDataSource.getDriverClass();

        try {
            Class.forName(driverClass);
            try {
                Connection conn= DriverManager.getConnection(url, user, password);
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select table_name,table_comment from information_schema.tables where table_schema='" + name + "'");
                while(resultSet.next()){
                    TableVo tableVo = new TableVo();
                    tableVo.setId(IdUtil.getUUId());
                    tableVo.setTableName(resultSet.getObject("table_name")+"");
                    tableVo.setTableComment(resultSet.getObject("table_comment")+"");
                    list.add(tableVo);
                }
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ColumnVo> getAllColumnsByIdAndTbName(String dbid, String tbname) {

        List<ColumnVo> list = new ArrayList<>();

        BigDataSource bigDataSource = bigDataSourceDao.selectById(dbid);
        String url = bigDataSource.getConString();
        String user = bigDataSource.getUsername();
        String password = bigDataSource.getPassword();
        String driverClass = bigDataSource.getDriverClass();

        try {
            Class.forName(driverClass);
            try {
                Connection conn= DriverManager.getConnection(url, user, password);
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select column_name,column_comment,data_type from information_schema.columns where table_name='"+ tbname +"'");
                while(resultSet.next()){
                    ColumnVo columnVo = new ColumnVo();
                    columnVo.setId(IdUtil.getUUId());
                    columnVo.setColumnName(resultSet.getObject("column_name")+"");
                    columnVo.setColumnAlias(resultSet.getObject("column_comment")+"");
                    columnVo.setColumnType(resultSet.getObject("data_type")+"");
                    columnVo.setColumnForm(MysqlDataTypeEnum.getDescByCode(resultSet.getObject("data_type")+""));
                    list.add(columnVo);
                }
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }
}
