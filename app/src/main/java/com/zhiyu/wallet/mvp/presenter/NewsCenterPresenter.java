package com.zhiyu.wallet.mvp.presenter;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.zhiyu.wallet.bean.News;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.RepayService;
import com.zhiyu.wallet.http.UserInfoService;
import com.zhiyu.wallet.mvp.contract.NewsCenterContract;
import com.zhiyu.wallet.mvp.contract.RepayContract;
import com.zhiyu.wallet.util.Println;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * @author zhiyu
 * Created by Administrator on 2018/11/7.
 */

public class NewsCenterPresenter extends BasePresenter<NewsCenterContract.NewsCenterIView> implements NewsCenterContract.NewsCenterIModel{

    private UserInfoService userInfoService;
    private BasehttpModel basehttpModel;

    public NewsCenterPresenter(){
        basehttpModel=BasehttpModel.getInstance(Constant.Local_ipAddress3);
        userInfoService=basehttpModel.create(UserInfoService.class);

    }

    @Override
    public void requestNewsData() {
        basehttpModel.observa(userInfoService.querynews(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                List<News> newsList= new ArrayList<>();
                try {
                    String resbody = responseBody.string();
                    JSONArray jsonArray = new JSONArray(resbody);
                    Gson gson = new Gson();

                    for(int i =0;i<jsonArray.length();i++){
                        News news = gson.fromJson(jsonArray.getJSONObject(i).toString(),News.class);
                        newsList.add(news);
                    }
                    Iview.showNewsData(newsList);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.showErrorMsg("网络连接失败");
                Println.err("newsdata",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestHelpcenterData(AssetManager assetManager) {
        String line ="";
        int i = 0;
        List<News> helplist= new ArrayList<>();
        News hnews = new News();
        if(assetManager!=null){
                try {
                InputStream inputStream = assetManager.open("help.txt");
                    if(inputStream!=null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                            while ((line=bufferedReader.readLine()) !=null){
                                Println.out("txt",line);
                                i++;
                                if(i%2==0){
                                    hnews.setContent(line);
                                    helplist.add(hnews);
                                }else {
                                    hnews = new News();
                                    hnews.setTitle(line);
                                }
                            }
                            inputStream.close();

                    }else {
                        Println.err("file","file not exists~!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        Iview.showHelpcenterData(helplist);
    }
}
