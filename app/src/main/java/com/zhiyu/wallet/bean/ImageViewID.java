package com.zhiyu.wallet.bean;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 2018/7/25
 *
 * @author zhiyu
 */
public class ImageViewID {

    public static ImageView getImageView() {
        return imageView;
    }

    public static void  setImageView(ImageView iv) {
        imageView = iv;
    }

    private static ImageView imageView;

    private static String imageViewName;

    private static String idfacepath;

    private static String idbackpath;

    private static String idcardpath;

    private static Bitmap idfacebitmap;

    private static Bitmap idbackbitmap;

    private static Bitmap idcardbitmap;

    public static String getImageViewName() {
        return imageViewName;
    }

    public static void setImageViewName(String imageViewName) {
        ImageViewID.imageViewName = imageViewName;
    }

    public static String getIdfacepath() {
        return idfacepath;
    }

    public static void setIdfacepath(String idfacepath) {
        ImageViewID.idfacepath = idfacepath;
    }

    public static String getIdbackpath() {
        return idbackpath;
    }

    public static void setIdbackpath(String idbackpath) {
        ImageViewID.idbackpath = idbackpath;
    }

    public static String getIdcardpath() {
        return idcardpath;
    }

    public static void setIdcardpath(String idcardpath) {
        ImageViewID.idcardpath = idcardpath;
    }

    public static Bitmap getIdfacebitmap() {
        return idfacebitmap;
    }

    public static void setIdfacebitmap(Bitmap idfacebitmap) {
        ImageViewID.idfacebitmap = idfacebitmap;
    }

    public static Bitmap getIdbackbitmap() {
        return idbackbitmap;
    }

    public static void setIdbackbitmap(Bitmap idbackbitmap) {
        ImageViewID.idbackbitmap = idbackbitmap;
    }

    public static Bitmap getIdcardbitmap() {
        return idcardbitmap;
    }

    public static void setIdcardbitmap(Bitmap idcardbitmap) {
        ImageViewID.idcardbitmap = idcardbitmap;
    }
}
