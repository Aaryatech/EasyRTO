package com.ats.easyrto.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.adapter.WorkTypeListAdapter;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.Info;
import com.ats.easyrto.model.WorkTypeModel;
import com.ats.easyrto.util.CommonDialog;
import com.ats.easyrto.util.CustomSharedPreference;
import com.ats.easyrto.util.PermissionsUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private FloatingActionButton fab;

    Cust customer;

    ArrayList<WorkTypeModel> workTypeList = new ArrayList<>();
    WorkTypeListAdapter adapter;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "EasyRTO");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // getActionBar().setBackgroundDrawable(getDrawable(R.drawable.actionbar_back));

        setTitle(Html.fromHtml("<font color='#000000'>Ezi RTO</font>"));

        if (PermissionsUtil.checkAndRequestPermissions(this)) {
        }

        String customerStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_CUSTOMER);
        Gson gson = new Gson();
        customer = gson.fromJson(customerStr, Cust.class);
        Log.e("Customer Bean : ", "---------------" + customer);

        int type = CustomSharedPreference.getInt(this, CustomSharedPreference.KEY_TYPE);
        if (type == 1) {

            String jsonStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_WORK_TYPE);

            Intent intent = new Intent(this, AttachmentActivity.class);
            intent.putExtra("json", jsonStr);
            startActivity(intent);
        }

        int orderStatusAct = getIntent().getIntExtra("orderStatusActivity", 0);
        if (orderStatusAct == 1) {
            startActivity(new Intent(HomeActivity.this, OrderStatusActivity.class));
        }

        /*try {
            if (customer == null) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }

        } catch (Exception e) {
        }*/

        gridView = findViewById(R.id.gridView);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        createFolder();

        getAllWorkType();

    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    public void getAllWorkType() {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<WorkTypeModel>> listCall = Constants.myInterface.getAllWorkType();
            listCall.enqueue(new Callback<ArrayList<WorkTypeModel>>() {
                @Override
                public void onResponse(Call<ArrayList<WorkTypeModel>> call, Response<ArrayList<WorkTypeModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("WORK TYPE LIST : ", " - " + response.body());

                            workTypeList.clear();
                            workTypeList = response.body();

                            adapter = new WorkTypeListAdapter(workTypeList, HomeActivity.this);
                            gridView.setAdapter(adapter);

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
                public void onFailure(Call<ArrayList<WorkTypeModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        if (customer == null) {
            MenuItem item1 = menu.findItem(R.id.action_user);
            MenuItem item2 = menu.findItem(R.id.action_change_pass);
            MenuItem item3 = menu.findItem(R.id.action_logout);
            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_user) {

            startActivity(new Intent(HomeActivity.this, UserInfoActivity.class));

            return true;
        } else if (id == R.id.action_change_pass) {

            startActivity(new Intent(HomeActivity.this, ChangePasswordActivity.class));

            return true;
        } else if (id == R.id.action_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //updateUserToken(user.getUserId(), "");

                    CustomSharedPreference.deletePreference(HomeActivity.this);

                    updateToken(customer.getCustId(), "");
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            if (customer == null) {
                startActivity(new Intent(HomeActivity.this, LoginAndRegistrationActivity.class));
                finish();

            } else {
                startActivity(new Intent(HomeActivity.this, OrderStatusActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
        builder.setTitle("Confirm Action");
        builder.setMessage("Do you really want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void updateToken(int userId, String token) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.updateToken(userId, token);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE TOKEN : ", "------------" + response.body());

                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                            commonDialog.dismiss();
                        } else {
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

}
