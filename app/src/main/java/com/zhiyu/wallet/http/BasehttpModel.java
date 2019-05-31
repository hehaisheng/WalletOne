package com.zhiyu.wallet.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.util.Println;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/10/29.
 */

public class BasehttpModel extends BaseCommonModel {
    private final long CONNECT_TIME_OUT = 15;
    private final long READ_TIME_OUT = 25;
    private static final String IPADDRESS = "http://www.zgyoufu.xyz/";
    private Retrofit retrofit;
    private static BasehttpModel basehttpModel;

    public BasehttpModel(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
                        builder.addHeader("Content-Type", "application/json;charset=utf-8");
                        builder.method(request.method(), request.body());
                        return chain.proceed(builder.build());
                    }
                })
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public BasehttpModel(String url,int type) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();
//                        builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                        builder.addHeader("Content-Type", "multipart/form-data");
                        builder.method(request.method(), request.body());
                        return chain.proceed(builder.build());
                    }
                })
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }


    public static BasehttpModel getInstance(String url) {
        if (basehttpModel == null) {
            synchronized (BasehttpModel.class) {
                if (basehttpModel == null) {
                    basehttpModel = new BasehttpModel(url);
                }
            }

        }
        return basehttpModel;

    }


    public <T> T create(Class<T> service) {

        return retrofit.create(service);
    }


    public RequestBody getRequestBody(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

    }


    public static class Builder {
        private String json;
        private JSONObject jsonObject;
        private JSONArray jsonArray;

        public Builder() {
            jsonObject = new JSONObject();
            jsonArray = new JSONArray();
        }

        public Builder addParam(String key, Object object) {

            try {
                jsonObject.put(key, object);
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return this;
        }

        public Builder addParam(Object bean) {

            Gson gson = new Gson();
            json = gson.toJson(bean);
            return this;
        }

        public Builder addParams(List<Map<String, Object>> mapList) {
            if (mapList == null) {
                return this;
            }
            for (int i = 0; i < mapList.size(); i++) {
                JSONObject json = new JSONObject(mapList.get(i));
                jsonArray.put(json);
            }
            json = jsonArray.toString();

            return this;
        }


        public Builder addParams(String name, List<Map<String, Object>> mapList) {
            if (mapList == null) {
                return this;
            }
            for (int i = 0; i < mapList.size(); i++) {
                JSONObject json = new JSONObject(mapList.get(i));
                jsonArray.put(json);
            }


            try {
                jsonObject.put(name, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            json = jsonObject.toString();

            return this;
        }


        public RequestBody build() {
            if (TextUtils.isEmpty(json)) {
                json = jsonObject.toString();
            }
            Println.out("json",json);
            return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);

        }
    }


}
