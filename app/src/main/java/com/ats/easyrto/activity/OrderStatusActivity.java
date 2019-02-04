package com.ats.easyrto.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.ats.easyrto.R;
import com.ats.easyrto.adapter.WorkTypeListAdapter;
import com.ats.easyrto.constants.Constants;
import com.ats.easyrto.fragment.CompletedFragment;
import com.ats.easyrto.fragment.InProcessFragment;
import com.ats.easyrto.fragment.PendingFragment;
import com.ats.easyrto.interfaces.CompletedInterface;
import com.ats.easyrto.interfaces.InProcessInterface;
import com.ats.easyrto.interfaces.PendingInterface;
import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.WorkHeader;
import com.ats.easyrto.model.WorkTypeModel;
import com.ats.easyrto.util.CommonDialog;
import com.ats.easyrto.util.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusActivity extends AppCompatActivity {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    FragmentPagerAdapter adapterViewPager;

    public static ArrayList<WorkHeader> staticWorkHeaderList = new ArrayList<>();
    public static ArrayList<WorkHeader> staticPendingWorkList = new ArrayList<>();
    public static ArrayList<WorkHeader> staticInProcessWorkList = new ArrayList<>();
    public static ArrayList<WorkHeader> staticCompletedWorkList = new ArrayList<>();

    Cust customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setTitle(Html.fromHtml("<font color='#000000'>Order Status</font>"));

        String customerStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_CUSTOMER);
        Gson gsonCust = new Gson();
        customer = gsonCust.fromJson(customerStr, Cust.class);
        Log.e("Customer Bean : ", "---------------" + customer);


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    PendingInterface fragmentPending = (PendingInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentPending != null) {
                        fragmentPending.fragmentBecameVisible();
                    }
                } else if (position == 1) {
                    InProcessInterface fragmentInProcess = (InProcessInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentInProcess != null) {
                        fragmentInProcess.fragmentBecameVisible();
                    }
                } else if (position == 2) {
                    CompletedInterface fragmentCompleted = (CompletedInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentCompleted != null) {
                        fragmentCompleted.fragmentBecameVisible();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getWorkStatusList(customer.getCustId());


    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PendingFragment();
                case 1:
                    return new InProcessFragment();
                case 2:
                    return new CompletedFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Pending";
                case 1:
                    return "In Process";
                case 2:
                    return "Completed";
                default:
                    return null;
            }
        }
    }


    public void getWorkStatusList(int custId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<WorkHeader>> listCall = Constants.myInterface.getWorkStatusList(custId);
            listCall.enqueue(new Callback<ArrayList<WorkHeader>>() {
                @Override
                public void onResponse(Call<ArrayList<WorkHeader>> call, Response<ArrayList<WorkHeader>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("WORK STATUS LIST : ", " - " + response.body());

                            if (staticWorkHeaderList != null) {
                                staticWorkHeaderList.clear();
                            }

                            staticWorkHeaderList = response.body();

                            if (staticWorkHeaderList.size() > 0) {

                                if (staticPendingWorkList != null) {
                                    staticPendingWorkList.clear();
                                }

                                if (staticInProcessWorkList != null) {
                                    staticInProcessWorkList.clear();
                                }

                                if (staticCompletedWorkList != null) {
                                    staticCompletedWorkList.clear();
                                }

                                for (int i = 0; i < staticWorkHeaderList.size(); i++) {
                                    if (staticWorkHeaderList.get(i).getStatus() >= 1 && staticWorkHeaderList.get(i).getStatus() <= 2) {
                                        staticPendingWorkList.add(staticWorkHeaderList.get(i));
                                    } else if (staticWorkHeaderList.get(i).getStatus() >= 3 && staticWorkHeaderList.get(i).getStatus() < 7) {
                                        staticInProcessWorkList.add(staticWorkHeaderList.get(i));
                                    } else if (staticWorkHeaderList.get(i).getStatus() == 7) {
                                        staticCompletedWorkList.add(staticWorkHeaderList.get(i));
                                    }
                                }
                            }

                            viewPager.setCurrentItem(1);
                            viewPager.setCurrentItem(0);

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
                public void onFailure(Call<ArrayList<WorkHeader>> call, Throwable t) {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
