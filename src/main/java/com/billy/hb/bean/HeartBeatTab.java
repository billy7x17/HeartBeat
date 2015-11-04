package com.billy.hb.bean;

public class HeartBeatTab {

    private String appName;

    private String identity;

    private String updateTime;

    private String heartbeatPeriod;

    private String heartbeatDoneTime;

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    public void setHeartbeatPeriod(String heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
    }

    public String getHeartbeatDoneTime() {
        return heartbeatDoneTime;
    }

    public void setHeartbeatDoneTime(String heartbeatDoneTime) {
        this.heartbeatDoneTime = heartbeatDoneTime;
    }

}
