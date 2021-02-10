package com.otpl.newcreate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;


import com.otpl.newcreate.R;
import com.otpl.newcreate.data.interfaces.ArrowBackPressed;
import com.otpl.newcreate.data.interfaces.DialogActionCallback;
import com.otpl.newcreate.data.model.api.ApiResponse;
import com.otpl.newcreate.data.model.local.ChangePassword;
import com.otpl.newcreate.databinding.ActivityResetPasswordBinding;
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

public class ResetPasswordActivity extends BaseActivity implements ArrowBackPressed {

    private static final String TAG = ResetPasswordActivity.class.getSimpleName();
    private Context mContext = ResetPasswordActivity.this;
    private ActivityResetPasswordBinding binding;
    private ChangePassword changePassword;
    private ApiHelper apiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_reset_password);
        binding.setResetPasswordActivity(this);
        setToolbarTitle("पासवर्ड रीसेट करें");
        showBackArrow();
        setArrowBackPressed(this);
        ScreenUtils.setupUI(binding.parentLayout, ResetPasswordActivity.this);
        changePassword = new ChangePassword();
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.hasExtra(AppConstants.MOBILE_NUMBER)) {
            String mobileNumber = intent.getExtras().getString(AppConstants.MOBILE_NUMBER);
            changePassword.setMobile(mobileNumber);
            binding.setChangePassword(changePassword);
            initialize();
        }
    }

    private void initialize() {
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        binding.mobileNumberEditText.addTextChangedListener(new MyTextWatcher(binding.mobileNumberEditText));
        binding.passwordEditText.addTextChangedListener(new MyTextWatcher(binding.passwordEditText));
        binding.confirmPasswordEditText.addTextChangedListener(new MyTextWatcher(binding.confirmPasswordEditText));
    }

    private boolean validateMobileNumber() {
        if (CommonUtils.isNullOrEmpty(changePassword.getMobile())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_number_required));
            requestFocus(binding.mobileNumberEditText);
            return false;
        } else if(!CommonUtils.isValidMobile(changePassword.getMobile())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_invalid_phone));
            requestFocus(binding.mobileNumberEditText);
            return false;
        } else {
            binding.mobileNumberInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNewPassword() {
        if (CommonUtils.isNullOrEmpty(changePassword.getPassword())) {
            binding.passwordInputLayout.setError(getString(R.string.error_new_password_required));
            requestFocus(binding.passwordEditText);
            return false;
        } else if (changePassword.getPassword().length() < 6){
            binding.passwordInputLayout.setError(getString(R.string.error_invalid_password));
            requestFocus(binding.passwordEditText);
            return false;
        } else {
            binding.passwordInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        String confirmPassword = binding.confirmPasswordEditText.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(confirmPassword)) {
            binding.confirmPasswordInputLayout.setError(getString(R.string.error_confirm_password_required));
            requestFocus(binding.confirmPasswordEditText);
            return false;
        } else if (confirmPassword.length() < 6) {
            binding.confirmPasswordInputLayout.setError(getString(R.string.error_invalid_password));
            requestFocus(binding.confirmPasswordEditText);
            return false;
        } else if (!confirmPassword.equals(changePassword.getPassword())) {
            binding.confirmPasswordInputLayout.setError(getString(R.string.error_new_psd_new_cnfm_psd_mismatching));
            requestFocus(binding.confirmPasswordEditText);
            return false;
        } else {
            binding.confirmPasswordInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void onClickChangePassword(View view) {
        if (!validateMobileNumber()) {
            return;
        }

        if (!validateNewPassword()) {
            return;
        }

        if (!validateConfirmPassword()) {
            return;
        }

        checkNetwork();
    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            execute();
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }

    private void execute() {
        showProgress(true);
        Logger.d(TAG, JSONConverter.getInstance().getString(changePassword));
        RequestBody requestBody = RequestBody.create(JSONConverter.getInstance().getString(changePassword),
                okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<ApiResponse> registrationCall = apiHelper.changePassword(requestBody);
        registrationCall.enqueue(new Callback<ApiResponse>() {
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
                                            Intent intent = new Intent(mContext, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                case R.id.passwordEditText:
                    validateNewPassword();
                    break;
                case R.id.confirmPasswordEditText:
                    validateConfirmPassword();
                    break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void arrowBackPressed() {
        onBackPressed();
    }
}
