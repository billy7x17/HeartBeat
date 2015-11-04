package com.billy.hb.task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.billy.hb.bean.HeartBeatTab;
import com.billy.hb.dao.HeartBeatDao;
import com.billy.hb.util.HeartBeatConstants;

/**
 * 负载均衡 - 心跳Task
 * @author <a href="mailto:li-lei@***.com">li-lei</a>
 * @version $Revision 1.0 $ 2015年1月20日 上午11:11:04
 */
public class HeartBeatTask extends TimerTask {

    private static Logger log = LoggerFactory.getLogger(HeartBeatTask.class);

    private HeartBeatDao heartBeatDao;

    private String appName = null;

    private String identity = null;

    private Long heartBeatPeriod = null;

    private String heartBeatDoneTime = null;
    
    /*public HeartBeatTask(String appName, String identity, Long heartBeatPeriod, String heartBeatDoneTime) {
        this.appName = appName;
        this.identity = identity;
        this.heartBeatPeriod = heartBeatPeriod;
        this.heartBeatDoneTime = heartBeatDoneTime;
    }*/

    @Override
    public void run() {
        try {
            List<HeartBeatTab> heartBeatList = heartBeatDao.selectHeartBeatByApp(appName);
            // 应用先查询心跳表，表中是否有当前应用存在
            // 表中没有当前应用，添加一条记录，自己做"主"，appName做主键，有一条记录了，其他无法再insert
            if (heartBeatList.size() == 0) {
                log.info("There is no heartBeat record in DB.");
                insertHeartBeat();
                AppContext.setStatus(HeartBeatConstants.LIVE);
                log.info("I am master!");
            } else {
                // 表中有当前应用的记录，以下判断当前应用的url是否是当前此应用实例的url
                HeartBeatTab heartBeat = heartBeatList.get(0);
                // 若心跳表中当前应用url等于自己的url，更新update_time,自己做"主"
                if (identity.equals(heartBeat.getIdentity())) {
                    HeartBeatTab heartBeatTab = new HeartBeatTab();
                    heartBeatTab.setUpdateTime(heartBeatDao.selectSystemDate().substring(0, heartBeatDao.selectSystemDate().length() - 2));
                    heartBeatDao.updateHeartBeatTime(heartBeatTab, appName, identity);
                    AppContext.setStatus(HeartBeatConstants.LIVE);
                    log.info("I am still master!");
                } else {
                    log.info("I begin to find master whether live!");
                    // 若心跳表中当前应用url不等于自己的url，检测主是否断连
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Long currentTime = df.parse(heartBeatDao.selectSystemDate()).getTime();
                    Long lastUpdateTime = df.parse(heartBeat.getUpdateTime()).getTime();
                    // 此应用当前url心跳未更新时间差值 = 当前系统时间-数据库中最后更新时间
                    Long notUpdateTime = currentTime - lastUpdateTime;
                    // 此应用当前主url总心跳时间 = DB配置的主url心跳时间+主url完成操作心跳时间的和  -- 单位：秒，所以*1000变成毫秒
                    Long heartBeatTime = (Long.parseLong(heartBeat.getHeartbeatPeriod()) + Long.parseLong(heartBeat.getHeartbeatDoneTime())) * 1000;
                    // 此应用当前url心跳未更新时间差值  > 此应用当前主url总心跳时间
                    if (notUpdateTime > heartBeatTime) {
                        log.info("I found master not live! I want to become myself to master!");
                        // 删除此应用当前主url记录，重新insert一个"主"，因为appName是主键，只能有一个从变成主(处理交给DB)
                        int n = 0;
                        try {
                            n = heartBeatDao.delete(appName);
                        } catch(Exception e) {
                            log.error("delete heartBeat DB record became current appInstance faild!", e);
                        }
                        if (n != 0) {
                            insertHeartBeat();
                            AppContext.setStatus(HeartBeatConstants.LIVE);
                            log.info("I begin to be master!");
                        }
                    } else {
                        AppContext.setStatus(HeartBeatConstants.UNLIVE);
                        log.info("Master still live, I am not master.");
                    }
                }
            }
        } catch (Throwable t) {
            log.error("HeartBeatTask cause exception: ", t);
        }
    }

    private void insertHeartBeat() {
        HeartBeatTab heartBeatTab = new HeartBeatTab();
        heartBeatTab.setAppName(appName);
        heartBeatTab.setIdentity(identity);
        heartBeatTab.setUpdateTime(heartBeatDao.selectSystemDate().substring(0, heartBeatDao.selectSystemDate().length() - 2));
        heartBeatTab.setHeartbeatPeriod(String.valueOf(heartBeatPeriod));
        heartBeatTab.setHeartbeatDoneTime(heartBeatDoneTime);
        heartBeatDao.insert(heartBeatTab);
    }

    public HeartBeatDao getHeartBeatDao() {
        return heartBeatDao;
    }

    public void setHeartBeatDao(HeartBeatDao heartBeatDao) {
        this.heartBeatDao = heartBeatDao;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Long getHeartBeatPeriod() {
        return heartBeatPeriod;
    }

    public void setHeartBeatPeriod(Long heartBeatPeriod) {
        this.heartBeatPeriod = heartBeatPeriod;
    }

    public String getHeartBeatDoneTime() {
        return heartBeatDoneTime;
    }

    public void setHeartBeatDoneTime(String heartBeatDoneTime) {
        this.heartBeatDoneTime = heartBeatDoneTime;
    }

}
