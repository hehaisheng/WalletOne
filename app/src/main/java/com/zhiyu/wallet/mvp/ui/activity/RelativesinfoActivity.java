package com.zhiyu.wallet.mvp.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.widget.LD_ActionSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/6.
 */

public class RelativesinfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.UserInfoIView{

    @BindView(R.id.et_firstname)
    EditText firstname;
    @BindView(R.id.et_urgentname)
    EditText urgentname;
    @BindView(R.id.ly_firstContacts)
    LinearLayout ly_firstContacts;
    @BindView(R.id.ly_urgentContacts)
    LinearLayout ly_urgentContacts;
    @BindView(R.id.ly_firstTel)
    LinearLayout ly_firstTel;
    @BindView(R.id.ly_urgentTel)
    LinearLayout ly_urgentTel;
    @BindView(R.id.tv_firstTel)
    TextView firstTel;
    @BindView(R.id.tv_urgentTel)
    TextView urgentTel;
    @BindView(R.id.tv_firstContacts)
    TextView firstContacts;
    @BindView(R.id.tv_urgentContacts)
    TextView urgentContacts;


    @BindView(R.id.tv_title)
    TextView tvtitle;
    @BindView(R.id.iv_title_back)
    LinearLayout ivtitle_back;

    @BindView(R.id.btn_confirm)
    Button confirm;

    public String username,usernumber,json;
    public int firType = 0;
    public int urgType = 1;
    private List<HashMap<String ,Object>> result;

    @Override
    protected void initData() {
        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);
      HashMap<String ,Object> map=  presenter.requestUesrRelatives();
      if(null!=map){
          firstname.setText(map.get("firstContactsname").toString());
          firstTel.setText(map.get("firstContactstel").toString());;
          firstContacts.setText(map.get("firstContactsship").toString());;
          urgentname.setText(map.get("urgentContractname").toString());;
          urgentTel.setText(map.get("urgentContracttel").toString());;
          urgentContacts.setText(map.get("urgentContractship").toString());;
      }
        getAllContacts();

    }

    @Override
    protected void initTitle() {
        ivtitle_back.setVisibility(View.VISIBLE);
        tvtitle.setText("亲友信息");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_relativesinfo;
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }


    @OnClick(R.id.iv_title_back)
    public void onBack(){
        removeCurrentActivity();

    }

    @OnClick(R.id.ly_firstTel)
    public void firstTel(){
        Intent intent =new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,firType);

    }
    @OnClick(R.id.ly_urgentTel)
    public void urgentTel(){
        Intent intent =new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,urgType);

    }


    @OnClick(R.id.btn_confirm)
    public void onConfirm(){
        String ftn=firstname.getText().toString().trim();
        String ftt=firstTel.getText().toString().trim();
        String fts=firstContacts.getText().toString().trim();
        String urn=urgentname.getText().toString().trim();
        String urt=urgentTel.getText().toString().trim();
        String urs=urgentContacts.getText().toString().trim();
        presenter.saveUserRelatives( ftn, ftt, fts, urn, urt, urs);
        presenter.saveUserAllContacts(result);
    }

    @OnClick(R.id.ly_firstContacts)
    public void onfirstContract(){
        final String[] firstContractitem= new String[]{"父亲","母亲","亲属","朋友","同事","配偶"};
        LD_ActionSheet.showActionSheet(this, firstContractitem, "取消", new LD_ActionSheet.Builder.OnActionSheetselectListener() {
            @Override
            public void itemSelect(Dialog dialog, int index) {
                dialog.dismiss();
                if(0 <= index-1){
                    firstContacts.setText(firstContractitem[index-1]);
                }
            }
        });
    }

    @OnClick(R.id.ly_urgentContacts)
    public void onurgentContract(){
        final String[] urgentContractitem= new String[]{"父亲","母亲","亲属","朋友","同事","配偶"};
        LD_ActionSheet.showActionSheet(this, urgentContractitem, "取消", new LD_ActionSheet.Builder.OnActionSheetselectListener() {
            @Override
            public void itemSelect(Dialog dialog, int index) {
                dialog.dismiss();
                if(0 <= index-1){
                    urgentContacts.setText(urgentContractitem[index-1]);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            // 获得DATA表中的名字
            username = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            System.out.println(username+" username");
            // 条件为联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            System.out.println(contactId +" id");
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            if(phone==null){
                return;
            }
            while (phone.moveToNext()) {
                usernumber = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
               // et_mobile.setText(usernumber + " (" + username + ")");
                System.out.println(usernumber+" usernameber");
            }
            phone.close();


            switch (requestCode){
                case 0:
                    firstTel.setText(usernumber);
                    break;
                case 1:
                    urgentTel.setText(usernumber);
                    break;
                    default:break;


            }
        }

    }

    public void getAllContacts(){
        ContentResolver reContentResolverol = getContentResolver();
        //获取所有电话信息（而不是联系人信息），这样方便展示
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 姓名
                ContactsContract.CommonDataKinds.Phone.NUMBER,// 电话号码
        };
        Cursor allphone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if (allphone == null) {
            return ;
        }
        //最终要返回的数据
       result = new ArrayList<>();
        String temp = "";  HashMap<String,Object> map ;
        while (allphone.moveToNext()) {
            String name = allphone.getString(0);
            String number = allphone.getString(1);
            if(temp.equals(name)){
                map=result.get(result.size()-1);
                map.remove("contactphone2");
                map.put("contactphone2",number);
            }else {
                temp= name;
                map = new HashMap<>();
                map.put("contact",name);
                map.put("contactphone1",number);
                map.put("contactphone2","");
                result.add(map);
            }

            //保存到集合里
            //Println.out("name",name);
            //Println.out("number",number);
        }
        allphone.close();

    }




    @Override
    public void showSuccess(String msg) {
        UIUtils.toast(msg,false);
        finish();
    }

    @Override
    public void showFailure(String msg) {

    }

    @Override
    public void connectFailure(String msg) {


    }
}
