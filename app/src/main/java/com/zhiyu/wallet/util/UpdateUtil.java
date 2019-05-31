package com.zhiyu.wallet.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Switch;

import com.zhiyu.wallet.bean.UpdateInfo;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BaseCommonModel;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.HomeService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.observers.DisposableObserver;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * @author Mr.Luluxiu
 * Created by Administrator on 2018/11/5.
 */

public class UpdateUtil {
    private long startTime;
    private final int TO_MAIN = 1;
    private final int DOWNLOAD_VERSION_SUCCESS = 2;
    private final int DOWNLOAD_APK_FAIL = 3;
    private final int DOWNLOAD_APK_SUCCESS = 4;

    private ProgressDialog dialog;
    private Context context;
    private File apkFile;
    private UpdateInfo updateInfo;
    private SetUpdateListencer setUpdateListencer;

    public UpdateUtil(Context context) {
        this.context = context;

    }

    public void checkUpdate() {
        startTime = System.currentTimeMillis();

        boolean connect = isConnect();
        if (!connect) {//没有移动网络
            UIUtils.toast("当前没有移动数据网络", false);
            handleUpdate(TO_MAIN);
        } else {
            updatedownload();

//            handleUpdate(TO_MAIN);
        }
    }


    public void updatedownload() {
        BasehttpModel basehttpModel = BasehttpModel.getInstance(Constant.Local_ipAddress3);
        HomeService homeService = basehttpModel.create(HomeService.class);
        basehttpModel.observa(homeService.update(new BasehttpModel.Builder().addParam("sogo","1").build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody response) {
                try {
                        try {
                            updateInfo = new UpdateInfo();
                            JSONObject jsonObject = null;

                            jsonObject = new JSONObject(response.string());

                            String version = jsonObject.getString("appVersion");
                            String apkUrl = jsonObject.getString("downaddress");
                            String desc = jsonObject.getString("updatecontent");
                            updateInfo.setVersion(version);
                            updateInfo.setApkUrl(apkUrl);
                            updateInfo.setDesc(desc);
                            handleUpdate(DOWNLOAD_VERSION_SUCCESS);
                            Println.out("update",jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handleUpdate(TO_MAIN);
                        }

                } catch (IOException e) {
                    e.printStackTrace();
                    handleUpdate(TO_MAIN);
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("failure   " + e.getMessage());
                handleUpdate(TO_MAIN);
            }

            @Override
            public void onComplete() {

            }
        });

    }


    public void handleUpdate(int code) {
        switch (code) {
            case TO_MAIN:
                setUpdateListencer.shutdowntomain(startTime);
                break;
            case DOWNLOAD_APK_SUCCESS:
                setUpdateListencer.downloadsuccess();
                installApk();
                break;
            case DOWNLOAD_VERSION_SUCCESS:
                showdialog();
                break;
            case DOWNLOAD_APK_FAIL:
                setUpdateListencer.downloadfail(dialog);
                break;
        }

    }

    private void showdialog() {
        String version = getVersion();
        //比较服务器获取的最新的版本跟本应用的版本是否一致
        if (TextUtils.isEmpty(updateInfo.getVersion())) {
            handleUpdate(TO_MAIN);
            return;
        }
        if (TextUtils.isEmpty(version)) {
            handleUpdate(TO_MAIN);
            return;
        }
        float versionf = Float.valueOf(version);
        float versionfup = Float.valueOf(updateInfo.getVersion());

        if (versionf >= versionfup) {
            // UIUtils.toast("当前应用已经是最新版本",false);
            handleUpdate(TO_MAIN);
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("下载最新版本")
                    .setMessage(updateInfo.getDesc())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //下载服务器保存的应用数据
                            downloadApk();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

    }

    private void downloadApk() {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
        //初始化数据要保持的位置
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filesDir = context.getExternalFilesDir("");
        } else {
            filesDir = context.getFilesDir();
        }

        apkFile = new File(filesDir, "Wallet_" + updateInfo.getVersion() + "_.apk");
        if (apkFile.exists()) {
            apkFile.delete();
        }
//         String path = getPackageName();
//        File dir = new File(Environment.getExternalStorageDirectory(), path+"/"+"update.apk");//在sd下创建文件夹myimage；Environment.getExternalStorageDirectory()得到SD卡路径文件
//        if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
//            dir.mkdirs();
//        }
//        apkFile = new File(dir+"");

        //启动一个分线程联网下载数据：
        new Thread() {
            public void run() {
                String path = updateInfo.getApkUrl();
                InputStream is = null;
                FileOutputStream fos = null;
                HttpURLConnection conn = null;
                int len;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(50000);
                    conn.setReadTimeout(50000);
                    conn.connect();

                    if (conn.getResponseCode() == 200) {
                        dialog.setMax(conn.getContentLength() / 1024);//设置dialog的最大值
                        is = conn.getInputStream();
                        fos = new FileOutputStream(apkFile);

                        byte[] buffer = new byte[1024];

                        while ((len = is.read(buffer)) != -1) {
                            //更新dialog的进度
                            dialog.incrementProgressBy(len / 1024);
                            fos.write(buffer, 0, len);
                            SystemClock.sleep(1);
                        }
                        handleUpdate(DOWNLOAD_APK_SUCCESS);
                    } else {
                        handleUpdate(DOWNLOAD_APK_FAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    UIUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIUtils.toast("更新失败,请到应用商店下载", false);
                        }
                    });
                    dialog.dismiss();
                    //finish();
                    handleUpdate(DOWNLOAD_APK_FAIL);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }



    private void installApk() {
//        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
//        intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));
//        startActivity(intent);
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    public String getVersion() {
        String version = "未知版本";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return version;
    }

    private boolean isConnect() {
        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            connected = networkInfo.isConnected();
        }
        return connected;
    }

    public interface SetUpdateListencer {

        void shutdowntomain(long starttime);

        void downloadsuccess();

        void downloadfail(Dialog dialog);
    }

    public UpdateUtil setOnDownloadListener(SetUpdateListencer updateListencer) {
        this.setUpdateListencer = updateListencer;
        checkUpdate();
        return this;
    }

}
