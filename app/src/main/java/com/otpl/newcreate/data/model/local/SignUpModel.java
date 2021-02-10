package com.otpl.newcreate.data.model.local;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.otpl.newcreate.BR;


public class SignUpModel extends BaseObservable {

    @SerializedName("name")
    private String fullName;

    @SerializedName("fatherName")
    private String fatherName;

    @SerializedName("mobile")
    private String mobileNumber;

    @SerializedName("districtId")
    private int district;

    @SerializedName("password")
    private String password;

    @Bindable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        notifyPropertyChanged(BR.fullName);
    }

    @Bindable
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
        notifyPropertyChanged(BR.fatherName);
    }

    @Bindable
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        notifyPropertyChanged(BR.mobileNumber);
    }

    @Bindable
    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
        notifyPropertyChanged(BR.district);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}

