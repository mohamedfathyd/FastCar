package com.khalej.fastcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.apiinterface_home;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO ="" ;
    private SharedPreferences sharedpref;
    String token;
    private SharedPreferences.Editor edt;
    private apiinterface_home apiinterface;
    @Override
    public void onTokenRefresh() {
        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        token = FirebaseInstanceId.getInstance().getToken();
        // Once the token is g enerated, subscribe to topic with the userId
     //   FirebaseMessaging.getInstance().subscribeToTopic(token);
        edt.putString("token",token);
        edt.apply();
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
        setToken();
    }
    public void setToken(){
        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);

        Call<ResponseBody> call = apiinterface.getcontacts_AddToken(sharedpref.getInt("id",0),token,0);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
