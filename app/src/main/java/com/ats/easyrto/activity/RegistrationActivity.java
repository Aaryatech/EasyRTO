package com.ats.easyrto.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.interfaces.InterfaceApi;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.util.CommonDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edName, edMobile, edDOB, edEmail, edAddress, edPassword, edConfirmPassword, edOTP, edTime;
    private Button btnRegister, btnSubmit;
    private LinearLayout llForm, llOTP;
    private TextView tvDOB, tvLogin;

    String strName, strMobile, strDOB, strEmail, strAddress, strPass, strConfirmPass, strOTP, strTime;

    int yyyy, mm, dd;
    long dateMillis;

    private String randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color='#000000'>Register</font>"));


        edName = findViewById(R.id.edName);
        edMobile = findViewById(R.id.edMobile);
        edDOB = findViewById(R.id.edDOB);
        edEmail = findViewById(R.id.edEmail);
        edAddress = findViewById(R.id.edAddress);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        edOTP = findViewById(R.id.edOTP);
        btnRegister = findViewById(R.id.btnRegister);
        btnSubmit = findViewById(R.id.btnSubmit);
        llForm = findViewById(R.id.llForm);
        llOTP = findViewById(R.id.llOTP);
        tvDOB = findViewById(R.id.tvDOB);
        tvLogin = findViewById(R.id.tvLogin);
        edTime = findViewById(R.id.edTime);

        btnRegister.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        edDOB.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        edTime.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegister) {

            strName = edName.getText().toString();
            strMobile = edMobile.getText().toString();
            strDOB = tvDOB.getText().toString();
            strEmail = edEmail.getText().toString();
            strPass = edPassword.getText().toString();
            strConfirmPass = edConfirmPassword.getText().toString();
            strOTP = edOTP.getText().toString();
            strAddress = edAddress.getText().toString();
            //strTime = edTime.getText().toString();
            strTime = "";

            boolean isValidName = false, isValidMobile = false, isValidDOB = false, isValidEmail = false, isValidPass = false, isValidConfirmPass = false, isValidAddress = false, isValidTime = false;

            if (strName.isEmpty()) {
                edName.setError("required");
            } else {
                edName.setError(null);
                isValidName = true;
            }

            if (strMobile.isEmpty()) {
                edMobile.setError("required");
            } else if (strMobile.length() != 10) {
                edMobile.setError("required 10 digits");
            } else {
                edMobile.setError(null);
                isValidMobile = true;
            }

            if (strDOB.isEmpty()) {
                edDOB.setError("required");
            } else {
                edDOB.setError(null);
                isValidDOB = true;
            }

            /*if (strEmail.isEmpty()) {
                edEmail.setError("required");
            } else if (!isValidEmailAddress(strEmail)) {
                edEmail.setError("invalid email");
            } else {
                edEmail.setError(null);
                isValidEmail = true;
            }*/

            if (!strEmail.isEmpty()) {
                if (!isValidEmailAddress(strEmail)) {
                    edEmail.setError("invalid email");
                } else {
                    edEmail.setError(null);
                    isValidEmail = true;
                }
            } else {
                isValidEmail = true;
            }

            if (strAddress.isEmpty()) {
                edAddress.setError("required");
            } else {
                edAddress.setError(null);
                isValidAddress = true;
            }

           /* if (strTime.isEmpty()) {
                edTime.setError("required");
            } else {
                edTime.setError(null);
                isValidTime = true;
            }*/

            if (strPass.isEmpty()) {
                edPassword.setError("required");
            } else {
                edPassword.setError(null);
                isValidPass = true;
            }

            if (strConfirmPass.isEmpty()) {
                edConfirmPassword.setError("required");
            } else if (!strPass.equals(strConfirmPass)) {
                edConfirmPassword.setError("password not matched");
            } else {
                edConfirmPassword.setError(null);
                isValidConfirmPass = true;
            }

            if (isValidName && isValidMobile && isValidDOB && isValidEmail && isValidAddress && isValidPass && isValidConfirmPass) {

                randomNumber = random(6);

                sendOTP(strMobile, randomNumber);

            }


        } else if (view.getId() == R.id.btnSubmit) {

            String strOTP = edOTP.getText().toString();

            if (strOTP.isEmpty()) {
                edOTP.setError("required");
            } else if (!strOTP.equalsIgnoreCase(randomNumber)) {
                edOTP.setError("OTP not matched");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                Cust customer = new Cust(0, strName, strMobile, strPass, strDOB, strEmail, sdf.format(System.currentTimeMillis()), 1, strAddress, strTime);

                saveCustomer(customer);
            }


        } else if (view.getId() == R.id.edDOB) {
            int yr, mn, dy;
            if (dateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(dateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = 1975;
                mn = 0;
                dy = 1;
            }

            DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    yyyy = year;
                    mm = month + 1;
                    dd = dayOfMonth;
                    edDOB.setText(dd + "-" + mm + "-" + yyyy);
                    tvDOB.setText(yyyy + "-" + mm + "-" + dd);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(yyyy, mm - 1, dd);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.HOUR, 0);
                    dateMillis = calendar.getTimeInMillis();
                }
            };

            DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme, dateListener, yr, mn, dy);
            dialog.show();
        } else if (view.getId() == R.id.tvLogin) {
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            finish();
        } else if (view.getId() == R.id.edTime) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(RegistrationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    edTime.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }


    }


    public void saveCustomer(Cust customer) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Cust> listCall = Constants.myInterface.saveCustomer(customer);
            listCall.enqueue(new Callback<Cust>() {
                @Override
                public void onResponse(Call<Cust> call, Response<Cust> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("User Data : ", "------------" + response.body());

                            Cust data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, ""+data.getResp(), Toast.LENGTH_SHORT).show();
                            } else {

                                if (data.getCustId() != 0) {

                                    Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                    finish();

                                } else {

                                    if (data.getResp().equalsIgnoreCase("already exist")){
                                        Toast.makeText(RegistrationActivity.this, "This user already exists", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(RegistrationActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                                    }

                                    commonDialog.dismiss();

                                }
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Cust> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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

            String message = "Thank you for registering at EziRTO app your OTP number is " + OTPNumber + " , Please do not share this OTP.";

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
                    Toast.makeText(RegistrationActivity.this, "Unable to send OTP, please try again", Toast.LENGTH_SHORT).show();
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
        // onBackPressed();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();

    }
}
