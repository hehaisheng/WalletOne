package com.zhiyu.wallet.bean;

/**
 * 2018/7/31
 *
 * 版本更新
 * @author zhiyu
 */
public class UpdateInfo {

    private String version;//服务器的最新版本值
    private String apkUrl;//最新版本的路径
    private String desc;//版本更新细节

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
