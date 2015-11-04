package com.billy.hb.task;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负载均衡 - 心跳timer启动
 * @author <a href="mailto:li-lei@***.com">li-lei</a>
 * @version $Revision 1.0 $ 2015年1月20日 下午1:30:04
 */
public class HeartBeatEngine {

    private static Logger log = LoggerFactory.getLogger(HeartBeatEngine.class);
    
    private static final long DEFAULT_PERIOD = 60L;

    private static final int UNIT = 1000;
    
    private static final long DEFAULT_DELAY = 1000L;
    
    private Timer heartBeatTimer = new Timer();
    
    private TimerTask heartBeatTask = null;
    
    private long heartBeatPeriod = DEFAULT_PERIOD;
    
    private long delay = DEFAULT_DELAY;
    
    private String appName;
    
    private String identity;
    
    private String heartBeatDoneTime;
    
    public void turnon() {
        /*heartBeatTask = new HeartBeatTask(appName, identity, heartBeatPeriod, heartBeatDoneTime);*/
        heartBeatTimer.schedule(heartBeatTask, delay * UNIT, this.heartBeatPeriod * UNIT);
        log.info("Turnon the heart beat engines is successfully!");
    }
    
    public void turnoff() {
        try {
            heartBeatTimer.cancel();
            log.info("Turnoff the heart beat engines is successfully!");
        } catch (Throwable t) {
            log.error("Turnoff the heart beat engines cause exception:", t);
        }
    }

    public TimerTask getHeartBeatTask() {
        return heartBeatTask;
    }

    public void setHeartBeatTask(TimerTask heartBeatTask) {
        this.heartBeatTask = heartBeatTask;
    }

    public long getHeartBeatPeriod() {
        return heartBeatPeriod;
    }

    public void setHeartBeatPeriod(long heartBeatPeriod) {
        this.heartBeatPeriod = heartBeatPeriod;
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
    
    public String getHeartBeatDoneTime() {
        return heartBeatDoneTime;
    }

    public void setHeartBeatDoneTime(String heartBeatDoneTime) {
        this.heartBeatDoneTime = heartBeatDoneTime;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

}
