package com.cds.promotion.service;

import android.app.NotificationManager;

/**
 * @author Chengzj
 * @date 2018/9/29 10:18
 */
public interface SocketContract {
    void startPushService();

    void checkNofificationPermission(NotificationManager manager);
}
