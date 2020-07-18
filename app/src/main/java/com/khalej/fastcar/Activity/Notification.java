package com.khalej.fastcar.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.khalej.fastcar.Adapter.RecyclerAdapter_notification;
import com.khalej.fastcar.R;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.apiinterface_home;
import com.khalej.fastcar.model.notificationData;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {
    private apiinterface_home apiinterface;
    RecyclerView recyclerView;
    EditText message;
    ImageView send;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter_notification recyclerAdapter;
    private List<notificationData> contactList = new ArrayList<>();
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        this.setTitle("");
        progressBar=(ProgressBar)findViewById(R.id.progressBar_subject);
        progressBar.setVisibility(View.VISIBLE);

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

        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        recyclerView=findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(this, 1);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(
                        1, //The number of Columns in the grid
                        LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);

        fetchInfo();
    }
    public void fetchInfo(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<List<notificationData>> call = apiinterface.getcontacts_Notification(sharedpref.getInt("id",0));
        call.enqueue(new Callback<List<notificationData>>() {
            @Override
            public void onResponse(Call<List<notificationData>> call, Response<List<notificationData>> response) {
                progressBar.setVisibility(View.GONE);

                try {


                    contactList = response.body();
                    if (response.code() == 404) {
                        contactList=new ArrayList<>();
                        return;
                    }
                    if(contactList.isEmpty()){
                        contactList=new ArrayList<>();
                    }
                    else {
                        //  Toast.makeText(ChatActivity.this, "22", Toast.LENGTH_LONG).show();
                        recyclerAdapter = new RecyclerAdapter_notification(Notification.this, contactList);
                        recyclerView.setAdapter(recyclerAdapter);
                      //  recyclerView.scrollToPosition(contactList.size() - 1);
                    }
                }
                catch (Exception e){
                    //  Toast.makeText(ChatActivity.this,e+"",Toast.LENGTH_LONG).show();
                    contactList=new ArrayList<>();
                }

            }

            @Override
            public void onFailure(Call<List<notificationData>> call, Throwable t) {
                contactList=new ArrayList<>();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


            finish();

    }
}

