package com.sofn.bigdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sofn.bigdata.mapper.KeTransMonitorMapper;
import com.sofn.bigdata.model.KTransMonitor;
import com.sofn.bigdata.model.KTransRecord;
import com.sofn.bigdata.service.KeTransMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Acthor Tao.Lee @date 2021/2/19 11:25
 * @Description
 */
@Service
public class KeTransMonitorServiceImpl implements KeTransMonitorService {

    @Autowired
    private KeTransMonitorMapper kTransMonitorDao;

    @Override
    public Integer alltranscount() {
        return kTransMonitorDao.selectCount(
                new QueryWrapper<KTransMonitor>()
                        .eq("monitor_status",1));
    }

    @Override
    public Integer alltranssuccesscount() {
/*        QueryWrapper<KTransMonitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(monitor_success) as allmonitorsuccess")
                .eq("monitor_status", 1);
//        Map<String, Object> map = kTransMonitorDao.selectOne(queryWrapper);
//        BigDecimal sumCount =(BigDecimal) map.get("allmonitorsuccess");;
        KTransMonitor sumCount = kTransMonitorDao.selectCount(queryWrapper);
        System.out.println("总和=========="+sumCount);*/
        return kTransMonitorDao.selectCountsuccess();
    }

    @Override
    public Integer alltransfailcount() {
        return kTransMonitorDao.selectCountfail();
    }

    @Override
    public List<KTransMonitor> getTransList() {
        return kTransMonitorDao.selectList(new QueryWrapper<KTransMonitor>());
    }

    @Override
    public List<KTransRecord> seetrans(Integer recordTrans) {
        return kTransMonitorDao.seetrans(recordTrans);
    }

    @Override
    public Integer seetransconut(Integer recordTrans) {
        return kTransMonitorDao.seetransconut(recordTrans);
    }
}
