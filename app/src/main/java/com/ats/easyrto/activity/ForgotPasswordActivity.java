package com.ats.easyrto.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.interfaces.InterfaceApi;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.Info;
import com.ats.easyrto.util.CommonDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edMobile, edPass, edOTP;
    private Button btnSubmit, btnOTPSubmit;
    private LinearLayout llForm, llOTP;

    String strMobile, strNewPass;
    private String randomNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color='#000000'>Forgot Password</font>"));

        edMobile = findViewById(R.id.edMobile);
        edPass = findViewById(R.id.edPass);
        edOTP = findViewById(R.id.edOTP);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnOTPSubmit = findViewById(R.id.btnOTPSubmit);
        llForm = findViewById(R.id.llForm);
        llOTP = findViewById(R.id.llOTP);

        btnSubmit.setOnClickListener(this);
        btnOTPSubmit.setOnClickListener(this);

        try {

            String number = getIntent().getStringExtra("mobile");
            if (strMobile != null) {
                edMobile.setText("" + strMobile);
                strMobile = number;
            }

        } catch (Exception e) {
            Log.e("FORGOT PASS ACT : ", " Exception : " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSubmit) {

            strMobile = edMobile.getText().toString();
            strNewPass = edPass.getText().toString();

            boolean isValidMobile = false, isValidPass = false;

            if (strMobile.isEmpty()) {
                edMobile.setError("required");
            } else {
                edMobile.setError(null);
                isValidMobile = true;
            }

            if (strNewPass.isEmpty()) {
                edPass.setError("required");
            } else {
                edPass.setError(null);
                isValidPass = true;
            }

            if (isValidMobile && isValidPass) {
                checkMobileExists(strMobile);
            }


        } else if (view.getId() == R.id.btnOTPSubmit) {

            String strOTP = edOTP.getText().toString();

            if (strOTP.isEmpty()) {
                edOTP.setError("required");
            } else if (!strOTP.equalsIgnoreCase(randomNumber)) {
                edOTP.setError("OTP not matched");
            } else {
                updateNewPassword(strMobile, strNewPass);

            }

        }

    }

    public static String random(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken.toString();
    }


    public void checkMobileExists(final String mobile) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.checkMobileExists(mobile);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CHECK MOBILE EXIST : ", " - " + response.body());

                            Info info = response.body();
                            if (info.getError()) {

                                randomNumber = random(6);
                                Log.e("OTP NUMBER : ",""+randomNumber);

                                sendOTP(mobile, randomNumber);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this, R.style.AlertDialogTheme);
                                builder.setMessage("User not registered");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(ForgotPasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendOTP(String mobile, String OTPNumber) {
        if (Constants.isOnline(this)) {

            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Accept", "application/json")
                                    .method(original.method(), original.body())
                                    .build();

                            okhttp3.Response response = chain.proceed(request);
                            return response;
                        }
                    })
                    .readTimeout(10000, TimeUnit.SECONDS)
                    .connectTimeout(10000, TimeUnit.SECONDS)
                    .writeTimeout(10000, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://control.bestsms.co.in/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();

            InterfaceApi myInterface = retrofit.create(InterfaceApi.class);

            String message = "OTP is " + OTPNumber + " for forget password in EZI_RTO App , Please do not share this OTP.";

            Call<String> listCall = myInterface.sendOTP("246089Au56zI8BbKQE5bdd7464", mobile, message, "EZIRTO", 4, 91);
            listCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    Log.e("OTP RESPONSE : ", " " + response.body());

                    llForm.setVisibility(View.GONE);
                    llOTP.setVisibility(View.VISIBLE);

                    commonDialog.dismiss();


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Unable to send OTP, please try again", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNewPassword(String mobile, String pass) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateNewPassword(mobile, pass);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE NEW PASS : ", " - " + response.body());

                            Info info = response.body();
                            if (!info.getError()) {

                                finish();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this, R.style.AlertDialogTheme);
                                builder.setMessage("" + info.getMessage());
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(ForgotPasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
