package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * @author 半梦
 * @create 2020-08-13 14:35
 */
public class SystemNotificationInformation implements Serializable {

    private SystemNotification systemNotification;

    private String username;

    private String identity;

    @Override
    public String toString() {
        return "SystemNotificationInformation{" +
                "systemNotification=" + systemNotification +
                ", username='" + username + '\'' +
                ", identity='" + identity + '\'' +
                '}';
    }

    public SystemNotificationInformation() {

    }

    public void setSystemNotification(SystemNotification systemNotification) {
        this.systemNotification = systemNotification;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public SystemNotification getSystemNotification() {
        return systemNotification;
    }

    public String getUsername() {
        return username;
    }

    public String getIdentity() {
        return identity;
    }

    public SystemNotificationInformation(SystemNotification systemNotification, String username, String identity) {
        this.systemNotification = systemNotification;
        this.username = username;
        this.identity = identity;
    }
}
