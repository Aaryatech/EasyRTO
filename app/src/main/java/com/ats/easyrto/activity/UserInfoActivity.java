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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.fcm.SharedPrefManager;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.util.CommonDialog;
import com.ats.easyrto.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edName, edMobile, edDOB, edEmail, edAddress, edTime;
    private Button btnUpdate;
    private TextView tvDOB, tvTime;

    Cust customer;

    int yyyy, mm, dd;
    long dateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color='#000000'>Info</font>"));

        edName = findViewById(R.id.edName);
        edMobile = findViewById(R.id.edMobile);
        edDOB = findViewById(R.id.edDOB);
        edEmail = findViewById(R.id.edEmail);
        edAddress = findViewById(R.id.edAddress);
        edTime = findViewById(R.id.edTime);

        tvDOB = findViewById(R.id.tvDOB);
        tvTime = findViewById(R.id.tvTime);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        edDOB.setOnClickListener(this);
        edTime.setOnClickListener(this);

        String customerStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_CUSTOMER);
        Gson gson = new Gson();
        customer = gson.fromJson(customerStr, Cust.class);
        Log.e("Customer Bean : ", "---------------" + customer);

        if (customer != null) {

            edName.setText("" + customer.getCustName());
            edMobile.setText("" + customer.getCustMobile());
            edDOB.setText("" + customer.getCustDob());
            tvDOB.setText("" + customer.getCustDob());
            edEmail.setText("" + customer.getCustEmail());
            edAddress.setText("" + customer.getAddPincode());
            edTime.setText("" + customer.getSutTime());

        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edDOB) {
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

        } else if (view.getId() == R.id.edTime) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(UserInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    edTime.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, false);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        } else if (view.getId() == R.id.btnUpdate) {

            String strName, strMobile, strEmail, strDOB, strTime, strAddress;

            strName = edName.getText().toString();
            strMobile = edMobile.getText().toString();
            strEmail = edEmail.getText().toString();
            strDOB = tvDOB.getText().toString();
            strAddress = edAddress.getText().toString();
            strTime = edTime.getText().toString();

            boolean isValidName = false, isValidEmail = false, isValidDOB = false, isValidAdd = false, isValidTime = false;

            if (strName.isEmpty()) {
                edName.setError("required");
            } else {
                edName.setError(null);
                isValidName = true;
            }

            if (strEmail.isEmpty()) {
                edEmail.setError("required");
            } else if (!isValidEmailAddress(strEmail)) {
                edEmail.setError("invalid email address");
            } else {
                edEmail.setError(null);
                isValidEmail = true;
            }

            if (strDOB.isEmpty()) {
                edDOB.setError("required");
            } else {
                edDOB.setError(null);
                isValidDOB = true;
            }

            if (strAddress.isEmpty()) {
                edAddress.setError("required");
            } else {
                edAddress.setError(null);
                isValidAdd = true;
            }

            if (strTime.isEmpty()) {
                edTime.setError("required");
            } else {
                edTime.setError(null);
                isValidTime = true;
            }

            if (isValidName && isValidEmail && isValidDOB && isValidAdd && isValidTime) {

                String token = SharedPrefManager.getmInstance(UserInfoActivity.this).getDeviceToken();

                Cust cust = new Cust(customer.getCustId(), strName, strMobile, customer.getCustPassword(), strDOB, strEmail, customer.getLastUpdateTime(), customer.getIsUsed(), token, customer.getExStr2(), customer.getExInt1(), customer.getExInt2(), customer.getDate1(), customer.getDate2(), strAddress, strTime, customer.getResp());
                updateCustomer(cust);
            }


        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void updateCustomer(Cust customer) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Cust> listCall = Constants.myInterface.updateCustomer(customer);
            listCall.enqueue(new Callback<Cust>() {
                @Override
                public void onResponse(Call<Cust> call, Response<Cust> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("User Data : ", "------------" + response.body());

                            Cust data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(UserInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            } else {

                                if (data.getCustId() != 0) {

                                    Toast.makeText(UserInfoActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                    Gson gson = new Gson();
                                    String json = gson.toJson(data);
                                    CustomSharedPreference.putString(UserInfoActivity.this, CustomSharedPreference.KEY_CUSTOMER, json);

                                    startActivity(new Intent(UserInfoActivity.this, HomeActivity.class));
                                    finish();


                                } else {
                                    Toast.makeText(UserInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                }
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(UserInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(UserInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Cust> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(UserInfoActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
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
