package com.simple.schedule.controller.service;

import com.simple.schedule.domain.DataCollect;
import com.simple.schedule.domain.DcsScheduleInfo;
import com.simple.schedule.domain.DcsServerNode;
import com.simple.schedule.domain.Instruct;

import java.util.List;

public interface DcsScheduleService {

    List<String> queryPathRootServerList() throws Exception;

    List<DcsScheduleInfo> queryDcsScheduleInfoList(String schedulerServerId) throws Exception;

    void pushInstruct(Instruct instruct) throws Exception;

    DataCollect queryDataCollect() throws Exception;

    List<DcsServerNode> queryDcsServerNodeList() throws Exception;
}
