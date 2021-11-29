package com.sofn.bigdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sofn.bigdata.model.DataShare;
import com.sofn.bigdata.vo.DataShareVo;
import com.sofn.common.utils.PageUtils;

public interface DataShareService extends IService<DataShare> {

    void saveDataShare(DataShareVo dataShare);

    void deleteDataShare(String id);

    void updateDataShare(DataShareVo dataShareVo);

    PageUtils getDataShareList(String interName, String interStatus, Integer pageNo, Integer pageSize);

    void startDataShare(String id);

    void notStartDataShare(String id);
}
