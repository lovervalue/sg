package com.sofn.bigdata.convertor;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@Component
public class String2ListLongTypeHandler extends BaseTypeHandler<List<Long>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Long> longs, JdbcType jdbcType) throws SQLException {
        if (longs != null && longs.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (Long l : longs) {
                stringBuffer.append(l).append(",");
            }
            StringBuffer sb = stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
            preparedStatement.setString(i, sb.toString());
        }
    }

    @Override
    public List<Long> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        List<Long> list = getList();
        String string = resultSet.getString(s);
        if (string != null && !string.equals("")) {
            String[] strings = string.split(",");
            for (String str : strings) {
                list.add(Long.valueOf(str));
            }
        }
        return list;
    }

    @Override
    public List<Long> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        List<Long> list = getList();
        String string = resultSet.getString(i);
        if (string != null && !string.equals("")) {
            String[] strings = string.split(",");
            for (String str : strings) {
                list.add(Long.valueOf(str));
            }
        }
        return list;
    }

    @Override
    public List<Long> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        List<Long> list = getList();
        String string = callableStatement.getString(i);
        if (string != null && !string.equals("")) {
            String[] strings = string.split(",");
            for (String str : strings) {
                list.add(Long.valueOf(str));
            }
        }
        return list;
    }

    public List<Long> getList(){
        return new ArrayList<>();
    }
}
