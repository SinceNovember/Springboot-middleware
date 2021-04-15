package com.simple.schedule.service;

import com.alibaba.fastjson.JSON;
import com.simple.schedule.common.Constants;
import com.simple.schedule.domain.Instruct;
import com.simple.schedule.task.CronTaskRegister;
import com.simple.schedule.task.SchedulingRunnable;
import com.simple.schedule.util.StrUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.simple.schedule.common.Constants.Global.LINE;
import static com.simple.schedule.common.Constants.Global.path_root;


/**
 * ZK服务器
 */
public class ZkCuratorServer {

    private static Logger logger = LoggerFactory.getLogger(ZkCuratorServer.class);

    public static CuratorFramework getClient(String connectString) {
        if (null != Constants.Global.client) {
            return Constants.Global.client;
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        //添加重连监听
        client.getConnectionStateListenable().addListener((curatorFramework, connectionState) -> {
            switch (connectionState) {
                case CONNECTED:
                    logger.info("middleware schedule init server connected {}", connectString);
                    break;
                case RECONNECTED:

                    break;
                default:
                    break;
            }
        });
        client.start();
        Constants.Global.client = client;
        return client;
    }

    /**
     * 监听zookeeper上的路径和节点的变化
     * @param applicationContext
     * @param client
     * @param path
     * @throws Exception
     */
    public static void addTreeCacheListener(final ApplicationContext applicationContext, final CuratorFramework client, String path) throws Exception {
        TreeCache treeCache = new TreeCache(client, path);
        treeCache.start();
        treeCache.getListenable().addListener((curatorFramework, event) -> {
            if (null == event.getData()) {
                return;
            }
            byte[] eventData = event.getData().getData();
            if (null == eventData || eventData.length < 1) {
                return;
            }
            String json = new String(eventData, Constants.Global.CHARSET_NAME);
            if ("".equals(json) || json.indexOf("{") != 0 || json.lastIndexOf("}") + 1 != json.length()) {
                return;
            }
            //获取发送的指令信息
            Instruct instruct = JSON.parseObject(new String(event.getData().getData(), Constants.Global.CHARSET_NAME),Instruct.class);
            switch (event.getType()) {
                case NODE_ADDED:
                case NODE_UPDATED:
                    if (Constants.Global.ip.equals(instruct.getIp()) && Constants.Global.schedulerServerId.equals(instruct.getSchedulerServerId())) {
                        CronTaskRegister cronTaskRegistrar = applicationContext.getBean("simple-schedule-cronTaskRegister", CronTaskRegister.class);
                        boolean isExist = applicationContext.containsBean(instruct.getBeanName());
                        if (!isExist) {
                            return;
                        }
                        Object scheduleBean = applicationContext.getBean(instruct.getBeanName());
                        String path_root_server_ip_clazz_method_status = StrUtil.joinStr(path_root, Constants.Global.LINE, "server", Constants.Global.LINE, instruct.getSchedulerServerId(), Constants.Global.LINE, "ip", LINE, instruct.getIp(), LINE, "clazz", LINE, instruct.getBeanName(), LINE, "method", LINE, instruct.getMethodName(), "/status");
                        //执行命令
                        Integer status = instruct.getStatus();
                        switch (status) {
                            case 0://关闭
                                cronTaskRegistrar.removeCronTask(instruct.getBeanName() + "_" + instruct.getMethodName());
                                setData(client, path_root_server_ip_clazz_method_status, "0");
                                logger.info("middleware schedule task stop {} {}", instruct.getBeanName(), instruct.getMethodName());
                                break;
                            case 1://启动
                                cronTaskRegistrar.addCronTask(new SchedulingRunnable(scheduleBean, instruct.getBeanName(), instruct.getMethodName()), instruct.getCron());
                                setData(client, path_root_server_ip_clazz_method_status, "1");
                                logger.info("middleware schedule task start {} {}", instruct.getBeanName(), instruct.getMethodName());
                                break;
                            case 2://更新
                                cronTaskRegistrar.removeCronTask(instruct.getBeanName() + "_" + instruct.getMethodName());
                                cronTaskRegistrar.addCronTask(new SchedulingRunnable(scheduleBean, instruct.getBeanName(), instruct.getMethodName()), instruct.getCron());
                                setData(client, path_root_server_ip_clazz_method_status, "1");
                                logger.info("middleware schedule task refresh {} {}", instruct.getBeanName(), instruct.getMethodName());
                                break;
                        }
                    }
                    break;
                case NODE_REMOVED:
                    break;
                default:
                    break;
            }
        });

    }

    //创建节点
    public static void createNode(CuratorFramework client, String path) throws Exception {
        List<String> pathChild = new ArrayList<>();
        pathChild.add(path);
        while (path.lastIndexOf(Constants.Global.LINE) > 0) {
            path = path.substring(0, path.lastIndexOf(Constants.Global.LINE));
            pathChild.add(path);
        }
        for (int i = pathChild.size() - 1; i >= 0; i--) {
            Stat stat = client.checkExists().forPath(pathChild.get(i));
            if (null == stat) {
                client.create().creatingParentsIfNeeded().forPath(pathChild.get(i));
            }
        }

    }

    //创建节点
    public static void createNodeSimple(CuratorFramework client, String path) throws Exception {
        if (null == client.checkExists().forPath(path)) {
            client.create().creatingParentsIfNeeded().forPath(path);
        }
    }

    //删除节点
    public static void deleteNodeSimple(CuratorFramework client, String path) throws Exception {
        if (null != client.checkExists().forPath(path)) {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        }
    }

    //设置数据
    public static void setData(CuratorFramework client, String path, String data) throws Exception {
        if (null == client.checkExists().forPath(path)) {
            return;
        }
        client.setData().forPath(path, data.getBytes(Constants.Global.CHARSET_NAME));
    }

    //获取数据
    public static byte[] getData(CuratorFramework client, String path) throws Exception {
        return client.getData().forPath(path);
    }

    //删除数据
    public static void deleteDataRetainNode(CuratorFramework client, String path) throws Exception {
        if (null != client.checkExists().forPath(path)) {
            client.delete().forPath(path);
        }
    }

    //添加临时节点数据
    public static void appendPersistentData(CuratorFramework client, String path, String data) throws Exception {
        PersistentEphemeralNode node = new PersistentEphemeralNode(client, PersistentEphemeralNode.Mode.EPHEMERAL, path, data.getBytes(Constants.Global.CHARSET_NAME));
        node.start();
        node.waitForInitialCreate(3, TimeUnit.SECONDS);
    }
    public static void deletingChildrenIfNeeded(CuratorFramework client, String path) throws Exception {
        if (null == client.checkExists().forPath(path)) return;
        // 递归删除节点
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }
}