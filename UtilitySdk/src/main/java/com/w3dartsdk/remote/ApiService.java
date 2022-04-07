package com.w3dartsdk.remote;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/*
    This is an interface file for that is used to call APIs
 */
public interface ApiService {

    // SCAN RESULT APIS
    @POST(ApiConstants.STORE_USER_SCAN_RESULT)
    Call<Object> storeUserScanResult(
            @Body JsonObject jsonObject,
            @Header("Authorization") String token
    );


    // User profile apis
    @Multipart
    @POST(ApiConstants.UPDATE_PROFILE)
    Call<Object> editProfile(
            @PartMap Map<String, RequestBody> params,
            @Header("Authorization") String token
    );


}