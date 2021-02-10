package com.otpl.newcreate.data.model.local;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.google.gson.annotations.SerializedName;
import com.otpl.newcreate.BR;


public class ForgotPassword extends BaseObservable {
    @SerializedName("mobile")
    private String mobileNumber;

    @SerializedName("otp")
    private String otp;

    @Bindable
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        notifyPropertyChanged(BR.mobileNumber);
    }

    @Bindable
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
        notifyPropertyChanged(BR.otp);
    }
}

