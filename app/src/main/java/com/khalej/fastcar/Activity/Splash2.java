package com.khalej.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.khalej.fastcar.R;

public class Splash2 extends AppCompatActivity {
   Button login,register;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        setContentView(R.layout.activity_splash2);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(Splash2.this,Login.class));
            }
        });
        if(sharedpref.getString("remember","").trim().equals("yes")){
            edt.putFloat("totalprice",0);
            edt.apply();
            startActivity(new Intent(Splash2.this,Login.class));
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Splash2.this);
                dialog.setContentView(R.layout.dialog_details);


                dialog.setTitle("اختر نوع المستخدم ");
                LinearLayout a,b;
                a=dialog.findViewById(R.id.a);
                b=dialog.findViewById(R.id.b);

                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Intent intent=new Intent(Splash2.this,CodeMobile.class);
                        intent.putExtra("type","client");
                        startActivity(intent);
                    }
                });
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Intent intent=new Intent(Splash2.this,CodeMobile.class);
                        intent.putExtra("type","driver");
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
    }
}
