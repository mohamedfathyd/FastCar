package com.khalej.fastcar.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.khalej.fastcar.R;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class UserProfile extends AppCompatActivity {
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    Button Confirm;
    TextView name,phone,email;
    CircleImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        userImage=findViewById(R.id.image);
        Confirm=findViewById(R.id.confirm);
        email.setText(sharedpref.getString("address",""));
        phone.setText(sharedpref.getString("phone",""));
        name.setText(sharedpref.getString("name",""));
        Glide.with(this).load(sharedpref.getString("image","")).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ww).into(userImage);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(UserProfile.this,EditProfile.class);
                intent.putExtra("name",sharedpref.getString("name",""));
                intent.putExtra("phone",sharedpref.getString("phone",""));
                startActivity(intent);
            }
        });
    }
}
