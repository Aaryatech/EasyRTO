package com.ats.easyrto.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.easyrto.BuildConfig;
import com.ats.easyrto.R;
import com.ats.easyrto.adapter.WorkTypeListAdapter;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.interfaces.InterfaceApi;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.Info;
import com.ats.easyrto.model.TaskDesc;
import com.ats.easyrto.model.WorkDetail;
import com.ats.easyrto.model.WorkHeader;
import com.ats.easyrto.model.WorkTypeModel;
import com.ats.easyrto.util.CommonDialog;
import com.ats.easyrto.util.CustomSharedPreference;
import com.ats.easyrto.util.PermissionsUtil;
import com.ats.easyrto.util.RealPathUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttachmentActivity extends AppCompatActivity implements View.OnClickListener {

    WorkTypeModel workTypeModel;

    private LinearLayout llAadharCard, llOrigLicense, llRCBook, llPUC, llInsurance1, llInsurance2, llAddProof, llBankLetter, llForm17, llBankNOC, llVehicleNo, llVehicleInfo, llFIR;
    private ImageView ivPhoto1, ivPhoto2, ivCamera1, ivCamera2, ivAadharCard, ivOrigLicense, ivRCBook, ivPUC, ivInsurance1, ivInsurance2, ivAddProof, ivBankLetter, ivForm17, ivBankNOC, ivVehicleInfo, ivFIR, ivAadharCardPhoto, ivOrigLicensePhoto, ivRCBookPhoto, ivPUCPhoto, ivInsurance1Photo, ivInsurance2Photo, ivAddProofPhoto, ivBankLetterPhoto, ivForm17Photo, ivBankNOCPhoto, ivVehicleInfoPhoto, ivFIRPhoto;
    private TextView tvPhoto1, tvPhoto2, tvAadharCard, tvOrigLicense, tvRCBook, tvPUC, tvInsurance1, tvInsurance2, tvAddProof, tvBankLetter, tvForm17, tvBankNOC, tvFIR, tvVehicleInfo, tvErrorPhoto1, tvErrorPhoto2, tvErrorAadhar, tvErrorOrigLic, tvErrorRCBook, tvErrorPUC, tvErrorInsc1, tvErrorInsc2, tvErrorAddProof, tvErrorBankLetter, tvErrorForm17, tvErrorBankNOC, tvLabel, tvErrorVehicleInfo, tvErrorFIR;
    private Button btnSubmit;
    private EditText edLicenseNo, edState, edStateId, edNumber, edSeries;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "EasyRTO");
    File f;

    Bitmap myBitmap1 = null, myBitmap2 = null, bitmapAadhar = null, bitmapOrigLicense = null, bitmapRCBook = null, bitmapPUC = null, bitmapInsurance1 = null, bitmapInsurance2 = null, bitmapAddProof = null, bitmapBankLetter = null, bitmapForm17 = null, bitmapBankNOC = null, bitmapFIR = null, bitmapVehicleInfo = null;
    public static String path1, imagePath1 = null, imagePath2 = null, pathAadhar = null, pathOrigLicense = null, pathRCBook = null, pathPUC = null, pathInsurance1 = null, pathInsurance2 = null, pathAddProof = null, pathBankLetter = null, pathForm17 = null, pathBankNOC = null, pathFIR = null, pathVehicleInfo = null;

    TaskDesc taskDesc = new TaskDesc();
    Cust customer;

    String vehicleNo;

    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    String workTypeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color='#000000'>Attachments</font>"));

        edLicenseNo = findViewById(R.id.edLicenseNo);

        tvLabel = findViewById(R.id.tvLabel);

        edState = findViewById(R.id.edState);
        edStateId = findViewById(R.id.edStateId);
        edNumber = findViewById(R.id.edNumber);
        edSeries = findViewById(R.id.edSeries);

        llAadharCard = findViewById(R.id.llAadharCard);
        llOrigLicense = findViewById(R.id.llOrigLicense);
        llRCBook = findViewById(R.id.llRCBook);
        llPUC = findViewById(R.id.llPUC);
        llInsurance1 = findViewById(R.id.llInsurance1);
        llInsurance2 = findViewById(R.id.llInsurance2);
        llAddProof = findViewById(R.id.llAddProof);
        llBankLetter = findViewById(R.id.llBankLetter);
        llForm17 = findViewById(R.id.llForm17);
        llBankNOC = findViewById(R.id.llBankNOC);
        llVehicleNo = findViewById(R.id.llVehicleNo);
        llFIR = findViewById(R.id.llFIR);
        llVehicleInfo = findViewById(R.id.llVehicleInfo);


        ivPhoto1 = findViewById(R.id.ivPhoto1);
        ivPhoto2 = findViewById(R.id.ivPhoto2);
        ivCamera1 = findViewById(R.id.ivCamera1);
        ivCamera2 = findViewById(R.id.ivCamera2);
        ivAadharCard = findViewById(R.id.ivAadharCard);
        ivOrigLicense = findViewById(R.id.ivOrigLicense);
        ivRCBook = findViewById(R.id.ivRCBook);
        ivPUC = findViewById(R.id.ivPUC);
        ivInsurance1 = findViewById(R.id.ivInsurance1);
        ivInsurance2 = findViewById(R.id.ivInsurance2);
        ivAddProof = findViewById(R.id.ivAddProof);
        ivBankLetter = findViewById(R.id.ivBankLetter);
        ivForm17 = findViewById(R.id.ivForm17);
        ivBankNOC = findViewById(R.id.ivBankNOC);
        ivFIR = findViewById(R.id.ivFIR);
        ivVehicleInfo = findViewById(R.id.ivVehicleInfo);

        ivAadharCardPhoto = findViewById(R.id.ivAadharPhoto);
        ivOrigLicensePhoto = findViewById(R.id.ivOrigLicensePhoto);
        ivRCBookPhoto = findViewById(R.id.ivRCBookPhoto);
        ivPUCPhoto = findViewById(R.id.ivPUCPhoto);
        ivInsurance1Photo = findViewById(R.id.ivInsurance1Photo);
        ivInsurance2Photo = findViewById(R.id.ivInsurance2Photo);
        ivAddProofPhoto = findViewById(R.id.ivAddProofPhoto);
        ivBankLetterPhoto = findViewById(R.id.ivBankLetterPhoto);
        ivForm17Photo = findViewById(R.id.ivForm17Photo);
        ivBankNOCPhoto = findViewById(R.id.ivBankNOCPhoto);
        ivFIRPhoto = findViewById(R.id.ivFIRPhoto);
        ivVehicleInfoPhoto = findViewById(R.id.ivVehicleInfoPhoto);

        tvPhoto1 = findViewById(R.id.tvPhoto1);
        tvPhoto2 = findViewById(R.id.tvPhoto2);
        tvAadharCard = findViewById(R.id.tvAadharCard);
        tvOrigLicense = findViewById(R.id.tvOrigLicense);
        tvRCBook = findViewById(R.id.tvRCBook);
        tvPUC = findViewById(R.id.tvPUC);
        tvInsurance1 = findViewById(R.id.tvInsurance1);
        tvInsurance2 = findViewById(R.id.tvInsurance2);
        tvAddProof = findViewById(R.id.tvAddProof);
        tvBankLetter = findViewById(R.id.tvBankLetter);
        tvForm17 = findViewById(R.id.tvForm17);
        tvBankNOC = findViewById(R.id.tvBankNOC);
        tvFIR = findViewById(R.id.tvFIR);
        tvVehicleInfo = findViewById(R.id.tvVehicleInfo);

        tvErrorPhoto1 = findViewById(R.id.tvErrorPhoto1);
        tvErrorPhoto2 = findViewById(R.id.tvErrorPhoto2);
        tvErrorAadhar = findViewById(R.id.tvErrorAadhar);
        tvErrorOrigLic = findViewById(R.id.tvErrorOrigLic);
        tvErrorRCBook = findViewById(R.id.tvErrorRC);
        tvErrorPUC = findViewById(R.id.tvErrorPUC);
        tvErrorInsc1 = findViewById(R.id.tvErrorInsc1);
        tvErrorInsc2 = findViewById(R.id.tvErrorInsc2);
        tvErrorAddProof = findViewById(R.id.tvErrorAddProof);
        tvErrorBankLetter = findViewById(R.id.tvErrorBankLetter);
        tvErrorForm17 = findViewById(R.id.tvErrorForm17);
        tvErrorBankNOC = findViewById(R.id.tvErrorBankNOC);
        tvErrorFIR = findViewById(R.id.tvErrorFIR);
        tvErrorVehicleInfo = findViewById(R.id.tvErrorVehicleInfo);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        ivCamera1.setOnClickListener(this);
        ivCamera2.setOnClickListener(this);
        ivAadharCard.setOnClickListener(this);
        ivOrigLicense.setOnClickListener(this);
        ivRCBook.setOnClickListener(this);
        ivPUC.setOnClickListener(this);
        ivInsurance1.setOnClickListener(this);
        ivInsurance2.setOnClickListener(this);
        ivAddProof.setOnClickListener(this);
        ivBankLetter.setOnClickListener(this);
        ivForm17.setOnClickListener(this);
        ivBankNOC.setOnClickListener(this);
        ivFIR.setOnClickListener(this);
        ivVehicleInfo.setOnClickListener(this);

       /*
        ivPhoto1.setOnClickListener(this);
        ivPhoto2.setOnClickListener(this);
       ivAadharCardPhoto.setOnClickListener(this);
        ivOrigLicensePhoto.setOnClickListener(this);
        ivRCBookPhoto.setOnClickListener(this);
        ivPUCPhoto.setOnClickListener(this);
        ivInsurance1Photo.setOnClickListener(this);
        ivInsurance2Photo.setOnClickListener(this);
        ivAddProofPhoto.setOnClickListener(this);
        ivBankLetterPhoto.setOnClickListener(this);
        ivForm17Photo.setOnClickListener(this);
        ivBankNOCPhoto.setOnClickListener(this);*/

        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }

        createFolder();

        getTaskDescById(1);

        String customerStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_CUSTOMER);
        Gson gsonCust = new Gson();
        customer = gsonCust.fromJson(customerStr, Cust.class);
        Log.e("Customer Bean : ", "---------------" + customer);

        try {

            workTypeStr = getIntent().getStringExtra("json");
            Log.e("JSON STR ", "------------------------ " + workTypeStr);

            Gson gson = new Gson();
            workTypeModel = gson.fromJson(workTypeStr, WorkTypeModel.class);

        } catch (Exception e) {
        }

        savePreviousData();


        if (workTypeModel != null) {

            setTitle(Html.fromHtml("<font color='#000000'>" + workTypeModel.getWorkTypeName() + "</font>"));

            if (workTypeModel.getwType() == 1) {

                edLicenseNo.setVisibility(View.GONE);
                llVehicleNo.setVisibility(View.VISIBLE);
                tvLabel.setText("Vehicle Number");

                llRCBook.setVisibility(View.VISIBLE);
                llInsurance1.setVisibility(View.VISIBLE);
                llInsurance2.setVisibility(View.VISIBLE);
                llPUC.setVisibility(View.VISIBLE);
                llAddProof.setVisibility(View.VISIBLE);

            } else if (workTypeModel.getwType() == 2) {

                edLicenseNo.setVisibility(View.GONE);
                llVehicleNo.setVisibility(View.VISIBLE);
                tvLabel.setText("Vehicle Number");

                llRCBook.setVisibility(View.VISIBLE);
                llInsurance1.setVisibility(View.VISIBLE);
                llInsurance2.setVisibility(View.VISIBLE);
                llPUC.setVisibility(View.VISIBLE);
                llBankNOC.setVisibility(View.VISIBLE);

            } else if (workTypeModel.getwType() == 3) {

                edLicenseNo.setVisibility(View.GONE);
                llVehicleNo.setVisibility(View.VISIBLE);
                tvLabel.setText("Vehicle Number");

                llRCBook.setVisibility(View.VISIBLE);
                llInsurance1.setVisibility(View.VISIBLE);
                llInsurance2.setVisibility(View.VISIBLE);
                llPUC.setVisibility(View.VISIBLE);
                llBankLetter.setVisibility(View.VISIBLE);
                llForm17.setVisibility(View.VISIBLE);

            } else if (workTypeModel.getwType() == 4) {

                edLicenseNo.setVisibility(View.GONE);
                llVehicleNo.setVisibility(View.VISIBLE);
                tvLabel.setText("Vehicle Number");

                llRCBook.setVisibility(View.VISIBLE);
                llInsurance1.setVisibility(View.VISIBLE);
                llInsurance2.setVisibility(View.VISIBLE);
                llPUC.setVisibility(View.VISIBLE);
                llAddProof.setVisibility(View.VISIBLE);

            } else if (workTypeModel.getwType() == 5) {

                edLicenseNo.setVisibility(View.VISIBLE);
                llVehicleNo.setVisibility(View.GONE);
                tvLabel.setText("License Number");

                llOrigLicense.setVisibility(View.VISIBLE);

            } else if (workTypeModel.getwType() == 6) {

                edLicenseNo.setVisibility(View.GONE);
                llVehicleNo.setVisibility(View.VISIBLE);
                tvLabel.setText("Vehicle Number");

                llFIR.setVisibility(View.VISIBLE);
                llVehicleInfo.setVisibility(View.VISIBLE);
                llInsurance1.setVisibility(View.VISIBLE);
                llInsurance2.setVisibility(View.VISIBLE);
                llPUC.setVisibility(View.VISIBLE);

            }
        }


        edState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (s.length() == 2) {
                        edStateId.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edStateId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (s.length() == 2) {
                        edSeries.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edSeries.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (s.length() == 2) {
                        edNumber.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivCamera1) {
            showCameraDialog("Photo1");

        } else if (view.getId() == R.id.ivCamera2) {
            showCameraDialog("Photo2");

        } else if (view.getId() == R.id.ivAadharCard) {
            showCameraDialog("Aadhar");

        } else if (view.getId() == R.id.ivOrigLicense) {
            showCameraDialog("OrigLicense");

        } else if (view.getId() == R.id.ivRCBook) {
            showCameraDialog("RCBook");

        } else if (view.getId() == R.id.ivPUC) {
            showCameraDialog("PUC");

        } else if (view.getId() == R.id.ivInsurance1) {
            showCameraDialog("Insurance1");

        } else if (view.getId() == R.id.ivInsurance2) {
            showCameraDialog("Insurance2");

        } else if (view.getId() == R.id.ivAddProof) {
            showCameraDialog("AddProof");

        } else if (view.getId() == R.id.ivBankLetter) {
            showCameraDialog("BankLetter");

        } else if (view.getId() == R.id.ivForm17) {
            showCameraDialog("Form17");

        } else if (view.getId() == R.id.ivFIR) {
            showCameraDialog("FIR");

        } else if (view.getId() == R.id.ivVehicleInfo) {
            showCameraDialog("VehicleInfo");

        } else if (view.getId() == R.id.ivPhoto1) {
            Intent intent = new Intent(AttachmentActivity.this, ImageZoomActivity.class);
            intent.putExtra("image", imagePath1);
            startActivity(intent);

        } else if (view.getId() == R.id.ivPhoto2) {

        } else if (view.getId() == R.id.ivAadharPhoto) {

        } else if (view.getId() == R.id.ivOrigLicensePhoto) {

        } else if (view.getId() == R.id.ivRCBookPhoto) {

        } else if (view.getId() == R.id.ivPUCPhoto) {

        } else if (view.getId() == R.id.ivInsurance1Photo) {

        } else if (view.getId() == R.id.ivInsurance2Photo) {

        } else if (view.getId() == R.id.ivAddProofPhoto) {

        } else if (view.getId() == R.id.ivBankLetterPhoto) {

        } else if (view.getId() == R.id.ivForm17Photo) {

        } else if (view.getId() == R.id.ivBankNOCPhoto) {

        } else if (view.getId() == R.id.btnSubmit) {

            vehicleNo = edLicenseNo.getText().toString().trim();

            String strState = edState.getText().toString();
            String strStateId = edStateId.getText().toString();
            String strSeries = edSeries.getText().toString();
            String strNumber = edNumber.getText().toString();

            if (workTypeModel.getwType() == 1) {

                if (strState.isEmpty()) {
                    edState.setError("required");
                } else if (strStateId.isEmpty()) {
                    edState.setError(null);
                    edStateId.setError("required");
                }
//                else if (strSeries.isEmpty()) {
//                    edStateId.setError(null);
//                    edSeries.setError("required");
//                }
                else if (strNumber.isEmpty()) {
                    edSeries.setError(null);
                    edNumber.setError("required");
                }

              /*  if (vehicleNo.isEmpty()) {
                    edLicenseNo.setError("required");
                }*/

                else if (pathRCBook == null) {
                    edNumber.setError(null);
                    edLicenseNo.setError(null);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.VISIBLE);

                }

                //-----------------------------------Change------------------------------

//                else if (imagePath1 == null) {
//                    edLicenseNo.setError(null);
//                    //edNumber.setError(null);
//                    tvErrorRCBook.setVisibility(View.GONE);
//                    tvErrorPhoto1.setVisibility(View.VISIBLE);
//
//                } else if (imagePath2 == null) {
//                    tvErrorPhoto1.setVisibility(View.GONE);
//                    tvErrorPhoto2.setVisibility(View.VISIBLE);
//
//                } else if (pathAadhar == null) {
//                    tvErrorPhoto2.setVisibility(View.GONE);
//                    tvErrorAadhar.setVisibility(View.VISIBLE);
//
//                    tvErrorAadhar.requestFocus();
//                }
//
//                else if (pathInsurance1 == null) {
//                    tvErrorAadhar.setVisibility(View.GONE);
//                    tvErrorInsc1.setVisibility(View.VISIBLE);
//
//                } else if (pathInsurance2 == null) {
//                    tvErrorInsc1.setVisibility(View.GONE);
//                    tvErrorInsc2.setVisibility(View.VISIBLE);
//
//                } else if (pathPUC == null) {
//                    tvErrorInsc2.setVisibility(View.GONE);
//                    tvErrorPUC.setVisibility(View.VISIBLE);
//
//                } else if (pathAddProof == null) {
//                    tvErrorPUC.setVisibility(View.GONE);
//                    tvErrorAddProof.setVisibility(View.VISIBLE);
//                    //-----------------------------------Change------------------------------
//
//                }
                else {
                    edLicenseNo.setError(null);
                    edState.setError(null);
                    edStateId.setError(null);
                    edSeries.setError(null);
                    edNumber.setError(null);

                    tvErrorPhoto1.setVisibility(View.GONE);
                    tvErrorPhoto2.setVisibility(View.GONE);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.GONE);
                    tvErrorInsc1.setVisibility(View.GONE);
                    tvErrorInsc2.setVisibility(View.GONE);
                    tvErrorPUC.setVisibility(View.GONE);
                    tvErrorAddProof.setVisibility(View.GONE);

                    // edLicenseNo.setText("" + strState + " " + strStateId + " " + strSeries + " " + strNumber);
                    vehicleNo = strState + " " + strStateId + " " + strSeries + " " + strNumber;

                    saveData();
                }


            } else if (workTypeModel.getwType() == 2) {

               /* if (vehicleNo.isEmpty()) {
                    edLicenseNo.setError("required");
                }*/


                if (strState.isEmpty()) {
                    edState.setError("required");
                } else if (strStateId.isEmpty()) {
                    edState.setError(null);
                    edStateId.setError("required");
                }
//                else if (strSeries.isEmpty()) {
//                    edStateId.setError(null);
//                    edSeries.setError("required");
//                }
                else if (strNumber.isEmpty()) {
                    edSeries.setError(null);
                    edNumber.setError("required");
                }

                else if (pathRCBook == null) {
                    edLicenseNo.setError(null);
                    edNumber.setError(null);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.VISIBLE);

                }

                //-----------------------------------------Change--------------------------------

//                 else if (imagePath1 == null) {
//                    edNumber.setError(null);
//                    tvErrorRCBook.setVisibility(View.GONE);
//                    tvErrorPhoto1.setVisibility(View.VISIBLE);
//
//                } else if (imagePath2 == null) {
//                    tvErrorPhoto1.setVisibility(View.GONE);
//                    tvErrorPhoto2.setVisibility(View.VISIBLE);
//
//                } else if (pathAadhar == null) {
//                    tvErrorPhoto2.setVisibility(View.GONE);
//                    tvErrorAadhar.setVisibility(View.VISIBLE);
//
//                    tvErrorAadhar.requestFocus();
//                } else if (pathInsurance1 == null) {
//                    tvErrorAadhar.setVisibility(View.GONE);
//                    tvErrorInsc1.setVisibility(View.VISIBLE);
//
//                }
//                else if (pathInsurance2 == null) {
//                    tvErrorInsc1.setVisibility(View.GONE);
//                    tvErrorInsc2.setVisibility(View.VISIBLE);
//
//                } else if (pathPUC == null) {
//                    tvErrorInsc2.setVisibility(View.GONE);
//                    tvErrorPUC.setVisibility(View.VISIBLE);
//
//                } else if (pathBankNOC == null) {
//                    tvErrorPUC.setVisibility(View.GONE);
//                    tvErrorBankNOC.setVisibility(View.VISIBLE);
//
////-----------------------------------------Change--------------------------------
//
//                }
                else {
                    edLicenseNo.setError(null);
                    edState.setError(null);
                    edStateId.setError(null);
                    edSeries.setError(null);
                    edNumber.setError(null);
                    tvErrorPhoto1.setVisibility(View.GONE);
                    tvErrorPhoto2.setVisibility(View.GONE);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.GONE);
                    tvErrorInsc1.setVisibility(View.GONE);
                    tvErrorInsc2.setVisibility(View.GONE);
                    tvErrorPUC.setVisibility(View.GONE);
                    tvErrorBankNOC.setVisibility(View.GONE);

                    vehicleNo = strState + " " + strStateId + " " + strSeries + " " + strNumber;

                    saveData();
                }


            } else if (workTypeModel.getwType() == 3) {

               /* if (vehicleNo.isEmpty()) {
                    edLicenseNo.setError("required");
                }*/

                if (strState.isEmpty()) {
                    edState.setError("required");
                } else if (strStateId.isEmpty()) {
                    edState.setError(null);
                    edStateId.setError("required");
                }
//                else if (strSeries.isEmpty()) {
//                    edStateId.setError(null);
//                    edSeries.setError("required");
//                }
                else if (strNumber.isEmpty()) {
                    edSeries.setError(null);
                    edNumber.setError("required");
                }

                else if (pathRCBook == null) {
                    edLicenseNo.setError(null);
                    edNumber.setError(null);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.VISIBLE);

                }
//---------------------------------change---------------------------------

//                 else if (imagePath1 == null) {
//                    edNumber.setError(null);
//                    tvErrorRCBook.setVisibility(View.GONE);
//                    tvErrorPhoto1.setVisibility(View.VISIBLE);
//
//                } else if (imagePath2 == null) {
//                    tvErrorPhoto1.setVisibility(View.GONE);
//                    tvErrorPhoto2.setVisibility(View.VISIBLE);
//
//                } else if (pathAadhar == null) {
//                    tvErrorPhoto2.setVisibility(View.GONE);
//                    tvErrorAadhar.setVisibility(View.VISIBLE);
//
//                    tvErrorAadhar.requestFocus();
//                }
//
//               else if (pathInsurance1 == null) {
//                    tvErrorAadhar.setVisibility(View.GONE);
//                    tvErrorInsc1.setVisibility(View.VISIBLE);
//
//                } else if (pathInsurance2 == null) {
//                    tvErrorInsc1.setVisibility(View.GONE);
//                    tvErrorInsc2.setVisibility(View.VISIBLE);
//
//                } else if (pathPUC == null) {
//                    tvErrorInsc2.setVisibility(View.GONE);
//                    tvErrorPUC.setVisibility(View.VISIBLE);
//
//                } else if (pathBankLetter == null) {
//                    tvErrorPUC.setVisibility(View.GONE);
//                    tvErrorBankLetter.setVisibility(View.VISIBLE);
//
//                } else if (pathForm17 == null) {
//                    tvErrorBankLetter.setVisibility(View.GONE);
//                    tvErrorForm17.setVisibility(View.VISIBLE);
//
//                }
//---------------------------------change---------------------------------
                else {
                    edLicenseNo.setError(null);
                    edState.setError(null);
                    edStateId.setError(null);
                    edSeries.setError(null);
                    edNumber.setError(null);
                    tvErrorPhoto1.setVisibility(View.GONE);
                    tvErrorPhoto2.setVisibility(View.GONE);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.GONE);
                    tvErrorInsc1.setVisibility(View.GONE);
                    tvErrorInsc2.setVisibility(View.GONE);
                    tvErrorPUC.setVisibility(View.GONE);
                    tvErrorBankLetter.setVisibility(View.GONE);
                    tvErrorForm17.setVisibility(View.GONE);

                    vehicleNo = strState + " " + strStateId + " " + strSeries + " " + strNumber;


                    saveData();
                }

            } else if (workTypeModel.getwType() == 4) {

               /* if (vehicleNo.isEmpty()) {
                    edLicenseNo.setError("required");
                }*/

                if (strState.isEmpty()) {
                    edState.setError("required");
                } else if (strStateId.isEmpty()) {
                    edState.setError(null);
                    edStateId.setError("required");
                }
//                else if (strSeries.isEmpty()) {
//                    edStateId.setError(null);
//                    edSeries.setError("required");
//                }
                else if (strNumber.isEmpty()) {
                    edSeries.setError(null);
                    edNumber.setError("required");
                } else if (pathRCBook == null) {
                    edNumber.setError(null);
                    edLicenseNo.setError(null);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.VISIBLE);

                }

//---------------------------------change---------------------------------
//                else if (imagePath1 == null) {
//                    edNumber.setError(null);
//                    tvErrorRCBook.setVisibility(View.GONE);
//                    tvErrorPhoto1.setVisibility(View.VISIBLE);
//
//                } else if (imagePath2 == null) {
//                    tvErrorPhoto1.setVisibility(View.GONE);
//                    tvErrorPhoto2.setVisibility(View.VISIBLE);
//
//                } else if (pathAadhar == null) {
//                    tvErrorPhoto2.setVisibility(View.GONE);
//                    tvErrorAadhar.setVisibility(View.VISIBLE);
//
//                    tvErrorAadhar.requestFocus();
//                }
//
//               else if (pathInsurance1 == null) {
//                    tvErrorAadhar.setVisibility(View.GONE);
//                    tvErrorInsc1.setVisibility(View.VISIBLE);
//
//                } else if (pathInsurance2 == null) {
//                    tvErrorInsc1.setVisibility(View.GONE);
//                    tvErrorInsc2.setVisibility(View.VISIBLE);
//
//                } else if (pathPUC == null) {
//                    tvErrorInsc2.setVisibility(View.GONE);
//                    tvErrorPUC.setVisibility(View.VISIBLE);
//
//                } else if (pathAddProof == null) {
//                    tvErrorPUC.setVisibility(View.GONE);
//                    tvErrorAddProof.setVisibility(View.VISIBLE);
////---------------------------------change---------------------------------
//                }
               else {
                    edLicenseNo.setError(null);
                    edState.setError(null);
                    edStateId.setError(null);
                    edSeries.setError(null);
                    edNumber.setError(null);
                    tvErrorPhoto1.setVisibility(View.GONE);
                    tvErrorPhoto2.setVisibility(View.GONE);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorRCBook.setVisibility(View.GONE);
                    tvErrorInsc1.setVisibility(View.GONE);
                    tvErrorInsc2.setVisibility(View.GONE);
                    tvErrorPUC.setVisibility(View.GONE);
                    tvErrorAddProof.setVisibility(View.GONE);

                    vehicleNo = strState + " " + strStateId + " " + strSeries + " " + strNumber;


                    saveData();
                }

            } else if (workTypeModel.getwType() == 5) {

                if (vehicleNo.isEmpty()) {
                    edLicenseNo.setError("required");
                }

/*                if (strState.isEmpty()) {
                    edState.setError("required");
                } else if (strStateId.isEmpty()) {
                    edState.setError(null);
                    edStateId.setError("required");
                } else if (strSeries.isEmpty()) {
                    edStateId.setError(null);
                    edSeries.setError("required");
                } else if (strNumber.isEmpty()) {
                    edSeries.setError(null);
                    edNumber.setError("required");
                } */
//---------------------------------change---------------------------------
                else if (pathOrigLicense == null) {
                    edLicenseNo.setError(null);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorOrigLic.setVisibility(View.VISIBLE);
                }

//                else if (imagePath1 == null) {
//                    edNumber.setError(null);
//                    tvErrorOrigLic.setVisibility(View.GONE);
//                    tvErrorPhoto1.setVisibility(View.VISIBLE);
//
//                } else if (imagePath2 == null) {
//                    tvErrorPhoto1.setVisibility(View.GONE);
//                    tvErrorPhoto2.setVisibility(View.VISIBLE);
//
//                } else if (pathAadhar == null) {
//                    tvErrorPhoto2.setVisibility(View.GONE);
//                    tvErrorAadhar.setVisibility(View.VISIBLE);
//
//                }

//---------------------------------change---------------------------------

                else {
                    edLicenseNo.setError(null);
                    edState.setError(null);
                    edStateId.setError(null);
                    edSeries.setError(null);
                    edNumber.setError(null);
                    tvErrorPhoto1.setVisibility(View.GONE);
                    tvErrorPhoto2.setVisibility(View.GONE);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorOrigLic.setVisibility(View.GONE);

                    //  vehicleNo = strState + " " + strStateId + " " + strSeries + " " + strNumber;


                    saveData();
                }

            } else if (workTypeModel.getwType() == 6) {

               /* if (vehicleNo.isEmpty()) {
                    edLicenseNo.setError("required");
                }*/

                if (strState.isEmpty()) {
                    edState.setError("required");
                } else if (strStateId.isEmpty()) {
                    edState.setError(null);
                    edStateId.setError("required");
                }
//                else if (strSeries.isEmpty()) {
//                    edStateId.setError(null);
//                    edSeries.setError("required");
//                }
                else if (strNumber.isEmpty()) {
                    edSeries.setError(null);
                    edNumber.setError("required");
                }


                /*else if (imagePath1 == null) {
                    edNumber.setError(null);
                    tvErrorPhoto1.setVisibility(View.VISIBLE);

                } else if (imagePath2 == null) {
                    tvErrorPhoto1.setVisibility(View.GONE);
                    tvErrorPhoto2.setVisibility(View.VISIBLE);

                } else if (pathAadhar == null) {
                    tvErrorPhoto2.setVisibility(View.GONE);
                    tvErrorAadhar.setVisibility(View.VISIBLE);

                    tvErrorAadhar.requestFocus();
                }
                */

                else if (pathFIR == null) {
                    edNumber.setError(null);
                    edLicenseNo.setError(null);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorFIR.setVisibility(View.VISIBLE);

                }


/*                else if (pathInsurance1 == null) {
                    tvErrorRCBook.setVisibility(View.GONE);
                    tvErrorInsc1.setVisibility(View.VISIBLE);

                } else if (pathInsurance2 == null) {
                    tvErrorInsc1.setVisibility(View.GONE);
                    tvErrorInsc2.setVisibility(View.VISIBLE);

                } else if (pathPUC == null) {
                    tvErrorInsc2.setVisibility(View.GONE);
                    tvErrorPUC.setVisibility(View.VISIBLE);

                } else if (pathAddProof == null) {
                    tvErrorPUC.setVisibility(View.GONE);
                    tvErrorAddProof.setVisibility(View.VISIBLE);

                }
                */

                else {
                    edLicenseNo.setError(null);
                    edState.setError(null);
                    edStateId.setError(null);
                    edSeries.setError(null);
                    edNumber.setError(null);
                    tvErrorPhoto1.setVisibility(View.GONE);
                    tvErrorPhoto2.setVisibility(View.GONE);
                    tvErrorAadhar.setVisibility(View.GONE);
                    tvErrorFIR.setVisibility(View.GONE);
                    tvErrorVehicleInfo.setVisibility(View.GONE);
                    tvErrorInsc1.setVisibility(View.GONE);
                    tvErrorInsc2.setVisibility(View.GONE);
                    tvErrorPUC.setVisibility(View.GONE);

                    vehicleNo = strState + " " + strStateId + " " + strSeries + " " + strNumber;


                    saveData();
                }

            }

        }
    }

    public void saveData() {

        ArrayList<String> pathArray = new ArrayList<>();
        ArrayList<String> fileNameArray = new ArrayList<>();

        String photo1 = "", photo2 = "", aadhar = "", rc = "", insc1 = "", insc2 = "", puc = "", addProof = "", bankNOC = "", bankLetter = "", form17 = "", origLic = "", fir = "", vehInfo = "";

        if (workTypeModel.getwType() == 1) {

            pathArray.clear();
            fileNameArray.clear();

            if (imagePath1 != null) {

                pathArray.add(imagePath1);

                File imgFile1 = new File(imagePath1);
                int pos = imgFile1.getName().lastIndexOf(".");
                String ext = imgFile1.getName().substring(pos + 1);
                photo1 = System.currentTimeMillis() + "_p1." + ext;
                fileNameArray.add(photo1);
            }

            if (imagePath2 != null) {

                pathArray.add(imagePath2);

                File imgFile2 = new File(imagePath2);
                int pos2 = imgFile2.getName().lastIndexOf(".");
                String ext2 = imgFile2.getName().substring(pos2 + 1);
                photo2 = System.currentTimeMillis() + "_p2." + ext2;
                fileNameArray.add(photo2);

            }

            if (pathAadhar != null) {

                pathArray.add(pathAadhar);

                File imgFileAadhar = new File(pathAadhar);
                int pos3 = imgFileAadhar.getName().lastIndexOf(".");
                String ext3 = imgFileAadhar.getName().substring(pos3 + 1);
                aadhar = System.currentTimeMillis() + "_ac." + ext3;
                fileNameArray.add(aadhar);

            }

            if (pathRCBook != null) {

                pathArray.add(pathRCBook);

                File imgFileRC = new File(pathRCBook);
                int pos4 = imgFileRC.getName().lastIndexOf(".");
                String ext4 = imgFileRC.getName().substring(pos4 + 1);
                rc = System.currentTimeMillis() + "_rc." + ext4;
                fileNameArray.add(rc);

            }

            if (pathInsurance1 != null) {

                pathArray.add(pathInsurance1);

                File imgFileInsc1 = new File(pathInsurance1);
                int pos5 = imgFileInsc1.getName().lastIndexOf(".");
                String ext5 = imgFileInsc1.getName().substring(pos5 + 1);
                insc1 = System.currentTimeMillis() + "_insc1." + ext5;
                fileNameArray.add(insc1);

            }

            if (pathInsurance2 != null) {

                pathArray.add(pathInsurance2);

                File imgFileInsc2 = new File(pathInsurance2);
                int pos6 = imgFileInsc2.getName().lastIndexOf(".");
                String ext6 = imgFileInsc2.getName().substring(pos6 + 1);
                insc2 = System.currentTimeMillis() + "_insc2." + ext6;
                fileNameArray.add(insc2);

            }

            if (pathPUC != null) {

                pathArray.add(pathPUC);

                File imgFilePUC = new File(pathPUC);
                int pos7 = imgFilePUC.getName().lastIndexOf(".");
                String ext7 = imgFilePUC.getName().substring(pos7 + 1);
                puc = System.currentTimeMillis() + "_puc." + ext7;
                fileNameArray.add(puc);

            }

            if (pathAddProof != null) {

                pathArray.add(pathAddProof);

                File imgFileAddProof = new File(pathAddProof);
                int pos8 = imgFileAddProof.getName().lastIndexOf(".");
                String ext8 = imgFileAddProof.getName().substring(pos8 + 1);
                addProof = System.currentTimeMillis() + "_addProof." + ext8;
                fileNameArray.add(addProof);

            }

            WorkDetail detail = new WorkDetail(0, taskDesc.getTaskDesc(), 1, 1, sdfDate.format(System.currentTimeMillis()), sdfDateTime.format(System.currentTimeMillis()), 1);
            WorkHeader header = new WorkHeader(workTypeModel.getWorkTypeId(), 0, vehicleNo, "", insc1, insc2, rc, puc, sdfDateTime.format(System.currentTimeMillis()), 1, bankLetter, bankNOC, origLic, aadhar, photo1, photo2, addProof, 1, 0, form17, sdfDate.format(System.currentTimeMillis()), Arrays.asList(detail));

            if (customer == null) {

                Gson gsonHeader = new Gson();
                String jsonHeader = gsonHeader.toJson(header);

                Gson gsonPath = new Gson();
                String jsonPath = gsonPath.toJson(pathArray);

                Gson gsonFile = new Gson();
                String jsonFile = gsonFile.toJson(fileNameArray);


                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_HEADER, jsonHeader);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_PATH_ARRAY, jsonPath);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_FILE_NAME_ARRAY, jsonFile);
                CustomSharedPreference.putInt(this, CustomSharedPreference.KEY_TYPE, 1);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_WORK_TYPE, workTypeStr);

                Intent intent = new Intent(AttachmentActivity.this, LoginAndRegistrationActivity.class);
                startActivity(intent);
                finish();

            } else {

                header.setCustId(customer.getCustId());
                sendImage(pathArray, fileNameArray, header);
            }

        } else if (workTypeModel.getwType() == 2) {

            pathArray.clear();
            fileNameArray.clear();

            if (imagePath1 != null) {

                pathArray.add(imagePath1);

                File imgFile1 = new File(imagePath1);
                int pos = imgFile1.getName().lastIndexOf(".");
                String ext = imgFile1.getName().substring(pos + 1);
                photo1 = System.currentTimeMillis() + "_p1." + ext;
                fileNameArray.add(photo1);
            }

            if (imagePath2 != null) {

                pathArray.add(imagePath2);

                File imgFile2 = new File(imagePath2);
                int pos2 = imgFile2.getName().lastIndexOf(".");
                String ext2 = imgFile2.getName().substring(pos2 + 1);
                photo2 = System.currentTimeMillis() + "_p2." + ext2;
                fileNameArray.add(photo2);

            }

            if (pathAadhar != null) {

                pathArray.add(pathAadhar);

                File imgFileAadhar = new File(pathAadhar);
                int pos3 = imgFileAadhar.getName().lastIndexOf(".");
                String ext3 = imgFileAadhar.getName().substring(pos3 + 1);
                aadhar = System.currentTimeMillis() + "_ac." + ext3;
                fileNameArray.add(aadhar);

            }

            if (pathRCBook != null) {

                pathArray.add(pathRCBook);

                File imgFileRC = new File(pathRCBook);
                int pos4 = imgFileRC.getName().lastIndexOf(".");
                String ext4 = imgFileRC.getName().substring(pos4 + 1);
                rc = System.currentTimeMillis() + "_rc." + ext4;
                fileNameArray.add(rc);

            }

            if (pathInsurance1 != null) {

                pathArray.add(pathInsurance1);

                File imgFileInsc1 = new File(pathInsurance1);
                int pos5 = imgFileInsc1.getName().lastIndexOf(".");
                String ext5 = imgFileInsc1.getName().substring(pos5 + 1);
                insc1 = System.currentTimeMillis() + "_insc1." + ext5;
                fileNameArray.add(insc1);

            }

            if (pathInsurance2 != null) {

                pathArray.add(pathInsurance2);

                File imgFileInsc2 = new File(pathInsurance2);
                int pos6 = imgFileInsc2.getName().lastIndexOf(".");
                String ext6 = imgFileInsc2.getName().substring(pos6 + 1);
                insc2 = System.currentTimeMillis() + "_insc2." + ext6;
                fileNameArray.add(insc2);

            }

            if (pathPUC != null) {

                pathArray.add(pathPUC);

                File imgFilePUC = new File(pathPUC);
                int pos7 = imgFilePUC.getName().lastIndexOf(".");
                String ext7 = imgFilePUC.getName().substring(pos7 + 1);
                puc = System.currentTimeMillis() + "_puc." + ext7;
                fileNameArray.add(puc);

            }

            if (pathBankNOC != null) {

                pathArray.add(pathBankNOC);

                File imgFileNOC = new File(pathBankNOC);
                int pos8 = imgFileNOC.getName().lastIndexOf(".");
                String ext8 = imgFileNOC.getName().substring(pos8 + 1);
                bankNOC = System.currentTimeMillis() + "_bNOC." + ext8;
                fileNameArray.add(bankNOC);

            }

            WorkDetail detail = new WorkDetail(0, taskDesc.getTaskDesc(), 1, 1, sdfDate.format(System.currentTimeMillis()), sdfDateTime.format(System.currentTimeMillis()), 1);
            WorkHeader header = new WorkHeader(workTypeModel.getWorkTypeId(), 0, vehicleNo, "", insc1, insc2, rc, puc, sdfDateTime.format(System.currentTimeMillis()), 1, bankLetter, bankNOC, origLic, aadhar, photo1, photo2, addProof, 1, 0, form17, sdfDate.format(System.currentTimeMillis()), Arrays.asList(detail));

            if (customer == null) {

                Gson gsonHeader = new Gson();
                String jsonHeader = gsonHeader.toJson(header);

                Gson gsonPath = new Gson();
                String jsonPath = gsonPath.toJson(pathArray);

                Gson gsonFile = new Gson();
                String jsonFile = gsonFile.toJson(fileNameArray);

                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_HEADER, jsonHeader);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_PATH_ARRAY, jsonPath);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_FILE_NAME_ARRAY, jsonFile);
                CustomSharedPreference.putInt(this, CustomSharedPreference.KEY_TYPE, 1);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_WORK_TYPE, workTypeStr);

                Intent intent = new Intent(AttachmentActivity.this, LoginAndRegistrationActivity.class);
                startActivity(intent);
                finish();

            } else {

                header.setCustId(customer.getCustId());
                sendImage(pathArray, fileNameArray, header);
            }

        } else if (workTypeModel.getwType() == 3) {

            pathArray.clear();
            fileNameArray.clear();

            if (imagePath1 != null) {

                pathArray.add(imagePath1);

                File imgFile1 = new File(imagePath1);
                int pos = imgFile1.getName().lastIndexOf(".");
                String ext = imgFile1.getName().substring(pos + 1);
                photo1 = System.currentTimeMillis() + "_p1." + ext;
                fileNameArray.add(photo1);
            }

            if (imagePath2 != null) {

                pathArray.add(imagePath2);

                File imgFile2 = new File(imagePath2);
                int pos2 = imgFile2.getName().lastIndexOf(".");
                String ext2 = imgFile2.getName().substring(pos2 + 1);
                photo2 = System.currentTimeMillis() + "_p2." + ext2;
                fileNameArray.add(photo2);

            }

            if (pathAadhar != null) {

                pathArray.add(pathAadhar);

                File imgFileAadhar = new File(pathAadhar);
                int pos3 = imgFileAadhar.getName().lastIndexOf(".");
                String ext3 = imgFileAadhar.getName().substring(pos3 + 1);
                aadhar = System.currentTimeMillis() + "_ac." + ext3;
                fileNameArray.add(aadhar);

            }

            if (pathRCBook != null) {

                pathArray.add(pathRCBook);

                File imgFileRC = new File(pathRCBook);
                int pos4 = imgFileRC.getName().lastIndexOf(".");
                String ext4 = imgFileRC.getName().substring(pos4 + 1);
                rc = System.currentTimeMillis() + "_rc." + ext4;
                fileNameArray.add(rc);

            }

            if (pathInsurance1 != null) {

                pathArray.add(pathInsurance1);

                File imgFileInsc1 = new File(pathInsurance1);
                int pos5 = imgFileInsc1.getName().lastIndexOf(".");
                String ext5 = imgFileInsc1.getName().substring(pos5 + 1);
                insc1 = System.currentTimeMillis() + "_insc1." + ext5;
                fileNameArray.add(insc1);

            }

            if (pathInsurance2 != null) {

                pathArray.add(pathInsurance2);

                File imgFileInsc2 = new File(pathInsurance2);
                int pos6 = imgFileInsc2.getName().lastIndexOf(".");
                String ext6 = imgFileInsc2.getName().substring(pos6 + 1);
                insc2 = System.currentTimeMillis() + "_insc2." + ext6;
                fileNameArray.add(insc2);

            }

            if (pathPUC != null) {

                pathArray.add(pathPUC);

                File imgFilePUC = new File(pathPUC);
                int pos7 = imgFilePUC.getName().lastIndexOf(".");
                String ext7 = imgFilePUC.getName().substring(pos7 + 1);
                puc = System.currentTimeMillis() + "_puc." + ext7;
                fileNameArray.add(puc);

            }

            if (pathBankLetter != null) {

                pathArray.add(pathBankLetter);

                File imgFileBankLetter = new File(pathBankLetter);
                int pos8 = imgFileBankLetter.getName().lastIndexOf(".");
                String ext8 = imgFileBankLetter.getName().substring(pos8 + 1);
                bankLetter = System.currentTimeMillis() + "_bLetter." + ext8;
                fileNameArray.add(bankLetter);

            }

            if (pathForm17 != null) {

                pathArray.add(pathForm17);

                File imgFileForm17 = new File(pathForm17);
                int pos9 = imgFileForm17.getName().lastIndexOf(".");
                String ext9 = imgFileForm17.getName().substring(pos9 + 1);
                form17 = System.currentTimeMillis() + "_f17." + ext9;
                fileNameArray.add(form17);

            }


            WorkDetail detail = new WorkDetail(0, taskDesc.getTaskDesc(), 1, 1, sdfDate.format(System.currentTimeMillis()), sdfDateTime.format(System.currentTimeMillis()), 1);
            WorkHeader header = new WorkHeader(workTypeModel.getWorkTypeId(), 0, vehicleNo, "", insc1, insc2, rc, puc, sdfDateTime.format(System.currentTimeMillis()), 1, bankLetter, bankNOC, origLic, aadhar, photo1, photo2, addProof, 1, 0, form17, sdfDate.format(System.currentTimeMillis()), Arrays.asList(detail));

            if (customer == null) {

                Gson gsonHeader = new Gson();
                String jsonHeader = gsonHeader.toJson(header);

                Gson gsonPath = new Gson();
                String jsonPath = gsonPath.toJson(pathArray);

                Gson gsonFile = new Gson();
                String jsonFile = gsonFile.toJson(fileNameArray);

                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_HEADER, jsonHeader);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_PATH_ARRAY, jsonPath);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_FILE_NAME_ARRAY, jsonFile);
                CustomSharedPreference.putInt(this, CustomSharedPreference.KEY_TYPE, 1);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_WORK_TYPE, workTypeStr);

                Intent intent = new Intent(AttachmentActivity.this, LoginAndRegistrationActivity.class);
                startActivity(intent);
                finish();

            } else {

                header.setCustId(customer.getCustId());
                sendImage(pathArray, fileNameArray, header);
            }

        } else if (workTypeModel.getwType() == 4) {

            pathArray.clear();
            fileNameArray.clear();

            if (imagePath1 != null) {

                pathArray.add(imagePath1);

                File imgFile1 = new File(imagePath1);
                int pos = imgFile1.getName().lastIndexOf(".");
                String ext = imgFile1.getName().substring(pos + 1);
                photo1 = System.currentTimeMillis() + "_p1." + ext;
                fileNameArray.add(photo1);
            }

            if (imagePath2 != null) {

                pathArray.add(imagePath2);

                File imgFile2 = new File(imagePath2);
                int pos2 = imgFile2.getName().lastIndexOf(".");
                String ext2 = imgFile2.getName().substring(pos2 + 1);
                photo2 = System.currentTimeMillis() + "_p2." + ext2;
                fileNameArray.add(photo2);

            }

            if (pathAadhar != null) {

                pathArray.add(pathAadhar);

                File imgFileAadhar = new File(pathAadhar);
                int pos3 = imgFileAadhar.getName().lastIndexOf(".");
                String ext3 = imgFileAadhar.getName().substring(pos3 + 1);
                aadhar = System.currentTimeMillis() + "_ac." + ext3;
                fileNameArray.add(aadhar);

            }

            if (pathRCBook != null) {

                pathArray.add(pathRCBook);

                File imgFileRC = new File(pathRCBook);
                int pos4 = imgFileRC.getName().lastIndexOf(".");
                String ext4 = imgFileRC.getName().substring(pos4 + 1);
                rc = System.currentTimeMillis() + "_rc." + ext4;
                fileNameArray.add(rc);

            }

            if (pathInsurance1 != null) {

                pathArray.add(pathInsurance1);

                File imgFileInsc1 = new File(pathInsurance1);
                int pos5 = imgFileInsc1.getName().lastIndexOf(".");
                String ext5 = imgFileInsc1.getName().substring(pos5 + 1);
                insc1 = System.currentTimeMillis() + "_insc1." + ext5;
                fileNameArray.add(insc1);

            }

            if (pathInsurance2 != null) {

                pathArray.add(pathInsurance2);

                File imgFileInsc2 = new File(pathInsurance2);
                int pos6 = imgFileInsc2.getName().lastIndexOf(".");
                String ext6 = imgFileInsc2.getName().substring(pos6 + 1);
                insc2 = System.currentTimeMillis() + "_insc2." + ext6;
                fileNameArray.add(insc2);

            }

            if (pathPUC != null) {

                pathArray.add(pathPUC);

                File imgFilePUC = new File(pathPUC);
                int pos7 = imgFilePUC.getName().lastIndexOf(".");
                String ext7 = imgFilePUC.getName().substring(pos7 + 1);
                puc = System.currentTimeMillis() + "_puc." + ext7;
                fileNameArray.add(puc);

            }

            if (pathAddProof != null) {

                pathArray.add(pathAddProof);

                File imgFileAddProof = new File(pathAddProof);
                int pos8 = imgFileAddProof.getName().lastIndexOf(".");
                String ext8 = imgFileAddProof.getName().substring(pos8 + 1);
                addProof = System.currentTimeMillis() + "_addProof." + ext8;
                fileNameArray.add(addProof);

            }

            WorkDetail detail = new WorkDetail(0, taskDesc.getTaskDesc(), 1, 1, sdfDate.format(System.currentTimeMillis()), sdfDateTime.format(System.currentTimeMillis()), 1);
            WorkHeader header = new WorkHeader(workTypeModel.getWorkTypeId(), 0, vehicleNo, "", insc1, insc2, rc, puc, sdfDateTime.format(System.currentTimeMillis()), 1, bankLetter, bankNOC, origLic, aadhar, photo1, photo2, addProof, 1, 0, form17, sdfDate.format(System.currentTimeMillis()), Arrays.asList(detail));

            if (customer == null) {

                Gson gsonHeader = new Gson();
                String jsonHeader = gsonHeader.toJson(header);

                Gson gsonPath = new Gson();
                String jsonPath = gsonPath.toJson(pathArray);

                Gson gsonFile = new Gson();
                String jsonFile = gsonFile.toJson(fileNameArray);

                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_HEADER, jsonHeader);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_PATH_ARRAY, jsonPath);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_FILE_NAME_ARRAY, jsonFile);
                CustomSharedPreference.putInt(this, CustomSharedPreference.KEY_TYPE, 1);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_WORK_TYPE, workTypeStr);

                Intent intent = new Intent(AttachmentActivity.this, LoginAndRegistrationActivity.class);
                startActivity(intent);
                finish();

            } else {
                header.setCustId(customer.getCustId());
                sendImage(pathArray, fileNameArray, header);
            }

        } else if (workTypeModel.getwType() == 5) {

            pathArray.clear();
            fileNameArray.clear();

            if (imagePath1 != null) {

                pathArray.add(imagePath1);

                File imgFile1 = new File(imagePath1);
                int pos = imgFile1.getName().lastIndexOf(".");
                String ext = imgFile1.getName().substring(pos + 1);
                photo1 = System.currentTimeMillis() + "_p1." + ext;
                fileNameArray.add(photo1);
            }

            if (imagePath2 != null) {

                pathArray.add(imagePath2);

                File imgFile2 = new File(imagePath2);
                int pos2 = imgFile2.getName().lastIndexOf(".");
                String ext2 = imgFile2.getName().substring(pos2 + 1);
                photo2 = System.currentTimeMillis() + "_p2." + ext2;
                fileNameArray.add(photo2);

            }

            if (pathAadhar != null) {

                pathArray.add(pathAadhar);

                File imgFileAadhar = new File(pathAadhar);
                int pos3 = imgFileAadhar.getName().lastIndexOf(".");
                String ext3 = imgFileAadhar.getName().substring(pos3 + 1);
                aadhar = System.currentTimeMillis() + "_ac." + ext3;
                fileNameArray.add(aadhar);

            }

            if (pathOrigLicense != null) {

                pathArray.add(pathOrigLicense);

                File imgFileOrigLic = new File(pathOrigLicense);
                int pos4 = imgFileOrigLic.getName().lastIndexOf(".");
                String ext4 = imgFileOrigLic.getName().substring(pos4 + 1);
                origLic = System.currentTimeMillis() + "_origLic." + ext4;
                fileNameArray.add(origLic);

            }

            WorkDetail detail = new WorkDetail(0, taskDesc.getTaskDesc(), 1, 1, sdfDate.format(System.currentTimeMillis()), sdfDateTime.format(System.currentTimeMillis()), 1);
            WorkHeader header = new WorkHeader(workTypeModel.getWorkTypeId(), 0, vehicleNo, "", insc1, insc2, rc, puc, sdfDateTime.format(System.currentTimeMillis()), 1, bankLetter, bankNOC, origLic, aadhar, photo1, photo2, addProof, 1, 0, form17, sdfDate.format(System.currentTimeMillis()), Arrays.asList(detail));

            if (customer == null) {

                Gson gsonHeader = new Gson();
                String jsonHeader = gsonHeader.toJson(header);

                Gson gsonPath = new Gson();
                String jsonPath = gsonPath.toJson(pathArray);

                Gson gsonFile = new Gson();
                String jsonFile = gsonFile.toJson(fileNameArray);

                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_HEADER, jsonHeader);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_PATH_ARRAY, jsonPath);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_FILE_NAME_ARRAY, jsonFile);
                CustomSharedPreference.putInt(this, CustomSharedPreference.KEY_TYPE, 1);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_WORK_TYPE, workTypeStr);

                Intent intent = new Intent(AttachmentActivity.this, LoginAndRegistrationActivity.class);
                startActivity(intent);
                finish();

            } else {
                header.setCustId(customer.getCustId());
                sendImage(pathArray, fileNameArray, header);
            }

        } else if (workTypeModel.getwType() == 6) {

            pathArray.clear();
            fileNameArray.clear();

            if (imagePath1 != null) {

                pathArray.add(imagePath1);

                File imgFile1 = new File(imagePath1);
                int pos = imgFile1.getName().lastIndexOf(".");
                String ext = imgFile1.getName().substring(pos + 1);
                photo1 = System.currentTimeMillis() + "_p1." + ext;
                fileNameArray.add(photo1);
            }

            if (imagePath2 != null) {

                pathArray.add(imagePath2);

                File imgFile2 = new File(imagePath2);
                int pos2 = imgFile2.getName().lastIndexOf(".");
                String ext2 = imgFile2.getName().substring(pos2 + 1);
                photo2 = System.currentTimeMillis() + "_p2." + ext2;
                fileNameArray.add(photo2);

            }

            if (pathAadhar != null) {

                pathArray.add(pathAadhar);

                File imgFileAadhar = new File(pathAadhar);
                int pos3 = imgFileAadhar.getName().lastIndexOf(".");
                String ext3 = imgFileAadhar.getName().substring(pos3 + 1);
                aadhar = System.currentTimeMillis() + "_ac." + ext3;
                fileNameArray.add(aadhar);

            }

            if (pathFIR != null) {

                pathArray.add(pathFIR);

                File imgFileFIR = new File(pathFIR);
                int pos4 = imgFileFIR.getName().lastIndexOf(".");
                String ext4 = imgFileFIR.getName().substring(pos4 + 1);
                fir = System.currentTimeMillis() + "_fir." + ext4;
                fileNameArray.add(fir);

            }

            if (pathVehicleInfo != null) {

                pathArray.add(pathVehicleInfo);

                File imgFileVehInfo = new File(pathVehicleInfo);
                int pos4 = imgFileVehInfo.getName().lastIndexOf(".");
                String ext4 = imgFileVehInfo.getName().substring(pos4 + 1);
                vehInfo = System.currentTimeMillis() + "_vehInfo." + ext4;
                fileNameArray.add(vehInfo);

            }

            if (pathInsurance1 != null) {

                pathArray.add(pathInsurance1);

                File imgFileInsc1 = new File(pathInsurance1);
                int pos5 = imgFileInsc1.getName().lastIndexOf(".");
                String ext5 = imgFileInsc1.getName().substring(pos5 + 1);
                insc1 = System.currentTimeMillis() + "_insc1." + ext5;
                fileNameArray.add(insc1);

            }

            if (pathInsurance2 != null) {

                pathArray.add(pathInsurance2);

                File imgFileInsc2 = new File(pathInsurance2);
                int pos6 = imgFileInsc2.getName().lastIndexOf(".");
                String ext6 = imgFileInsc2.getName().substring(pos6 + 1);
                insc2 = System.currentTimeMillis() + "_insc2." + ext6;
                fileNameArray.add(insc2);

            }

            if (pathPUC != null) {

                pathArray.add(pathPUC);

                File imgFilePUC = new File(pathPUC);
                int pos7 = imgFilePUC.getName().lastIndexOf(".");
                String ext7 = imgFilePUC.getName().substring(pos7 + 1);
                puc = System.currentTimeMillis() + "_puc." + ext7;
                fileNameArray.add(puc);

            }

            WorkDetail detail = new WorkDetail(0, taskDesc.getTaskDesc(), 1, 1, sdfDate.format(System.currentTimeMillis()), sdfDateTime.format(System.currentTimeMillis()), 1);
            WorkHeader header = new WorkHeader(workTypeModel.getWorkTypeId(), 0, vehicleNo, "", insc1, insc2, rc, puc, sdfDateTime.format(System.currentTimeMillis()), 1, fir, vehInfo, origLic, aadhar, photo1, photo2, addProof, 1, 0, form17, sdfDate.format(System.currentTimeMillis()), Arrays.asList(detail));

            if (customer == null) {

                Gson gsonHeader = new Gson();
                String jsonHeader = gsonHeader.toJson(header);

                Gson gsonPath = new Gson();
                String jsonPath = gsonPath.toJson(pathArray);

                Gson gsonFile = new Gson();
                String jsonFile = gsonFile.toJson(fileNameArray);

                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_HEADER, jsonHeader);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_PATH_ARRAY, jsonPath);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_FILE_NAME_ARRAY, jsonFile);
                CustomSharedPreference.putInt(this, CustomSharedPreference.KEY_TYPE, 1);
                CustomSharedPreference.putString(this, CustomSharedPreference.KEY_WORK_TYPE, workTypeStr);

                Intent intent = new Intent(AttachmentActivity.this, LoginAndRegistrationActivity.class);
                startActivity(intent);
                finish();

            } else {
                header.setCustId(customer.getCustId());
                sendImage(pathArray, fileNameArray, header);
            }

        }


    }


    //--------------------------IMAGE-----------------------------------------

    public void showCameraDialog(final String type) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Choose");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type.equalsIgnoreCase("Photo1")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 101);
                } else if (type.equalsIgnoreCase("Photo2")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 201);
                } else if (type.equalsIgnoreCase("Aadhar")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 301);
                } else if (type.equalsIgnoreCase("OrigLicense")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 401);
                } else if (type.equalsIgnoreCase("RCBook")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 501);
                } else if (type.equalsIgnoreCase("PUC")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 601);
                } else if (type.equalsIgnoreCase("Insurance1")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 701);
                } else if (type.equalsIgnoreCase("Insurance2")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 801);
                } else if (type.equalsIgnoreCase("AddProof")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 901);
                } else if (type.equalsIgnoreCase("BankLetter")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 1001);
                } else if (type.equalsIgnoreCase("Form17")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 1101);
                } else if (type.equalsIgnoreCase("BankNOC")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 1201);
                } else if (type.equalsIgnoreCase("FIR")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 1301);
                } else if (type.equalsIgnoreCase("VehicleInfo")) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 1401);
                }
            }
        });
        builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (type.equalsIgnoreCase("Photo1")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_p1.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 102);
                        } else if (type.equalsIgnoreCase("Photo2")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_p2.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 202);
                        } else if (type.equalsIgnoreCase("Aadhar")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_ac.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 302);
                        } else if (type.equalsIgnoreCase("OrigLicense")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_origLic.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 402);
                        } else if (type.equalsIgnoreCase("RCBook")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_rc.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 502);
                        } else if (type.equalsIgnoreCase("PUC")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_puc.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 602);
                        } else if (type.equalsIgnoreCase("Insurance1")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_insc1.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 702);
                        } else if (type.equalsIgnoreCase("Insurance2")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_insc2.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 802);
                        } else if (type.equalsIgnoreCase("AddProof")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_addProof.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 902);
                        } else if (type.equalsIgnoreCase("BankLetter")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_bLetter.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1002);
                        } else if (type.equalsIgnoreCase("Form17")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_f17.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1102);
                        } else if (type.equalsIgnoreCase("BankNOC")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_bNoc.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1202);
                        } else if (type.equalsIgnoreCase("FIR")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_fir.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1302);
                        } else if (type.equalsIgnoreCase("VehicleInfo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_vehInfo.jpg");
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), authorities, f);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1402);
                        }

                    } else {

                        if (type.equalsIgnoreCase("Photo1")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_p1.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 102);
                        } else if (type.equalsIgnoreCase("Photo2")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_p2.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 202);
                        } else if (type.equalsIgnoreCase("Aadhar")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_ac.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 302);
                        } else if (type.equalsIgnoreCase("OrigLicense")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_origLic.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 402);
                        } else if (type.equalsIgnoreCase("RCBook")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_rc.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 502);
                        } else if (type.equalsIgnoreCase("PUC")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_puc.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 602);
                        } else if (type.equalsIgnoreCase("Insurance1")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_insc1.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 702);
                        } else if (type.equalsIgnoreCase("Insurance2")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_insc2.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 802);
                        } else if (type.equalsIgnoreCase("AddProof")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_addProof.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 902);
                        } else if (type.equalsIgnoreCase("BankLetter")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_bLetter.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1002);
                        } else if (type.equalsIgnoreCase("Form17")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_f17.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1102);
                        } else if (type.equalsIgnoreCase("BankNOC")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_bNOC.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1202);
                        } else if (type.equalsIgnoreCase("FIR")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_fir.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1302);
                        } else if (type.equalsIgnoreCase("VehicleInfo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "" + System.currentTimeMillis() + "_vehInfo.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1402);
                        }

                    }
                } catch (Exception e) {
                    ////Log.e("select camera : ", " Exception : " + e.getMessage());
                }
            }
        });
        builder.show();
    }

    private void showFileChooser() {
        Log.e("ATTACH ACT", " showFileChooser");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {


            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, intent);
            startActivityForResult(chooserIntent, 300);


/*            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    300);*/
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String realPath;
        Bitmap bitmap = null;

        if (resultCode == RESULT_OK && requestCode == 102) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivPhoto1.setImageBitmap(myBitmap1);

                    myBitmap1 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath1 = f.getAbsolutePath();
                tvPhoto1.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap1 = getBitmapFromCameraData(data, this);

                ivPhoto1.setImageBitmap(myBitmap1);
                imagePath1 = uriFromPath.getPath();
                tvPhoto1.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 202) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivPhoto2.setImageBitmap(myBitmap2);

                    myBitmap2 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap2.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath2 = f.getAbsolutePath();
                tvPhoto2.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 201) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap2 = getBitmapFromCameraData(data, this);

                ivPhoto2.setImageBitmap(myBitmap2);
                imagePath2 = uriFromPath.getPath();
                tvPhoto2.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap2.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 302) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapAadhar = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivAadharCardPhoto.setImageBitmap(bitmapAadhar);

                    bitmapAadhar = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapAadhar.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathAadhar = f.getAbsolutePath();
                tvAadharCard.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 301) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapAadhar = getBitmapFromCameraData(data, this);

                ivAadharCardPhoto.setImageBitmap(bitmapAadhar);
                pathAadhar = uriFromPath.getPath();
                tvAadharCard.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapAadhar.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 402) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapOrigLicense = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivOrigLicensePhoto.setImageBitmap(bitmapOrigLicense);

                    bitmapOrigLicense = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapOrigLicense.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathOrigLicense = f.getAbsolutePath();
                tvOrigLicense.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 401) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapOrigLicense = getBitmapFromCameraData(data, this);

                ivOrigLicensePhoto.setImageBitmap(bitmapOrigLicense);
                pathOrigLicense = uriFromPath.getPath();
                tvOrigLicense.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapOrigLicense.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 502) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapRCBook = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivRCBookPhoto.setImageBitmap(bitmapRCBook);

                    bitmapRCBook = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapRCBook.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathRCBook = f.getAbsolutePath();
                tvRCBook.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 501) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapRCBook = getBitmapFromCameraData(data, this);

                ivRCBookPhoto.setImageBitmap(bitmapRCBook);
                pathRCBook = uriFromPath.getPath();
                tvRCBook.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapRCBook.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 602) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapPUC = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivPUCPhoto.setImageBitmap(bitmapPUC);

                    bitmapPUC = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapPUC.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathPUC = f.getAbsolutePath();
                tvPUC.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 601) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapPUC = getBitmapFromCameraData(data, this);

                ivPUCPhoto.setImageBitmap(bitmapPUC);
                pathPUC = uriFromPath.getPath();
                tvPUC.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapPUC.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 702) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapInsurance1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivInsurance1Photo.setImageBitmap(bitmapInsurance1);

                    bitmapInsurance1 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapInsurance1.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathInsurance1 = f.getAbsolutePath();
                tvInsurance1.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 701) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapInsurance1 = getBitmapFromCameraData(data, this);

                ivInsurance1Photo.setImageBitmap(bitmapInsurance1);
                pathInsurance1 = uriFromPath.getPath();
                tvInsurance1.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapInsurance1.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 802) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapInsurance2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivInsurance2Photo.setImageBitmap(bitmapInsurance2);

                    bitmapInsurance2 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapInsurance2.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathInsurance2 = f.getAbsolutePath();
                tvInsurance2.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 801) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapInsurance2 = getBitmapFromCameraData(data, this);

                ivInsurance2Photo.setImageBitmap(bitmapInsurance2);
                pathInsurance2 = uriFromPath.getPath();
                tvInsurance2.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapInsurance2.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 902) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapAddProof = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivAddProofPhoto.setImageBitmap(bitmapAddProof);

                    bitmapAddProof = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapAddProof.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathAddProof = f.getAbsolutePath();
                tvAddProof.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 901) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapAddProof = getBitmapFromCameraData(data, this);

                ivAddProofPhoto.setImageBitmap(bitmapAddProof);
                pathAddProof = uriFromPath.getPath();
                tvAddProof.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapAddProof.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 1002) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapBankLetter = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivBankLetterPhoto.setImageBitmap(bitmapBankLetter);

                    bitmapBankLetter = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapBankLetter.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathBankLetter = f.getAbsolutePath();
                tvBankLetter.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 1001) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapBankLetter = getBitmapFromCameraData(data, this);

                ivBankLetterPhoto.setImageBitmap(bitmapBankLetter);
                pathBankLetter = uriFromPath.getPath();
                tvBankLetter.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapBankLetter.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 1102) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapForm17 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivForm17Photo.setImageBitmap(bitmapForm17);

                    bitmapForm17 = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapForm17.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathForm17 = f.getAbsolutePath();
                tvForm17.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 1101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapForm17 = getBitmapFromCameraData(data, this);

                ivForm17Photo.setImageBitmap(bitmapForm17);
                pathForm17 = uriFromPath.getPath();
                tvForm17.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapForm17.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 1202) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapBankNOC = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivBankNOCPhoto.setImageBitmap(bitmapBankNOC);

                    bitmapBankNOC = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapBankNOC.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathBankNOC = f.getAbsolutePath();
                tvBankNOC.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 1201) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapBankNOC = getBitmapFromCameraData(data, this);

                ivBankNOCPhoto.setImageBitmap(bitmapBankNOC);
                pathBankNOC = uriFromPath.getPath();
                tvBankNOC.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapBankNOC.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 1302) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapFIR = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivFIRPhoto.setImageBitmap(bitmapFIR);

                    bitmapFIR = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapFIR.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathFIR = f.getAbsolutePath();
                tvFIR.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 1301) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapFIR = getBitmapFromCameraData(data, this);

                ivFIRPhoto.setImageBitmap(bitmapFIR);
                pathFIR = uriFromPath.getPath();
                tvFIR.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapFIR.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == 1402) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    bitmapVehicleInfo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivVehicleInfoPhoto.setImageBitmap(bitmapVehicleInfo);

                    bitmapVehicleInfo = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        bitmapVehicleInfo.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                pathVehicleInfo = f.getAbsolutePath();
                tvVehicleInfo.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 1201) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                bitmapVehicleInfo = getBitmapFromCameraData(data, this);

                ivVehicleInfoPhoto.setImageBitmap(bitmapVehicleInfo);
                pathVehicleInfo = uriFromPath.getPath();
                tvVehicleInfo.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmapVehicleInfo.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        }
    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        path1 = picturePath;
        cursor.close();

        Bitmap bitm = shrinkBitmap(picturePath, 720, 720);
        Log.e("Image Size : ---- ", " " + bitm.getByteCount());

        return bitm;
        // return BitmapFactory.decodeFile(picturePath, options);
    }

    public static Bitmap shrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    private void sendImage(ArrayList<String> filePath, ArrayList<String> fileName, final WorkHeader workHeader) {

        Log.e("PARAMETER : ", "   FILE PATH : " + filePath + "            FILE NAME : " + fileName);

        final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = null;

        MultipartBody.Part[] uploadImagesParts = new MultipartBody.Part[filePath.size()];

        for (int index = 0; index < filePath.size(); index++) {
            Log.e("ATTACH ACT", "requestUpload:  image " + index + "  " + filePath.get(index));
            imgFile = new File(filePath.get(index));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            uploadImagesParts[index] = MultipartBody.Part.createFormData("file", "" + fileName.get(index), surveyBody);
        }


        // RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), "photo1");
        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), "1");

        Call<JSONObject> call = Constants.myInterface.imageUpload(uploadImagesParts, fileName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();

//                path1 = "";
//                imagePath1 = "";
//                imagePath2 = "";
//                pathAadhar = "";
//                pathOrigLicense = "";
//                pathRCBook = "";
//                pathPUC = "";
//                pathInsurance1 = "";
//                pathInsurance2 = "";
//                pathAddProof = "";
//                pathBankLetter = "";
//                pathForm17 = "";
//                pathBankNOC = "";

                path1 = null;
                imagePath1 = null;
                imagePath2 = null;
                pathAadhar = null;
                pathOrigLicense = null;
                pathRCBook = null;
                pathPUC = null;
                pathInsurance1 = null;
                pathInsurance2 = null;
                pathAddProof = null;
                pathBankLetter = null;
                pathForm17 = null;
                pathBankNOC = null;
                pathFIR = null;
                pathVehicleInfo = null;

                Log.e("Response : ", "--" + response.body());

                saveWorkHeader(workHeader);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(AttachmentActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTaskDescById(int id) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<TaskDesc> listCall = Constants.myInterface.getTaskDescById(id);
            listCall.enqueue(new Callback<TaskDesc>() {
                @Override
                public void onResponse(Call<TaskDesc> call, Response<TaskDesc> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("TASK DESC LIST : ", " - " + response.body());

                            taskDesc = response.body();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TaskDesc> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveWorkHeader(WorkHeader workHeader) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.saveWorkHeader(workHeader);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE WORK HEADER : ", " - " + response.body());

                            Info info = response.body();
                            if (!info.getError()) {
                                Toast.makeText(AttachmentActivity.this, "Documents uploaded successfully", Toast.LENGTH_SHORT).show();

                                CustomSharedPreference.deletePreferenceByKey(AttachmentActivity.this, CustomSharedPreference.KEY_HEADER);
                                CustomSharedPreference.deletePreferenceByKey(AttachmentActivity.this, CustomSharedPreference.KEY_PATH_ARRAY);
                                CustomSharedPreference.deletePreferenceByKey(AttachmentActivity.this, CustomSharedPreference.KEY_FILE_NAME_ARRAY);
                                CustomSharedPreference.deletePreferenceByKey(AttachmentActivity.this, CustomSharedPreference.KEY_TYPE);
                                CustomSharedPreference.deletePreferenceByKey(AttachmentActivity.this, CustomSharedPreference.KEY_WORK_TYPE);

                                sendSMS(customer.getCustMobile());

                                finish();
                            } else {
                                Toast.makeText(AttachmentActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(AttachmentActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(AttachmentActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(AttachmentActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    public void savePreviousData() {

        if (customer != null) {
            Log.e("savePrevData", "-------------------- " + customer);

            int type = CustomSharedPreference.getInt(AttachmentActivity.this, CustomSharedPreference.KEY_TYPE);

            if (type == 1) {

                String headerStr = CustomSharedPreference.getString(AttachmentActivity.this, CustomSharedPreference.KEY_HEADER);
                Gson gsonHeader = new Gson();
                WorkHeader header = gsonHeader.fromJson(headerStr, WorkHeader.class);
                header.setCustId(customer.getCustId());

                String pathStr = CustomSharedPreference.getString(AttachmentActivity.this, CustomSharedPreference.KEY_PATH_ARRAY);
                Gson gsonPath = new Gson();
                Type typePath = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> pathArray = gsonPath.fromJson(pathStr, typePath);

                String fileStr = CustomSharedPreference.getString(AttachmentActivity.this, CustomSharedPreference.KEY_FILE_NAME_ARRAY);
                Gson gsonFile = new Gson();
                Type typeFile = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> fileNameArray = gsonFile.fromJson(fileStr, typeFile);

                sendImage(pathArray, fileNameArray, header);
            }


        }

    }


    public void sendSMS(String mobile) {
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

            String message = "Documents uploaded successfully for " + workTypeModel.getWorkTypeName() + ". Our representative will contact you shortly.";

            Call<String> listCall = myInterface.sendOTP("246089Au56zI8BbKQE5bdd7464", mobile, message, "EZIRTO", 4, 91);
            listCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    Log.e("OTP RESPONSE : ", " " + response.body());

                    commonDialog.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    commonDialog.dismiss();
                    //Toast.makeText(AttachmentActivity.this, "Unable to send OTP, please try again", Toast.LENGTH_SHORT).show();
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
