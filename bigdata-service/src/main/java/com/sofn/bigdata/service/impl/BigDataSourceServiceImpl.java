package com.sofn.bigdata.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sofn.bigdata.enums.CharSetEnum;
import com.sofn.bigdata.enums.DriverSetEnum;
import com.sofn.bigdata.enums.IsolationLevelEnum;
import com.sofn.bigdata.enums.MysqlDataTypeEnum;
import com.sofn.bigdata.mapper.BigDataSourceMapper;
import com.sofn.bigdata.mapper.SourceTbInfoMapper;
import com.sofn.bigdata.model.BigDataSource;
import com.sofn.bigdata.model.SourceTbInfo;
import com.sofn.bigdata.service.BigDataSourceService;
import com.sofn.bigdata.utils.DataUtils;
import com.sofn.bigdata.vo.*;
import com.sofn.common.service.BaseService;
import com.sofn.common.utils.IdUtil;
import com.sofn.common.utils.PageUtils;
import com.sofn.common.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Service
public class BigDataSourceServiceImpl extends BaseService<BigDataSourceMapper, BigDataSource> implements BigDataSourceService {

    @Autowired
    private BigDataSourceMapper bigDataSourceDao;
    @Autowired
    private SourceTbInfoMapper sourceTbInfoDao;

    @Override
    public void saveBigDataSource(BigDataSourceVo bigDataSourceVo) {

        BigDataSource bigDataSource = new BigDataSource();
        BeanUtils.copyProperties(bigDataSourceVo,bigDataSource);
        bigDataSource.setCheckSqlId(bigDataSourceVo.getCheckSqlId());
        bigDataSource.setDbsourceChar(bigDataSourceVo.getDbsourceCharid());
        bigDataSource.setDbtargetChar(bigDataSourceVo.getDbtargetharid());
        bigDataSource.setDriverType(bigDataSourceVo.getDriverTypeid());
        bigDataSource.setIsolevel(bigDataSourceVo.getIsolevelid());
        bigDataSource.setDelFlag("N");
        bigDataSource.setCreateTime(new Date());
        bigDataSource.setCreateUserId(UserUtil.getLoginUserId());
        bigDataSource.setId(IdUtil.getUUId());

        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(bigDataSource));

        bigDataSourceDao.saveBigDataSource(params);
    }

    @Override
    public boolean testConBigDataSource(BigDataSourceVo bigDataSourceVo) throws Exception{

        /*String URL="jdbc:mysql://127.0.0.1:3306/imooc?useUnicode=true&amp;characterEncoding=utf-8";
        String USER="root";
        String PASSWORD="123456";*/

        String url = bigDataSourceVo.getConString();
        String user = bigDataSourceVo.getUsername();
        String password = bigDataSourceVo.getPassword();
        /*String driverType = bigDataSourceVo.getDriverTypename();*/
        String driverClass = bigDataSourceVo.getDriverClass();

        Class.forName(driverClass);
        Connection conn= DriverManager.getConnection(url, user, password);

        boolean flag = conn.isClosed();
        conn.close();

        return flag;

    }

    @Override
    public void executeSql(String sqlValue, String dbid) {

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
                ResultSet resultSet = statement.executeQuery(sqlValue);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public PageUtils<DbSourceVo> getBigDataSourceList(String dbname, String dbalias, Integer pageNo, Integer pageSize) {
        if(pageNo != null && pageSize != null){
            PageHelper.offsetPage(pageNo, pageSize);
        }
        List<BigDataSource> res = bigDataSourceDao.getBigDataSourceList(dbname, dbalias);

        PageInfo<BigDataSource> pageInfo = new PageInfo<>(res);
        List<BigDataSource> resList = pageInfo.getList();

        List<DbSourceVo> resInfo = getResByResList(resList);
        PageInfo<DbSourceVo> sourceVoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,sourceVoPageInfo);
        sourceVoPageInfo.setList(resInfo);

        return PageUtils.getPageUtils(sourceVoPageInfo);
    }

    private List<DbSourceVo> getResByResList(List<BigDataSource> resList) {
        List<DbSourceVo> sourceVoList = new ArrayList<>();
        for(BigDataSource source : resList){
            DbSourceVo sourceVo = new DbSourceVo();
            BeanUtils.copyProperties(source,sourceVo);
            String operator = source.getCreateUserId();
            Date operadate = source.getCreateTime();
            sourceVo.setOperator(UserUtil.getUsernameById(operator));
            sourceVo.setOperatime(operadate);
            sourceVoList.add(sourceVo);
        }
        return sourceVoList;
    }

    @Override
    public void delBigDataSource(String dbid) {
        bigDataSourceDao.deleteBigDataSource(dbid);
    }

    @Override
    public List<DataEnumVo> getCharSet() {
        return CharSetEnum.getEnumInfo();
    }

    @Override
    public List<DataEnumVo> getDriverList() {
        return DriverSetEnum.getEnumInfo();
    }

    @Override
    public List<CheckSqlVo> getCheckSqlList() {
        List<CheckSqlVo> resList = new ArrayList<>();
        resList.add(new CheckSqlVo("1","获取连接时测试","SELECT 1 FROM DUAL"));
        resList.add(new CheckSqlVo("2","返还时测试","SELECT 2 FROM DUAL"));
        resList.add(new CheckSqlVo("3","获取、返还连接时测试","SELECT 3 FROM DUAL"));
        resList.add(new CheckSqlVo("4","返还时关闭连接","SELECT 4 FROM DUAL"));
        resList.add(new CheckSqlVo("5","不测试连接","SELECT 5 FROM DUAL"));
        return resList;
    }

    @Override
    public List<DataEnumVo> getIsolationLevelList() {
        return IsolationLevelEnum.getEnumInfo();
    }

    @Override
    public HashMap<String, Object> getDbSourceDetail(String dbid, String level) {

        HashMap<String, Object> resMap = new HashMap<>();
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
                resultSet.close();
                if("2".equals(level)){
                    List<TableVo> list1 = new ArrayList<>();
                    for(TableVo tableVo : list){
                        String tableName = tableVo.getTableName();
                        ResultSet rs = statement.executeQuery("select column_name,column_comment,data_type from information_schema.columns where table_name='"+ tableName +"'");
                        List<ColumnVo> columnVos = new ArrayList<>();
                        while(rs.next()){
                            ColumnVo columnVo = new ColumnVo();
                            columnVo.setId(IdUtil.getUUId());
                            columnVo.setColumnName(rs.getObject("column_name")+"");
                            columnVo.setColumnAlias(rs.getObject("column_comment")+"");
                            columnVo.setColumnType(rs.getObject("data_type")+"");
                            columnVo.setColumnForm(MysqlDataTypeEnum.getDescByCode(rs.getObject("data_type")+""));
                            columnVos.add(columnVo);
                        }
                        tableVo.setColumnVos(columnVos);
                        list1.add(tableVo);
                        rs.close();
                    }
                    list = list1;
                }
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        resMap.put("name",name);
        resMap.put("alias",bigDataSource.getAlias());
        resMap.put("tables",list);
        return resMap;
    }

    @Override
    public List<SourceTableInfoVo> getTableListBySourceId(String dbid) {

        List<SourceTableInfoVo> list = new ArrayList<>();

        BigDataSource bigDataSource = bigDataSourceDao.selectById(dbid);
        String name = bigDataSource.getName();
        String url = bigDataSource.getConString();
        String user = bigDataSource.getUsername();
        String password = bigDataSource.getPassword();
        String driverClass = bigDataSource.getDriverClass();

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn= DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select table_name from information_schema.tables where table_schema='" + name + "'");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for(int i=1;i<=columnCount;i++){
                SourceTableInfoVo infoVo = new SourceTableInfoVo();
                String columnName = metaData.getColumnName(i);
                infoVo.setTbid(IdUtil.getUUId());
                infoVo.setTbName(columnName);
                list.add(infoVo);
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    @Override
    public void saveTables(String tbids) {
        List<String> formatList = DataUtils.getFormatList(tbids);
        sourceTbInfoDao.saveTables(formatList);
    }

    @Override
    public List<SourceTableInfoVo> getTablesBySourceId(String dbid) {

        List<SourceTbInfo> list = sourceTbInfoDao.getTablesBySourceId(dbid);
        List<SourceTableInfoVo> voList = new ArrayList<>();

        for(SourceTbInfo sourceTbInfo : list){
            SourceTableInfoVo vo = new SourceTableInfoVo();
            BeanUtils.copyProperties(sourceTbInfo,vo);
            vo.setTbid(sourceTbInfo.getId());
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<DbSourceVo> getAllSourceList() {
        List<BigDataSource> res = bigDataSourceDao.getBigDataSourceList(null,null);
        return getResByResList(res);
    }

    @Override
    public HashMap<String, Object> getColunmDetailBySourceId(String dbid, String tbname, String datatype) {

        String sql;

        HashMap<String, Object> resMap = new HashMap<>();

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
                if("1".equals(datatype)){
                    List<ColumnVo> list = new ArrayList<>();
                    sql = "select column_name,column_comment,data_type from information_schema.columns where table_name='"+ tbname +"'";
                    ResultSet resultSet = statement.executeQuery(sql);
                    while(resultSet.next()){
                        ColumnVo columnVo = new ColumnVo();
                        columnVo.setId(IdUtil.getUUId());
                        columnVo.setColumnName(resultSet.getObject("column_name")+"");
                        columnVo.setColumnAlias(resultSet.getObject("column_comment")+"");
                        columnVo.setColumnType(resultSet.getObject("data_type")+"");
                        columnVo.setColumnForm(MysqlDataTypeEnum.getDescByCode(resultSet.getObject("data_type")+""));
                        list.add(columnVo);
                    }
                    resMap.put("tables",list);
                    conn.close();
                }else if("2".equals(datatype)){
                    List<Map<Object,Object>> list = new ArrayList<>();
                    sql = "select * from " + tbname;
                    ResultSet resultSet = statement.executeQuery(sql);
                    ResultSetMetaData rData = resultSet.getMetaData();
                    while(resultSet.next()){
                        Map<Object,Object> obj = new HashMap<>();
                        for (int i = 1; i <= rData.getColumnCount(); i++) {
                            obj.put(rData.getColumnName(i).toLowerCase(), resultSet.getObject(i));
                        }
                        list.add(obj);
                    }
                    resMap.put("tables",list);
                    conn.close();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        resMap.put("name",name);
        resMap.put("alias",bigDataSource.getAlias());
        return resMap;
    }

    @Override
    public void renameAliasDb(String dbid, String alias) {
        bigDataSourceDao.renameAliasDb(dbid,alias);
    }
}
