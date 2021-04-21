package com.simple.es;

import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ESService {

    private static Logger log = LoggerFactory.getLogger(ESService.class);

    @Resource
    private JestClient client;


    /**
     * 修改es
     * @param id
     * @param esIndex
     * @param object
     * @return
     */
    public boolean update(String id, String esIndex, Object object) {
        Index index = new Index.Builder(object).index(esIndex).id(id).refresh(true).build();
        try {
            JestResult result = client.execute(index);
            return result != null && result.isSucceeded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Index getUpdateIndex(String id, String esIndex, Object object) {
        return new Index.Builder(object).index(esIndex).id(id).refresh(true).build();
    }

    public Delete getDeleteIndex(String id, String esIndex) {
        return new Delete.Builder(id).index(esIndex).build();
    }

    /**
     * 批量执行es请求
     * @param indexList
     * @param esIndex
     * @return
     */
    public boolean executeESClientRequest(List<BulkableAction> indexList, String esIndex) {
        Bulk bulk = new Bulk.Builder()
                .defaultIndex(esIndex)
                .addAction(indexList)
                .build();
        try {
            JestResult result = client.execute(bulk);
            if (result != null && result.isSucceeded()) {
                log.info("es更新成功，更新数    ->{}", indexList.size());
                return true;
            }
        } catch (Exception ignore) {
            log.error("es 更新失败\n", ignore);
        } finally {
            indexList.clear();
        }
        return false;
    }

    /**
     * 删除指定idDom
     * @param id
     * @param esIndex
     * @return
     */
    public boolean delete(String id, String esIndex) {
        try {
            DocumentResult result = client.execute(new Delete.Builder(id)
                    .index(esIndex).build());
            return result.isSucceeded();
        } catch (Exception e) {
            throw new RuntimeException("delete exception", e);
        }
    }
}
