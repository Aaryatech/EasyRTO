package com.ats.easyrto.interfaces;


import com.ats.easyrto.model.Cust;
import com.ats.easyrto.model.Info;
import com.ats.easyrto.model.LoginResponse;
import com.ats.easyrto.model.TaskDesc;
import com.ats.easyrto.model.UpdateCostModel;
import com.ats.easyrto.model.WorkHeader;
import com.ats.easyrto.model.WorkTypeModel;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InterfaceApi {

    @POST("saveCust")
    Call<Cust> saveCustomer(@Body Cust customer);

    @POST("loginResponseCust")
    Call<LoginResponse> doLogin(@Query("custMobile") String custMobile, @Query("custPassword") String custPassword);

    @GET("getAllWorkTypeList")
    Call<ArrayList<WorkTypeModel>> getAllWorkType();

    @Multipart
    @POST("photoUpload")
    Call<JSONObject> imageUpload(@Part MultipartBody.Part[] filePath, @Part("imageName") ArrayList<String> name, @Part("type") RequestBody type);

    @POST("saveOrderHeaderDetail")
    Call<Info> saveWorkHeader(@Body WorkHeader workHeader);

    @POST("getTaskByInnerTaskId")
    Call<TaskDesc> getTaskDescById(@Query("innerTaskId") int innerTaskId);

    @POST("getCustWorkHeader")
    Call<ArrayList<WorkHeader>> getWorkStatusList(@Query("custId") int custId);

    @POST("changeCustPass")
    Call<Info> changePassword(@Query("custId") int custId, @Query("oldPass") String oldPass, @Query("newPass") String newPass);

    //OTP API
    @GET("sendhttp.php")
    Call<String> sendOTP(@Query("authkey") String authkey, @Query("mobiles") String mobiles, @Query("message") String message, @Query("sender") String sender, @Query("route") int route, @Query("country") int country);

    @POST("updateToken")
    Call<Info> updateToken(@Query("custId") int custId, @Query("token") String token);

    @POST("changeCustPassByMobNo")
    Call<Info> updateNewPassword(@Query("custMobile") String custMobile, @Query("newPass") String newPass);

    @POST("getCustomerByMobileNo")
    Call<Info> checkMobileExists(@Query("custMobile") String custMobile);

    @POST("saveCustomer")
    Call<Cust> updateCustomer(@Body Cust customer);

    @POST("updateWorkPayment")
    Call<Info> updateWorkCost(@Body ArrayList<UpdateCostModel> updateCostModel);

    // renew - https://image.flaticon.com/icons/png/512/716/716704.png
    // transfer - http://www.jmkxyy.com/transfer-icon/transfer-icon-11.jpg
    // noc - https://fsmsolicitors.co.uk/wp-content/uploads/2018/03/document-icon.png
    // hypo - http://developers.institute/wp-content/uploads/2018/02/150256-200.png
    // cancel - https://cdn1.iconfinder.com/data/icons/money-4/512/credit_card_remove-512.png
    // drc - https://static.thenounproject.com/png/141695-200.png

}
