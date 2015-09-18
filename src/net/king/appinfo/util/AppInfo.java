package net.king.appinfo.util;

import android.graphics.drawable.Drawable;

/**
 * App信息类
 */
public class AppInfo {
    // 包名
    private String packageName;
    // APP名
    private String appName;
    // 图标
    private Drawable icon;
    // 版本号
    private String versionName;
    // 权限
    private String[] permissions;
    // 主Activity的类名
    private String launchActivityName; 
    
    public String getLaunchActivityName() {
        return launchActivityName;
    }

    public void setLaunchActivityName(String launchActivityName) {
        this.launchActivityName = launchActivityName;
    }

    public AppInfo() {}

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    };
    
    
}