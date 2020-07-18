package com.khalej.fastcar.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.khalej.fastcar.R;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.Reset;
import com.khalej.fastcar.model.apiinterface_home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {
    TextInputEditText textInputEditTextphone,textInputEditTextpassword;
    AppCompatButton appCompatButtonRegisterservcies;
    ProgressDialog progressDialog;
    private apiinterface_home apiinterface;
    Reset reset;
    int id;
    LinearLayout newpassword;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        setContentView(R.layout.activity_forget_password);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        newpassword =findViewById(R.id.newpassword);
        textInputEditTextphone=findViewById(R.id.textInputEditTextphone);
        textInputEditTextpassword=findViewById(R.id.textInputEditTextpassword);
        newpassword.setVisibility(View.GONE);
        appCompatButtonRegisterservcies=findViewById(R.id.appCompatButtonRegisterservcies);
        appCompatButtonRegisterservcies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpassword.getVisibility()==View.GONE){
                    fetchInfo();}
                else{
                    fetchInfo_();
                }
            }
        });

    }

    public void fetchInfo(){
        progressDialog = ProgressDialog.show(ForgetPassword.this,"جاري الإرسال","Please wait...",false,false);
        progressDialog.show();

        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<Reset> call= apiinterface.getcontacts_ResetPassword(
                textInputEditTextphone.getText().toString());
        call.enqueue(new Callback<Reset>() {
            @Override
            public void onResponse(Call<Reset> call, Response<Reset> response) {
                reset=response.body();
                progressDialog.dismiss();
                try {


                    if (reset.getCan() == 1) {
                        Toast.makeText(ForgetPassword.this, "أدخل كلمة سر جديدة", Toast.LENGTH_LONG).show();
                        newpassword.setVisibility(View.VISIBLE);
                        id = reset.getUser_id();
                    } else {
                        Toast.makeText(ForgetPassword.this, "هذه البيانات غير مسجلة", Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception e){
                    Toast.makeText(ForgetPassword.this, "هذه البيانات غير مسجلة", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Reset> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
    public void fetchInfo_(){
        progressDialog = ProgressDialog.show(ForgetPassword.this,"جاري حفظ كلمة السر الجديدة","Please wait...",false,false);
        progressDialog.show();

        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<ResponseBody> call= apiinterface.update_password(
                id, textInputEditTextpassword.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(ForgetPassword.this,"تم حفظ كلمة السر الجديدة", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
