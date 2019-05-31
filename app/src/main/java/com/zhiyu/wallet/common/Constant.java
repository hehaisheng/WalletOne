package com.zhiyu.wallet.common;

/**
 * @
 * Created by Administrator on 2018/11/5.
 */

public class Constant {

    public static final String Service_ipAddress = "http://192.168.3.105:8080/HMwallet-web-ME/";

    //本地测试 http://192.168.1.103:8080/HMwallet-web-ME
    //服务器地址 http://www.zgyoufu.com/

    public static final String Local_ipAddress = "http://192.168.1.109:8080/HMwallet-web/";

    public static final String Local_ipAddress2 = "http://192.168.3.107:8080/HMwallet-web-ME/";

//  public static final String Local_ipAddress3 = "http://192.168.3.108:8080/HMwallet-web-ME/";
    public static final String Local_ipAddress3 = "http://120.77.168.71/HMwallet-web-ME/";
//  public static final String Local_ipAddress3 = "http://192.168.1.109:8080/HMwallet-web-ME/";
//  public static final String Local_ipAddress3 = "http://www.zgyoufu.com/";

    // 手势密码的状态
    public static final int Gesture_Set = 1;    //

    public static final int Gesture_Change = 2; //

    public static final int Gesture_Cancle = 3; //

    public static final int Gesture_Check =4;

    // 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态

    public static final int POINT_STATE_SELECTED = 1; // 按下状态

    public static final int POINT_STATE_WRONG = 2; // 错误状态

    public static final String AppID = "2109978412";
    public static final String AppKey = "KfdhlEvKGBTBuodD";
    public static final String MerchantID = "2088131981896798";

    public static final String partnerCode = "youywl_mohe" ;
    public static final String partnerKey = "d9759ca0546f42209429e6ce6e1b3a16";

    public static final String merchantno = "10025768004";
    public static final String QUICK_REPAY="SQKKSCENEKJ010";
    public static final String callBackUrl= Local_ipAddress3 + "cardpayment/callback";
    public static final String bindcallBackUrl= Local_ipAddress3 + "authbindcardrequest/authbindcardcallback";

    public static final String authKey = "2ad13958-58e5-4013-b074-a73594cd1f5f";

    public static final String merchantNo = "0005810F2120325";

    /**
     * 单图
     */
    // outtype value
    public static final String SINGLEIMG = "singleImg";
    /**
     * 多图
     */
    public static final String MULTIIMG = "multiImg";
    /**
     * 低质量视频
     */
    public static final String VIDEO = "video";
    /**
     * 高质量视频
     */
    public static final String FULLVIDEO = "fullVideo";
    // motion type
    public static final String BLINK = "BLINK";
    public static final String NOD = "NOD";
    public static final String MOUTH = "MOUTH";
    public static final String YAW = "YAW";
    // complexity value
    public static final String EASY = "easy";
    public static final String NORMAL = "normal";
    public static final String HARD = "hard";
    public static final String HELL = "hell";

    public static final String ERROR_CAMERA_REFUSE = "相机权限获取失败或权限被拒绝";
    public static final String ERROR_SD_REFUSE = "SD卡权限被拒绝";
    public static final String ERROR_SD_CAMERA_PERMISSION = "相机权限被拒绝或SD卡权限被拒绝，请授权相机权限和SD卡权限";
    public static final String ERROR_PACKAGE = "未替换包名或包名错误";
    public static final String ERROR_LICENSE_OUT_OF_DATE = "授权文件过期";
    public static final String ERROR_SDK_INITIALIZE = "算法SDK初始化失败：可能是授权文件或模型路径错误，SDK权限过期，包名绑定错误";
    public static final int DAYS_BEFORE_LIC_EXPIRED = 5;
    public static final String ERROR_SCAN_CANCEL = "扫描被取消";
    public static final String ERROR_TIME_OUT = "扫描失败，扫描超时";

}
