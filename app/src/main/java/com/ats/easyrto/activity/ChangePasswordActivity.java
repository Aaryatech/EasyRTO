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
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.Info;
import com.ats.easyrto.model.WorkHeader;
import com.ats.easyrto.util.CommonDialog;
import com.ats.easyrto.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edOldPass, edNewPass, edConfirmPass;
    private Button btnSubmit;

    Cust customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        setTitle(Html.fromHtml("<font color='#000000'>Change Password</font>"));


        edOldPass = findViewById(R.id.edOldPass);
        edNewPass = findViewById(R.id.edNewPass);
        edConfirmPass = findViewById(R.id.edConfirmPass);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        String customerStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_CUSTOMER);
        Gson gson = new Gson();
        customer = gson.fromJson(customerStr, Cust.class);
        Log.e("Customer Bean : ", "---------------" + customer);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            String strOldPass, strNewPass, strConfirmPass;
            boolean isValidOldPass = false, isValidNewPass = false, isValidConfirmPass = false;

            strOldPass = edOldPass.getText().toString().trim();
            strNewPass = edNewPass.getText().toString().trim();
            strConfirmPass = edConfirmPass.getText().toString().trim();

            if (strOldPass.isEmpty()) {
                edOldPass.setError("required");
            } else {
                edOldPass.setError(null);
                isValidOldPass = true;
            }


            if (strNewPass.isEmpty()) {
                edNewPass.setError("required");
            } else {
                edNewPass.setError(null);
                isValidNewPass = true;
            }

            if (strConfirmPass.isEmpty()) {
                edConfirmPass.setError("required");
            } else if (!strConfirmPass.equals(strNewPass)) {
                edConfirmPass.setError("password not matched");
            } else {
                edConfirmPass.setError(null);
                isValidConfirmPass = true;
            }

            if (isValidOldPass && isValidNewPass && isValidConfirmPass) {
                changePassword(customer.getCustId(), strOldPass, strNewPass);
            }

        }
    }


    public void changePassword(int custId, String oldPass, String newPass) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.changePassword(custId, oldPass, newPass);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("CHANGE PASSWORD : ", " - " + response.body());

                            Info info = response.body();
                            if (!info.getError()) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this, R.style.AlertDialogTheme);
                                builder.setMessage("Password changed successfully");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this, R.style.AlertDialogTheme);
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
                            Toast.makeText(ChangePasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(ChangePasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
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
