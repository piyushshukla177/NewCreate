package com.otpl.newcreate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.otpl.newcreate.R;
import com.otpl.newcreate.data.interfaces.DialogActionCallback;
import com.otpl.newcreate.data.model.api.ApiResponse;
import com.otpl.newcreate.data.model.api.District;
import com.otpl.newcreate.data.model.local.SignUpModel;
import com.otpl.newcreate.databinding.ActivitySignUpBinding;
import com.otpl.newcreate.network.ApiClient;
import com.otpl.newcreate.network.ApiHelper;
import com.otpl.newcreate.utils.CommonUtils;
import com.otpl.newcreate.utils.JSONConverter;
import com.otpl.newcreate.utils.Logger;
import com.otpl.newcreate.utils.NetworkUtils;
import com.otpl.newcreate.utils.ScreenUtils;
import com.otpl.newcreate.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = SignUpActivity.class.getSimpleName();
    private Context mContext = SignUpActivity.this;
    private ActivitySignUpBinding binding;
    private SignUpModel signUpModel;
    private ApiHelper apiHelper;
    private ArrayAdapter<CharSequence> adapter;
    private boolean isGetDistrict, isSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        //setupWindowAnimations();
        binding.setSignUpActivity(this);
        ScreenUtils.setupUI(binding.parentLayout, SignUpActivity.this);
        setUpAppBar();
        binding.setToolbarTitle(getResources().getString(R.string.toolbar_title_sign_up));
        signUpModel = new SignUpModel();
        binding.setSignUpModel(signUpModel);

        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        adapter = ArrayAdapter.createFromResource(mContext,
                R.array.select_district_array, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        binding.fullNameEditText.addTextChangedListener(new MyTextWatcher(binding.fullNameEditText));
        binding.fatherNameEditText.addTextChangedListener(new MyTextWatcher(binding.fatherNameEditText));
        binding.mobileNumberEditText.addTextChangedListener(new MyTextWatcher(binding.mobileNumberEditText));
        binding.passwordEditText.addTextChangedListener(new MyTextWatcher(binding.passwordEditText));
        binding.confirmPasswordEditText.addTextChangedListener(new MyTextWatcher(binding.confirmPasswordEditText));

        binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String districtName = parent.getSelectedItem().toString();
                if (!districtName.equals("जनपद का चयन करें")) {
                    District district = (District) parent.getSelectedItem();
                    signUpModel.setDistrict(district.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        isGetDistrict = true;
        checkNetwork();
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

    public void onClickSignUp(View view) {
        submitForm();
    }

    public void onClickSignIn(View view) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateFatherName()) {
            return;
        }

        if (!validateMobileNumber()) {
            return;
        }

        if (!validateDistrictSpinner()) {
            return;
        }

        if (!validateNewPassword()) {
            return;
        }

        if (!validateConfirmPassword()) {
            return;
        }

        isSignUp = true;
        checkNetwork();
    }

    private void getDistrict() {
        isGetDistrict = false;
        showProgress(true);
        Call<ApiResponse<List<District>>> getDistrictCall = apiHelper.getDistrict(5);
        getDistrictCall.enqueue(new Callback<ApiResponse<List<District>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<District>>> call,
                                   Response<ApiResponse<List<District>>> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().Success()) {
                            List<District> districtList = new ArrayList<>();
                            District district = new District();
                            district.setName("Select District");
                            district.setNameH("जनपद का चयन करें");
                            district.setId(0);
                            districtList.add(district);
                            districtList.addAll(response.body().getData());

                            ArrayAdapter<District> adapter = new ArrayAdapter<>(mContext,
                                    R.layout.simple_spinner_item, districtList);
                            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            binding.districtSpinner.setAdapter(adapter);
                        } else {
                            binding.districtSpinner.setAdapter(adapter);
                        }
                    }
                } else {
                    binding.districtSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<District>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    showProgress(false);
                    ViewUtils.showToast(t.getLocalizedMessage());
                    binding.districtSpinner.setAdapter(adapter);
                }
                t.printStackTrace();
            }
        });
    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            if (isGetDistrict) {
                getDistrict();
            } else if (isSignUp) {
                execute();
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

    private void execute() {
        isSignUp = false;
        showProgress(true);
        Logger.d(TAG, JSONConverter.getInstance().getString(signUpModel));
        RequestBody requestBody = RequestBody.create(JSONConverter.getInstance().getString(signUpModel),
                okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<ApiResponse> registrationCall = apiHelper.registration(requestBody);
        registrationCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ViewUtils.showToast(response.body().getMessage());
                        if (response.body().Success()) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
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

    private boolean validateMobileNumber() {
        if (CommonUtils.isNullOrEmpty(signUpModel.getMobileNumber())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_number_required));
            requestFocus(binding.mobileNumberEditText);
            return false;
        }else if(!CommonUtils.isValidMobile(signUpModel.getMobileNumber())) {
            binding.mobileNumberInputLayout.setError(getString(R.string.error_invalid_phone));
            requestFocus(binding.mobileNumberEditText);
            return false;
        } else {
            binding.mobileNumberInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        if (CommonUtils.isNullOrEmpty(signUpModel.getFullName())) {
            binding.fullNameInputLayout.setError(getString(R.string.error_name_required));
            requestFocus(binding.fullNameEditText);
            return false;
        } else {
            binding.fullNameInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFatherName() {
        if (CommonUtils.isNullOrEmpty(signUpModel.getFatherName())) {
            binding.fatherNameInputLayout.setError(getString(R.string.error_father_name_required));
            requestFocus(binding.fatherNameEditText);
            return false;
        } else {
            binding.fatherNameInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDistrictSpinner() {
        String subStationName = binding.districtSpinner.getSelectedItem().toString().trim();
        if (subStationName.equals("जनपद का चयन करें")) {
            ViewUtils.showToast("कृपया जनपद का चयन करें।");
            return false;
        }
        return true;
    }

    private boolean validateNewPassword() {
        if (CommonUtils.isNullOrEmpty(signUpModel.getPassword())) {
            binding.passwordInputLayout.setError(getString(R.string.error_password_required));
            requestFocus(binding.passwordEditText);
            return false;
        } else if (signUpModel.getPassword().length() < 6){
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
        } else if (confirmPassword.length() < 6){
            binding.confirmPasswordInputLayout.setError(getString(R.string.error_invalid_password));
            requestFocus(binding.confirmPasswordEditText);
            return false;
        } else if (!confirmPassword.equals(signUpModel.getPassword())){
            binding.confirmPasswordInputLayout.setError(getString(R.string.error_psd_cnfm_psd_mismatching));
            requestFocus(binding.confirmPasswordEditText);
            return false;
        }else {
            binding.confirmPasswordInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.fullNameEditText:
                    validateName();
                    break;
                case R.id.fatherNameEditText:
                    validateFatherName();
                    break;
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
