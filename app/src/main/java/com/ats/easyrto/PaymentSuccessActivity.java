package com.ats.easyrto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.easyrto.activity.HomeActivity;
import com.ats.easyrto.activity.UserInfoActivity;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.Info;
import com.ats.easyrto.model.UpdateCostModel;
import com.ats.easyrto.model.WorkHeader;
import com.ats.easyrto.util.CommonDialog;
import com.ats.easyrto.util.CustomSharedPreference;
import com.ebs.android.sdk.PaymentActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSuccessActivity extends Activity {
    String payment_id;
    String PaymentId;
    String AccountId;
    String MerchantRefNo;
    String Amount;
    String DateCreated;
    String Description;
    String Mode;
    String IsFlagged;
    String BillingName;
    String BillingAddress;
    String BillingCity;
    String BillingState;
    String BillingPostalCode;
    String BillingCountry;
    String BillingPhone;
    String BillingEmail;
    String DeliveryName;
    String DeliveryAddress;
    String DeliveryCity;
    String DeliveryState;
    String DeliveryPostalCode;
    String DeliveryCountry;
    String DeliveryPhone;
    String PaymentStatus;
    String PaymentMode;
    String SecureHash;
    int workId;

    double workCost, amtDone, amtPending, amount;

    LinearLayout llSuccess, llFailed;
    Button btnOK, btnRetry, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment_success);

        llSuccess = findViewById(R.id.llSuccess);
        llFailed = findViewById(R.id.llFailed);
        btnOK = findViewById(R.id.btnOK);
        btnRetry = findViewById(R.id.btnRetry);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();

        payment_id = intent.getStringExtra("payment_id");
        System.out.println("payment_id" + " " + payment_id);

        getJsonReport();


        btnRetry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
                PaymentSuccessActivity.this.finish();
                startActivity(i);

            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentSuccessActivity.this, HomeActivity.class);
                intent.putExtra("orderStatusActivity", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentSuccessActivity.this, HomeActivity.class);
                intent.putExtra("orderStatusActivity", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });


    }

    private void getJsonReport() {
        String response = payment_id;

        ///Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

        JSONObject jObject;
        try {
            jObject = new JSONObject(response.toString());
            PaymentId = jObject.getString("PaymentId");
            AccountId = jObject.getString("AccountId");
            MerchantRefNo = jObject.getString("MerchantRefNo");
            DateCreated = jObject.getString("DateCreated");
            Description = jObject.getString("Description");
            Mode = jObject.getString("Mode");
            IsFlagged = jObject.getString("IsFlagged");
            BillingName = jObject.getString("BillingName");
            BillingAddress = jObject
                    .getString("BillingAddress");
            BillingCity = jObject.getString("BillingCity");
            BillingState = jObject.getString("BillingState");
            BillingPostalCode = jObject
                    .getString("BillingPostalCode");
            BillingCountry = jObject
                    .getString("BillingCountry");
            BillingPhone = jObject.getString("BillingPhone");
            BillingEmail = jObject.getString("BillingEmail");

            //  DeliveryCity = jObject.getString("DeliveryCity");
            DeliveryPostalCode = jObject
                    .getString("DeliveryPostalCode");
            DeliveryCountry = jObject
                    .getString("DeliveryCountry");
            DeliveryPhone = jObject.getString("DeliveryPhone");
            PaymentStatus = jObject.getString("PaymentStatus");
            PaymentMode = jObject.getString("PaymentMode");
            SecureHash = jObject.getString("SecureHash");

            workId = Integer.parseInt(jObject.getString("DeliveryCity"));
            Log.e("WORK ID : ", " --------------------- " + workId);

            DeliveryAddress = jObject.getString("DeliveryAddress");
            amtDone = Double.parseDouble(DeliveryAddress);

            DeliveryName = jObject.getString("DeliveryName");
            workCost = Double.parseDouble(DeliveryName);

            DeliveryState = jObject.getString("DeliveryState");
            amtPending = Double.parseDouble(DeliveryState);

            Amount = jObject.getString("Amount");
            amount = Double.parseDouble(Amount);

            System.out.println("paymentid_rsp" + PaymentId);
            Toast.makeText(getApplicationContext(), "Transaction Response in Merchant App :: " + PaymentId, Toast.LENGTH_LONG).show();
            if (PaymentStatus.equalsIgnoreCase("failed")) {
                llFailed.setVisibility(View.VISIBLE);
                llSuccess.setVisibility(View.GONE);
            } else {


                int status = 3;

//                if (workCost == amtDone) {
//                    status = 3;
//                } else {
//                    status = 2;
//                }

                UpdateCostModel model = new UpdateCostModel(workId, (float) workCost, status, (int) amtDone, (int) amtPending);

                ArrayList<UpdateCostModel> updateModelList = new ArrayList<>();
                updateModelList.add(model);

                updateWorkCost(updateModelList);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateWorkCost(ArrayList<UpdateCostModel> updateCostModel) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateWorkCost(updateCostModel);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE COST : ", "------------" + response.body());

                            Info data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(PaymentSuccessActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            } else {

                                if (!data.getError()) {

                                    llSuccess.setVisibility(View.VISIBLE);
                                    llFailed.setVisibility(View.GONE);

                                    commonDialog.dismiss();


                                } else {
                                    Toast.makeText(PaymentSuccessActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                                    commonDialog.dismiss();

                                }
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(PaymentSuccessActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(PaymentSuccessActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(PaymentSuccessActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("BACK PRESS","----------------");
    }
}