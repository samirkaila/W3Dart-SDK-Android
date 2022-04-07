package com.w3dartsdk.remote;

/*
    This is a constant file used for api calling
 */
public interface ApiConstants {

    String BASE_URL = "http://nuai.w3n.xyz";

    // authentication model
    String LOGIN_URL = "/api/login";
    String SIGNUP_URL = "/api/register";
    String LOGOUT_USER = "/api/logout";

    String FORGOT_PASSWORD = "/api/forgot-password";

    // face scan result
    String STORE_USER_SCAN_RESULT = "/api/store-user-scan-result";
    String DELETE_USER_SCAN_RESULT = "/api/delete-user-scan-result";
//    String DOWNLOAD_RESULT = "/api/download-result/?scan_result_id={scan_result_id}";
    String DOWNLOAD_RESULT = "/api/download-result/";
    String GET_USER_SCAN_RESULT_HISTORY_LOG = "/api/get-user-scan-result-history-log";
    String GET_HISTORY_TRENDS = "/api/history-graph";

    // User profile apis
    String UPDATE_PROFILE = "/api/update-profile";
    String UPDATE_PROFILE_PICTURE = "/api/update-profile-pic";
    String REMOVE_PROFILE_PICTURE = "/api/remove-profile-pic";
    String DELETE_ACCOUNT = "/api/delete-account";

    // User profile apis
    String TERMS_CONDITIONS = "/api/update-profile";
    String PRIVACY_POLICY = "/api/update-profile-pic";

    // Information
    String INFORMATION = "/api/information";

    // In app purchase apis
    String CHECK_PURCHASE_STATUS = "/api/check-user-plan-purchase-status";
    String STORE_PAYMENT_DETAIL = "/api/store-user-payment-detail";

}