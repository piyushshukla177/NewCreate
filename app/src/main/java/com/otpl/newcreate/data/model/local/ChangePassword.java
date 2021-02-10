package com.otpl.newcreate.data.model.local;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.google.gson.annotations.SerializedName;
import com.otpl.newcreate.BR;

public class ChangePassword extends BaseObservable {
    @SerializedName("password")
    private String password;

    @SerializedName("mobile")
    private String mobile;

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        notifyPropertyChanged(BR.mobile);
    }
}
