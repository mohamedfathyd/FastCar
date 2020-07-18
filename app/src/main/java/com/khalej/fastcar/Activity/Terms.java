package com.khalej.fastcar.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.khalej.fastcar.R;
import com.khalej.fastcar.model.AboutUs;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.apiinterface_home;

import androidx.appcompat.app.AppCompatActivity;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Terms extends AppCompatActivity {
    private apiinterface_home apiinterface;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    AboutUs respons =new AboutUs();
    String conent;
    TextView terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        terms=findViewById(R.id.terms);
        fetchInfo();

    }
    public void fetchInfo() {

        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<AboutUs> call = apiinterface.Conditoins_ar();
        call.enqueue(new Callback<AboutUs>() {
            @Override
            public void onResponse(Call<AboutUs> call, Response<AboutUs> response) {
                try {
                    respons=response.body();
                    sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
                    edt = sharedpref.edit();

                    try{
                        if(sharedpref.getString("language","").trim().equals("ar")){
                            conent=respons.getAr_content();}
                        else{
                            conent=respons.getAr_content();
                        }}
                    catch (Exception e){
                        conent=respons.getAr_content();
                    }
                   terms.setText(conent);
                }
                catch (Exception e){}
            }

            @Override
            public void onFailure(Call<AboutUs> call, Throwable t) {
                // Toast.makeText(Terms.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
