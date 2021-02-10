package com.otpl.newcreate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.otpl.newcreate.R;
import com.otpl.newcreate.data.interfaces.DialogActionCallback;
import com.otpl.newcreate.data.local.prefs.PrefsHelper;
import com.otpl.newcreate.data.model.api.ApiResponse;
import com.otpl.newcreate.data.model.api.LoggedInUser;
import com.otpl.newcreate.data.model.local.LoginModel;
import com.otpl.newcreate.databinding.ActivityLoginBinding;
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


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext = LoginActivity.this;
    private ActivityLoginBinding binding;
    private LoginModel loginModel;
    private ApiHelper apiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLoginActivity(this);
        binding.setButtonVisibility(true);
        binding.setLoaderVisibility(false);
        ScreenUtils.setupUI(binding.parentLayout, LoginActivity.this);
        loginModel = new LoginModel();
        binding.setLoginModel(loginModel);

        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        binding.mobileNumberEditText.addTextChangedListener(new MyTextWatcher(binding.mobileNumberEditText));
        binding.passwordEditText.addTextChangedListener(new MyTextWatcher(binding.passwordEditText));

    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            loginExecute();
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }


    private void loginExecute() {
        binding.setLoaderVisibility(true);
        binding.setButtonVisibility(false);
        Logger.d(TAG, JSONConverter.getInstance().getString(loginModel));
        final RequestBody requestBody = RequestBody.create(JSONConverter.getInstance().getString(loginModel),
                okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<ApiResponse<LoggedInUser>> loginCall = apiHelper.login(requestBody);
        loginCall.enqueue(new Callback<ApiResponse<LoggedInUser>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LoggedInUser>> call,
                                   @NonNull Response<ApiResponse<LoggedInUser>> response) {
                binding.setLoaderVisibility(false);
                binding.setButtonVisibility(true);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().Success()) {
                            LoggedInUser loggedInUser = response.body().getData();
                            String welcome = getString(R.string.welcome) + loggedInUser.getName()+"!";
                            ViewUtils.showToast(welcome);

                            PrefsHelper.putString(mContext, AppConstants.LOGGED_IN_USER,
                                    JSONConverter.getInstance().getString(loggedInUser));
                            // Intent intent = new Intent(mContext, DashboardActivity.class);
                            // startActivity(intent);
                            ViewUtils.showToast(response.body().getMessage());
                            finish();
                        } else {
                            /*ViewUtils.showErrorDialog(mContext, apiResponse.getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {
                                            binding.passwordEditText.requestFocus();
                                        }
                                    });*/
                            ViewUtils.showToast(response.body().getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LoggedInUser>> call,
                                  @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    binding.setLoaderVisibility(false);
                    binding.setButtonVisibility(true);
                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });
    }
    public void onClickLogin(View view) {
        submitForm();
    }

    public void onClickForgotPassword(View view) {
        Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    private void submitForm() {
        if (!validateMobileNumber()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        checkNetwork();
    }

    public void onClickSignUp(View view) {
        Intent i = new Intent(mContext, SignUpActivity.class);
        startActivity(i);
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
                    validatePassword();
                    break;
            }
        }
    }

    private boolean validateMobileNumber() {
        if (CommonUtils.isNullOrEmpty(loginModel.getMobileNumber())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_registered_phone_required));
            requestFocus(binding.mobileNumberEditText);
            return false;
        }else if(!CommonUtils.isValidMobile(loginModel.getMobileNumber())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_invalid_phone));
            requestFocus(binding.mobileNumberEditText);
            return false;
        } else {
            binding.mobileNumberInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (CommonUtils.isNullOrEmpty(loginModel.getPassword())) {
            binding.passwordInputLayout.setError(getString(R.string.error_password_required));
            requestFocus(binding.passwordEditText);
            return false;
        } else {
            binding.passwordInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

