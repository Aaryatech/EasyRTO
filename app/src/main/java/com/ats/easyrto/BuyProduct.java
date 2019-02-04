package com.ats.easyrto;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ebs.android.sdk.Config.Encryption;
import com.ebs.android.sdk.Config.Mode;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;

public class BuyProduct extends Activity implements OnClickListener {

    //9920653119

    //04466633333- technical

    Button btnPayment;
    TextView tvTask, tvAmount;

    private static String HOST_NAME = "ebs";

    private static final int ACC_ID = 29277; // Provided by EBS
    private static final String SECRET_KEY = "8c82781b2c94a9b34b815a8e9ba5d2d3";

    double totalAmount, workCost, amtDone, amtPending;
    String taskName;
    int workId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_buy_product);

        HOST_NAME = getResources().getString(R.string.hostname);
        initview();
    }

    protected void initview() {
        btnPayment = findViewById(R.id.btnPayment);
        tvTask = findViewById(R.id.tvTask);
        tvAmount = findViewById(R.id.tvAmount);

        btnPayment.setOnClickListener(this);

        taskName = getIntent().getStringExtra("task");
        totalAmount = getIntent().getFloatExtra("amount", 0);
        workId = getIntent().getIntExtra("id", 0);
        workCost = getIntent().getFloatExtra("workCost", 0);
        amtDone = getIntent().getFloatExtra("done", 0);
        amtPending = getIntent().getFloatExtra("pending", 0);

        Log.e("WORK COST : ", " --------- " + workCost);
        Log.e("AMT DONE : ", " --------- " + amtDone);
        Log.e("AMT PENDING : ", " --------- " + amtPending);

        tvTask.setText("" + taskName);
        tvAmount.setText("Amount : " + totalAmount + "/-");
        //Toast.makeText(this, "Work Id : " + workId, Toast.LENGTH_SHORT).show();

    }


    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPayment) {
            callEbsKit(this);
        }
    }

    private void callEbsKit(BuyProduct buyProduct) {

        PaymentRequest.getInstance().setTransactionAmount(
                String.format("%.2f", totalAmount));

        /** Mandatory */

        PaymentRequest.getInstance().setAccountId(ACC_ID);
        PaymentRequest.getInstance().setSecureKey(SECRET_KEY);

        // Reference No
        PaymentRequest.getInstance().setReferenceNo("223");
        /** Mandatory */

        // Email Id
        //PaymentRequest.getInstance().setBillingEmail("test_tag@testmail.com");

        PaymentRequest.getInstance().setBillingEmail("test@testmail.com");
        /** Mandatory */


        PaymentRequest.getInstance().setFailureid("1");

        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));
        // System.out.println("FAILURE MESSAGE"+getResources().getString(R.string.payment_failure_message));

        /** Mandatory */

        // Currency
        PaymentRequest.getInstance().setCurrency("INR");
        /** Mandatory */

        /** Optional */
        // Your Reference No or Order Id for this transaction
        PaymentRequest.getInstance().setTransactionDescription(
                "Transaction");

        /** Billing Details */
        PaymentRequest.getInstance().setBillingName("Test_Name");
        /** Optional */
        PaymentRequest.getInstance().setBillingAddress("North Mada Street");
        /** Optional */
        PaymentRequest.getInstance().setBillingCity("Chennai");
        /** Optional */
        PaymentRequest.getInstance().setBillingPostalCode("600019");
        /** Optional */
        PaymentRequest.getInstance().setBillingState("Tamilnadu");
        /** Optional */
        PaymentRequest.getInstance().setBillingCountry("IND");
        /** Optional */
        PaymentRequest.getInstance().setBillingPhone("01234567890");
        /** Optional */


        /** Shipping Details */
        PaymentRequest.getInstance().setShippingName(String.valueOf(workCost));//WorkCost
        /** Optional */
        PaymentRequest.getInstance().setShippingAddress(String.valueOf(amtDone));//Amt_Done
        /** Optional */
        PaymentRequest.getInstance().setShippingCity(String.valueOf(workId));
        /** Optional */
        PaymentRequest.getInstance().setShippingState(String.valueOf(amtPending));//Amt_Pending
        /** Optional */


        PaymentRequest.getInstance().setShippingPostalCode("600019");
        /** Optional */

        PaymentRequest.getInstance().setShippingCountry("IND");
        /** Optional */
//		PaymentRequest.getInstance().setShippingEmail("test@testmail.com");
        /** Optional */
        PaymentRequest.getInstance().setShippingPhone("01234567890");
        /** Optional */
        PaymentRequest.getInstance().setLogEnabled("1");


        /**
         * Payment option configuration
         */

        /** Optional */
        PaymentRequest.getInstance().setHidePaymentOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideCashCardOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideCreditCardOption(true);

        /** Optional */
        PaymentRequest.getInstance().setHideDebitCardOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideNetBankingOption(false);

        /** Optional */
        PaymentRequest.getInstance().setHideStoredCardOption(false);

        /**
         * Initialise parameters for dyanmic values sending from merchant
         */

        //PaymentRequest.getInstance().setCustomPostValues(custom_post_parameters);
        /** Optional-Set dyanamic values */

        // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));

        EBSPayment.getInstance().init(buyProduct, ACC_ID, SECRET_KEY,
                Mode.ENV_LIVE, Encryption.ALGORITHM_MD5, HOST_NAME);

        // finish();


    }


}