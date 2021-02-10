package com.otpl.newcreate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.otpl.newcreate.R;
import com.otpl.newcreate.data.interfaces.DialogActionCallback;
import com.otpl.newcreate.data.model.api.ApiResponse;
import com.otpl.newcreate.data.model.local.ForgotPassword;
import com.otpl.newcreate.databinding.ActivityForgotPasswordBinding;
import com.otpl.newcreate.network.ApiClient;
import com.otpl.newcreate.network.ApiHelper;
import com.otpl.newcreate.utils.AppConstants;
import com.otpl.newcreate.utils.CommonUtils;
import com.otpl.newcreate.utils.JSONConverter;
import com.otpl.newcreate.utils.Logger;
import com.otpl.newcreate.utils.NetworkUtils;
import com.otpl.newcreate.utils.ScreenUtils;
import com.otpl.newcreate.utils.ViewUtils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordActivity extends AppCompatActivity {

    public static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private Context mContext = ForgotPasswordActivity.this;
    private ActivityForgotPasswordBinding binding;
    private ForgotPassword forgotPassword;
    private ApiHelper apiHelper;
    private boolean isRequestOtp, isVerifyOtp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binding.setForgotPasswordActivity(this);
        ScreenUtils.setupUI(binding.parentLayout, ForgotPasswordActivity.this);
        setUpAppBar();
        binding.setToolbarTitle("पासवर्ड भूल गए?");
        forgotPassword = new ForgotPassword();
        binding.setForgotPassword(forgotPassword);

        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        binding.mobileNumberEditText.addTextChangedListener(new MyTextWatcher(binding.mobileNumberEditText));
        binding.otpEditText.addTextChangedListener(new MyTextWatcher(binding.otpEditText));

    }

    private void setUpAppBar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showProgress(boolean b) {
        ViewUtils.showProgress(b, binding.viewForm, binding.progressBar);
    }

    public void onClickRequestOTP(View view) {
        if (!validateMobileNumber()) {
            return;
        }

        isRequestOtp = true;
        checkNetwork();
    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            if (isRequestOtp) {
                requestOtp();
            } else if (isVerifyOtp){
                verifyOtp();
            }
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }


    private void requestOtp() {
        isRequestOtp = false;
        showProgress(true);
        Call<ApiResponse> requestOtpCall = apiHelper.requestOtp(forgotPassword.getMobileNumber());
        requestOtpCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().Success()) {
                            ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {
                                            binding.getCodeButton.setVisibility(View.GONE);
                                            binding.otpLayout.setVisibility(View.VISIBLE);
                                        }
                                    });
                        } else {
                            ViewUtils.showToast(response.body().getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgress(false);
                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });
    }

    public void onClickVerifyOTP(View view) {
        if (!validateMobileNumber()) {
            return;
        }

        if (!validateOtp()) {
            return;
        }

        isVerifyOtp = true;
        checkNetwork();
    }

    private void verifyOtp() {
        isVerifyOtp = false;
        showProgress(true);
        Logger.d(TAG, JSONConverter.getInstance().getString(forgotPassword));
        RequestBody requestBody = RequestBody.create(JSONConverter.getInstance().getString(forgotPassword),
                okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<ApiResponse> verifyOtpCall = apiHelper.verifyOtp(requestBody);
        verifyOtpCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().Success()) {
                            ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {
                                            Intent intent = new Intent(mContext, ResetPasswordActivity.class);
                                            intent.putExtra(AppConstants.MOBILE_NUMBER, forgotPassword.getMobileNumber());
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        } else {
                            ViewUtils.showToast(response.body().getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgress(false);
                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.mobileNumberEditText:
                    validateMobileNumber();
                    break;
                case R.id.otpEditText:
                    validateOtp();
                    break;
            }
        }
    }

    private boolean validateMobileNumber() {
        if (CommonUtils.isNullOrEmpty(forgotPassword.getMobileNumber())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_registered_phone_required));
            requestFocus(binding.mobileNumberEditText);
            return false;
        } else if(!CommonUtils.isValidMobile(forgotPassword.getMobileNumber())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_invalid_phone));
            requestFocus(binding.mobileNumberEditText);
            return false;
        } else {
            binding.mobileNumberInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateOtp() {
        if (CommonUtils.isNullOrEmpty(forgotPassword.getOtp())) {
            binding.otpInputLayout.setError(getString(R.string.error_otp_required));
            requestFocus(binding.otpEditText);
            return false;
        } else if (!CommonUtils.isOtpValid(forgotPassword.getOtp())) {
            binding.otpInputLayout.setError(getString(R.string.error_invalid_otp));
            requestFocus(binding.otpEditText);
            return false;
        } else {
            binding.otpInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
