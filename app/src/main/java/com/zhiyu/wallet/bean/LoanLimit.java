package com.zhiyu.wallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 1
 * Created by Administrator on 2018/11/7.
 */

public class LoanLimit {


    private List<LoanLimitItem> list;

    public LoanLimit (){

        list=new ArrayList<>();
    }
    public List<LoanLimitItem> getList() {
        return list;
    }

    public void setList(List<LoanLimitItem> list) {
        this.list = list;
    }

   public static class LoanLimitItem implements Parcelable{
        private int id ;
        private double money;
        private double servicecharge;
        private double interest;
        private double lixifeiyong;
        private double accountamount;
        private String reserve1;
        private int reserve2;
       private double riskmanagementfees;
       private double indentityauthfee;
       private double phoneverfree;
       private double bankcardverfee;
       private double mathchupfee;
       private double interestfee;


       protected LoanLimitItem(Parcel in) {
           id = in.readInt();
           money = in.readDouble();
           servicecharge = in.readDouble();
           interest = in.readDouble();
           lixifeiyong = in.readDouble();
           accountamount = in.readDouble();
           reserve1 = in.readString();
           reserve2 = in.readInt();
           riskmanagementfees =  in.readDouble();
           indentityauthfee = in.readDouble();
           phoneverfree = in.readDouble();
           bankcardverfee = in.readDouble();
           mathchupfee = in.readDouble();
           interestfee = in.readDouble();
       }

       public static final Creator<LoanLimitItem> CREATOR = new Creator<LoanLimitItem>() {
           @Override
           public LoanLimitItem createFromParcel(Parcel in) {
               return new LoanLimitItem(in);
           }

           @Override
           public LoanLimitItem[] newArray(int size) {
               return new LoanLimitItem[size];
           }
       };

       public int getId() {
           return id;
       }

       public void setId(int id) {
           this.id = id;
       }

       public double getMoney() {
           return money;
       }

       public void setMoney(float money) {
           this.money = money;
       }

       public double getServicecharge() {
           return servicecharge;
       }

       public void setServicecharge(float servicecharge) {
           this.servicecharge = servicecharge;
       }

       public double getInterest() {
           return interest;
       }

       public void setInterest(float interest) {
           this.interest = interest;
       }

       public double getLixifeiyong() {
           return lixifeiyong;
       }

       public void setLixifeiyong(float lixifeiyong) {
           this.lixifeiyong = lixifeiyong;
       }

       public double getAccountamount() {
           return accountamount;
       }

       public void setAccountamount(float accountamount) {
           this.accountamount = accountamount;
       }

       public String getReserve1() {
           return reserve1;
       }

       public void setReserve1(String reserve1) {
           this.reserve1 = reserve1;
       }

       public int getReserve2() {
           return reserve2;
       }

       public void setReserve2(int reserve2) {
           this.reserve2 = reserve2;
       }

       public double getRiskmanagementfees() {
           return riskmanagementfees;
       }

       public double getIndentityauthfee() {
           return indentityauthfee;
       }

       public double getPhoneverfree() {
           return phoneverfree;
       }

       public double getBankcardverfee() {
           return bankcardverfee;
       }

       public double getMathchupfee() {
           return mathchupfee;
       }

       public double getInterestfee() {
           return interestfee;
       }

       @Override
       public int describeContents() {
           return 0;
       }

       @Override
       public void writeToParcel(Parcel dest, int flags) {
           dest.writeInt(id);
           dest.writeDouble(money);
           dest.writeDouble(servicecharge);
           dest.writeDouble(interest);
           dest.writeDouble(lixifeiyong);
           dest.writeDouble(accountamount);
           dest.writeString(reserve1);
           dest.writeInt(reserve2);
           dest.writeDouble(riskmanagementfees);
           dest.writeDouble(indentityauthfee);
           dest.writeDouble(phoneverfree);
           dest.writeDouble(bankcardverfee);
           dest.writeDouble(mathchupfee);
           dest.writeDouble(interestfee);


       }
   }
}
