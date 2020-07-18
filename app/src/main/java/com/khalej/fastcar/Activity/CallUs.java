package com.khalej.fastcar.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.khalej.fastcar.R;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.apiinterface_home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallUs extends AppCompatActivity {
    TextInputEditText textInputEditTextname,textInputEditTextaddress,textInputEditTextphone,
            textInputEditTextmessage;
    AppCompatButton appCompatButton;
    private apiinterface_home apiinterface;
    ProgressDialog progressDialog;
    LinearLayout whatsone,whatstwo,whatsthree,gmail,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_us);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        this.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        whatsone=findViewById(R.id.whatsone);
        whatstwo=findViewById(R.id.whatstwo);
        whatsthree=findViewById(R.id.whatsthree);
        gmail=findViewById(R.id.gmail);
        phone=findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+966920033169"));
                startActivity(intent);
            }
        });
        whatsone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String url = "https://api.whatsapp.com/send?phone="+"+201030496669";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);}
                catch( Exception e){
                    Toast.makeText(CallUs.this, "غير متاحه الأن عاود المحاولة لاحقا " ,Toast.LENGTH_LONG).show();
                }
            }
        });
        whatstwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String url = "https://api.whatsapp.com/send?phone="+"+9660548254000";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);}
                catch( Exception e){
                    Toast.makeText(CallUs.this, "غير متاحه الأن عاود المحاولة لاحقا " ,Toast.LENGTH_LONG).show();
                }
            }
        });
        whatsthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    String url = "https://api.whatsapp.com/send?phone="+"+9660500205354";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);}
                catch( Exception e){
                    Toast.makeText(CallUs.this, "غير متاحه الأن عاود المحاولة لاحقا " ,Toast.LENGTH_LONG).show();
                }
            }
        });
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "EngServies4000@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "نشكرك على التواصل معنا قم بوضع مقترحاتك ");
               startActivity(Intent.createChooser(emailIntent, null));
            }
        });
        textInputEditTextname=findViewById(R.id.textInputEditTextName);
        textInputEditTextaddress=findViewById(R.id.textInputEditTextaddress);
        textInputEditTextphone=findViewById(R.id.textInputEditTextphone);
        textInputEditTextmessage=findViewById(R.id.textInputEditTextmessage);
        appCompatButton=findViewById(R.id.appCompatButtonRegister);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchInfo();
            }
        });
    }
    public void fetchInfo() {

        progressDialog = ProgressDialog.show(CallUs.this, "جاري ارسال رسالتك", "Please wait...", false, false);
        progressDialog.show();
        String phone=textInputEditTextphone.getText().toString();

        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<ResponseBody> call = apiinterface.CallUs(textInputEditTextname.getText().toString(),textInputEditTextaddress.getText().toString(),
                textInputEditTextphone.getText().toString(),textInputEditTextmessage.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CallUs.this);
                dlgAlert.setMessage("تم ارسال رسالتك بنجاح");
                dlgAlert.setTitle("Fast Car");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(CallUs.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
