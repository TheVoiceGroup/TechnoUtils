package com.tmclients.technoutils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Logger {

    private Context context;
    private String API_LEVEL, DEVICE, MODEL, PRODUCT, FINGERPRINT, TYPE, BRAND, DISPLAY, MANUFACTURER, ANDROIDID;
    PreferenceHelper preferenceHelper;
    private String ARRAYNAME;
    RootBeer rootBeer;

    public Logger(Context context, String PreferenceHelperMainKEY, String ARRAYNAME){
        this.context = context;
        preferenceHelper = new PreferenceHelper(context, PreferenceHelperMainKEY);
        this.ARRAYNAME = ARRAYNAME;
        rootBeer = new RootBeer(context);
    }

    public void LOGUSERDEVICEINFO(String USERURL, String TOKEN, String APPID, String APPVERSION, String CLIENTID){
        ANDROIDID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        API_LEVEL = Build.VERSION.SDK;
        DEVICE = Build.DEVICE;
        MODEL = Build.MODEL;
        PRODUCT = Build.PRODUCT;
        FINGERPRINT = Build.FINGERPRINT;
        TYPE = Build.TYPE;
        BRAND = Build.BRAND;
        DISPLAY = Build.DISPLAY;
        MANUFACTURER = Build.MANUFACTURER;
        LOGUSER(USERURL, APPID, APPVERSION, TOKEN, ANDROIDID, API_LEVEL, DEVICE, MODEL, PRODUCT, FINGERPRINT, TYPE, BRAND, DISPLAY, MANUFACTURER, CLIENTID);
    }


    public void LOGUSER(String USERURL, String APPID, String APPVERSION, String Device_ID, String ANDROIDID, String API_LEVEL, String DEVICE, String MODEL, String PRODUCT, String FINGERPRINT, String TYPE, String BRAND, String DISPLAY, String MANUFACTURER, String CLIENTID){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("app_id", APPID);
        params.put("app_version", APPVERSION);
        params.put("device_id", Device_ID);
        params.put("android_id", ANDROIDID);
        params.put("api_level", API_LEVEL);
        params.put("device", DEVICE);
        params.put("model", MODEL);
        params.put("product", PRODUCT);
        params.put("fingerprint", FINGERPRINT);
        params.put("type", TYPE);
        params.put("brand", BRAND);
        params.put("display", DISPLAY);
        params.put("manufacturer", MANUFACTURER);
        params.put("client_id", CLIENTID);
        params.put("device_rooted", rootBeer.isRooted());

        client.get(USERURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(ARRAYNAME);
                    JSONObject objJson;
                    objJson = jsonArray.getJSONObject(0);
                    String UserID = objJson.getString("ID");
                    String Status = objJson.getString("STATUS");
                    preferenceHelper.SaveString("ADUSERID",UserID);
                    preferenceHelper.SaveString("ADSTATUS",Status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void LOGACTIVITY(String URL, String ACTIVITYTYPE){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", preferenceHelper.GetString("ADUSERID", "0"));
        params.put("activity_type", ACTIVITYTYPE);

        client.get(URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(ARRAYNAME);
                    JSONObject objJson;
//                    for (int i = 0; i < jsonArray.length(); i++) {
                    objJson = jsonArray.getJSONObject(0);
//                    String UserID = objJson.getString("ID");
//                    String Status = objJson.getString("STATUS");
//                    preferenceHelper.SAVEUSERID(UserID);
//                    preferenceHelper.SAVEADSTATUS(Status);
//                    Ad();
//                        strMessage = objJson.getString(Constant.MSG);
//                        Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void LOGSTATUS(String URL, String STATUS){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", preferenceHelper.GetString("ADUSERID", "0"));
        params.put("status", STATUS);

        client.get(URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                preferenceHelper.SaveString("ADSTATUS","BLOCKED");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public String RETURNADSTATUS(){
        return preferenceHelper.GetString("ADSTATUS", "ACTIVE");
    }
}
