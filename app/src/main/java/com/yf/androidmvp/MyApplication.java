package com.yf.androidmvp;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.yf.androidmvp.util.StringUtil;

import java.util.List;

/**
 * @Description:
 * @Author: ZhengXiang Sun
 * @Data: 2020-12-23
 */
public class MyApplication extends Application {

    private static Context appCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isMainProcess()) {
            return;
        }
        appCtx = getApplicationContext();
//        initLeakCanary();
    }

    public static Context getAppCtx() {
        return appCtx;
    }

    /**
     * 获取是否是主进程
     *
     * @return 是否是主进程
     */
    private boolean isMainProcess() {
        return StringUtil.equals(getProcess(), getPackageName());
    }

    /**
     * 获取当前进程的名称.
     */
    private String getProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> rps;
        String name = "";
        if (am != null) {
            rps = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo rp : rps) {
                if (rp.pid == pid) {
                    name = rp.processName;
                    break;
                }
            }
        }
        return name;
    }
}
