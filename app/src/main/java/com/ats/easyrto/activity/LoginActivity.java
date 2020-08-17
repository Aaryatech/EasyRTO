package com.ats.easyrto.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.fcm.SharedPrefManager;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.Info;
import com.ats.easyrto.model.LoginResponse;
import com.ats.easyrto.util.CommonDialog;
import com.ats.easyrto.util.CustomSharedPreference;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edMobile, edPass;
    private Button btnLogin;
    private TextView tvForgetPass, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edMobile = findViewById(R.id.edMobile);
        edPass = findViewById(R.id.edPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgetPass = findViewById(R.id.tvForgetPass);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(this);
        tvForgetPass.setOnClickListener(this);
        tvRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {

            String strMobile, strPass;

            boolean isValidMobile = false, isValidPass = false;

            strMobile = edMobile.getText().toString();
            strPass = edPass.getText().toString();

            if (strMobile.isEmpty()) {
                edMobile.setError("required");
            } else if (strMobile.length() != 10) {
                edMobile.setError("required 10 digits");
            } else {
                edMobile.setError(null);
                isValidMobile = true;
            }

            if (strPass.isEmpty()) {
                edPass.setError("required");
            } else {
                edPass.setError(null);
                isValidPass = true;
            }

            if (isValidMobile && isValidPass) {

                doLogin(strMobile, strPass);

            }


        } else if (view.getId() == R.id.tvForgetPass) {

            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            intent.putExtra("mobile", "" + edMobile.getText().toString());
            startActivity(intent);

        } else if (view.getId() == R.id.tvRegister) {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            finish();
        }
    }


    public void doLogin(String mobile, String pass) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<LoginResponse> listCall = Constants.myInterface.doLogin(mobile, pass);
            listCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("User Data : ", "------------" + response.body());

                            LoginResponse data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                            } else {

                                if (!data.isError()) {

                                    //Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                    Gson gson = new Gson();
                                    String json = gson.toJson(data.getCust());
                                    CustomSharedPreference.putString(LoginActivity.this, CustomSharedPreference.KEY_CUSTOMER, json);

                                    String token = SharedPrefManager.getmInstance(LoginActivity.this).getDeviceToken();
                                    Log.e("Token : ", "---------" + token);
                                    updateToken(data.getCust().getCustId(), token);


                                } else {
                                    Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                }
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateToken(int custId, String token) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateToken(custId, token);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE TOKEN : ", "------------" + response.body());

                            Info data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();

                            } else {

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();

                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
