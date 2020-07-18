package com.khalej.fastcar.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.khalej.fastcar.Adapter.RecyclerAdapter_rides;
import com.khalej.fastcar.R;
import com.khalej.fastcar.model.AboutUs;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.My_Order;
import com.khalej.fastcar.model.Wallet;
import com.khalej.fastcar.model.apiinterface_home;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mores extends AppCompatActivity {
    TextView logout,terms,whous,callus,language,login,bank;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    CircleImageView image;
    TextView name ,address,phone ;
    private  static final int IMAGEUser = 99;
    Bitmap bitmapUser;
    String mediaPath,mediaPathId;
    String imagePath;
    ProgressDialog progressDialog;
    LinearLayout logut;
    Wallet contactList;
    ImageView addorder;
    private apiinterface_home apiinterface;
    private static final int MY_CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_REQUEST = 1;
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        logout=findViewById(R.id.logout);
        terms=findViewById(R.id.terms);
        whous=findViewById(R.id.whous);
        callus=findViewById(R.id.callus);
        language=findViewById(R.id.language);
        logut=findViewById(R.id.logut);
        image=findViewById(R.id.image);
        name=findViewById(R.id.username);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        bank=findViewById(R.id.bank);

        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();



        Glide.with(this).load(sharedpref.getString("image","")).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ww).into(image);

        name.setText(sharedpref.getString("name",""));
//        if(sharedpref.getInt("type",0)==2||sharedpref.getInt("type",0)==1){
//            address.setText( "الرصيد :" +sharedpref.getFloat("wallet",0));
//            address.setVisibility(View.GONE);
//            addorder.setVisibility(View.VISIBLE);
//        }
//        else{
//            address.setVisibility(View.GONE);
//            addorder.setVisibility(View.GONE);
//        }
        phone.setText(sharedpref.getString("phone",""));
      logout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              edt.putInt("id",0);
              edt.putString("name","");
              edt.putString("image","");
              edt.putString("phone","");
              edt.putString("address","");
              edt.putString("password","");
              edt.putString("createdAt","");
              edt.putInt("type",0);
              edt.putFloat("wallet",0);
              edt.putString("status", "");
              edt.putString("remember","no");
              edt.apply();
              startActivity(new Intent(Mores.this, Login.class));
            finish();
          }
      });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedpref.getString("user_type","").equals("client")){
                startActivity(new Intent(Mores.this, MainActivity.class));
                finish();}
                else{
                    startActivity(new Intent(Mores.this, MainDriver.class));
                    finish();
                }
            }
        });


        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try{
//                    String url = "https://api.whatsapp.com/send?phone="+"+97333348098";
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);}
//                catch( Exception e){
//                    Toast.makeText(getActivity(), "غير متاحه الأن عاود المحاولة لاحقا " ,Toast.LENGTH_LONG).show();
//                }

                startActivity(new Intent(Mores.this,CallUs.class));
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedpref.getString("language","").trim().equals("ar")){
                    edt.putString("language","en");
                    edt.apply();
                    if(sharedpref.getString("user_type","").equals("client")){
                        startActivity(new Intent(Mores.this, MainActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(Mores.this, MainDriver.class));
                        finish();
                    }
                }
                else
                {
                    edt.putString("language","ar");
                    edt.apply();
                    if(sharedpref.getString("user_type","").equals("client")){
                        startActivity(new Intent(Mores.this, MainActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(Mores.this, MainDriver.class));
                        finish();
                    }
                }
            }
        });

        whous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Mores.this,whous.class));
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Mores.this,Terms.class));
            }
        });

        if(sharedpref.getString("user_type","").equals("client")){}else{
            getwallet();
        }
    }
public void getwallet(){
    apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
    Call<Wallet> call = apiinterface.getwallet(sharedpref.getInt("id",0));
    call.enqueue(new Callback<Wallet>() {
        @Override
        public void onResponse(Call<Wallet> call, Response<Wallet> response) {

            try {


                contactList = response.body();
                if (response.code() == 404) {
                    return;
                }

                else {
                    //  Toast.makeText(ChatActivity.this, "22", Toast.LENGTH_LONG).show();
                    if(sharedpref.getInt("country_id",0)==1){
                        address.setText("رصيد الحساب : " + contactList.getBalance() +" EGP");}
                    else{
                        address.setText("رصيد الحساب : " + contactList.getBalance() +" SAR");
                    }
                    //recyclerView.scrollToPosition(contactList.size() - 1);
                }
            }
            catch (Exception e){
                //  Toast.makeText(ChatActivity.this,e+"",Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public void onFailure(Call<Wallet> call, Throwable t) {

        }
    });
}
}
