package com.zhiyu.wallet.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cc.shinichi.library.tool.utility.device.SDKUtil;

/**
 * 2018/7/12
 *
 * @author Mr.Luluxiu
 */

public class BitmapUtils {


    public static String saveCompressBitmap(Context context, String filename) {
        String path = context.getPackageName();
        File dir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = context.getExternalFilesDir("picture");//在sd下创建文件夹picture；Environment.getExternalStorageDirectory()得到SD卡路径文件
        } else {
            dir = context.getFilesDir();
        }

        if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
            dir.mkdirs();
        }
        File currentImageFile;
        if (TextUtils.isEmpty(filename)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
            currentImageFile = new File(dir, df.format(new Date()) + ".jpg");
        } else {
            currentImageFile = new File(dir, filename + ".jpg");
        }

        if (!currentImageFile.exists()) {
            try {
                currentImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return currentImageFile.getAbsolutePath();
    }

    //采样率压缩（根据图片压缩并保存到相应位置）：
    public static boolean compressImage(Bitmap image, String filepath) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > 600) {    //循环判断如果压缩后图片是否大于600kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                options -= 10;//每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
            //压缩好后写入文件中
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //采样率压缩（根据路径获取图片并压缩）：
    public static Bitmap getSmallBitmap(Context context, String filePath, int reqWidth, int reqHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;              //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            BitmapFactory.decodeFile(filePath, options);            //此时返回bm为空
//            Println.out("calculateInSampleSize", "width " + options.outWidth + "height " + options.outHeight);

            if (reqWidth == 0 || reqHeight == 0) {
                options.inSampleSize = 1;
            } else if (options.outHeight > options.outWidth) {
                options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
            } else {
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例 数值越高，图片像素越低
            }

            options.inJustDecodeBounds = false;                                     //重新读入图片，注意此时把options.inJustDecodeBounds 设回false了

            Bitmap bitmap =BitmapFactory.decodeFile(filePath, options);
//            Println.out("bitmap", "width " + bitmap.getWidth() + "height " + bitmap.getHeight());


            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //采样率压缩（根据图片URI压缩）：
    public static Bitmap getSmallBitmap(Context context, Uri uri, int reqWidth, int reqHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//开始读入图片，此时把options.inJustDecodeBounds 设回true了
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,options);

            Println.out("calculateInSampleSize", "width " + options.outWidth + "height " + options.outHeight);
            if (options.outHeight > options.outWidth) {
                options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
            } else {
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例 数值越高，图片像素越低
            }

            options.inJustDecodeBounds = false;//重新读入图片，注意此时把options.inJustDecodeBounds 设回false了
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,options);

            Println.out("CAMERA","width "+bitmap.getWidth() +"height"+bitmap.getHeight());

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //采样率压缩（根据图片压缩）：
    public static Bitmap getSmallBitmap(Context context, Bitmap bitmap, int reqWidth, int reqHeight) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//开始读入图片，此时把options.inJustDecodeBounds 设回true了

            BitmapFactory.decodeStream(bais, null, options);

            Println.out("calculateInSampleSize", "width " + options.outWidth + "height " + options.outHeight);
            if (options.outHeight > options.outWidth) {
                options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
            } else {
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//设置缩放比例 数值越高，图片像素越低
            }

            options.inJustDecodeBounds = false;//重新读入图片，注意此时把options.inJustDecodeBounds 设回false了

            bitmap = BitmapFactory.decodeStream(bais, null, options);

            Println.out("CAMERA","width "+bitmap.getWidth() +"height"+bitmap.getHeight());

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        try {
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 1;  //1表示不缩放

            if (height > reqHeight || width > reqWidth) {
                int heightRatio = Math.round((float) height / (float) reqHeight);
                int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            return inSampleSize;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }


    public static boolean saveBitmap2Gallery(Context context, Bitmap bitmap, String filename) {
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;

        File file = new File(galleryPath, filename + ".jpg");
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

            //  fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }

        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, file.toString(), null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return true;

    }

    public static Bitmap getScrollViewBitmap(ScrollView scrollView){
        int h = 0 ;
        for(int i =0 ; i <scrollView.getChildCount();i++){

            h+= scrollView.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(),h,Bitmap.Config.ARGB_8888);
        Canvas canvas1 =new Canvas(bitmap);
        scrollView.draw(canvas1);
        return bitmap;
    }

    public static Bitmap getLinearlayoutBitmap(LinearLayout linearLayout){
        int h = 0;
        for(int i =0 ; i < linearLayout.getChildCount(); i++){

            h+= linearLayout.getChildAt(i).getHeight();

        }
        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getWidth(),h ,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linearLayout.draw(canvas);

        return bitmap;
    }

    public static Bitmap getMergeBitmap(Bitmap first,Bitmap second){
        Bitmap bitmap  = Bitmap.createBitmap(first.getWidth(),first.getHeight()+second.getHeight(),first.getConfig());

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(first,new Matrix(),null);
        canvas.drawBitmap(second,0,first.getHeight(),null);

        return bitmap;
    }


    public static void delectImage(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        File file = new File(url);
        if (file.exists()) {
            file.delete();
        }
    }

}
