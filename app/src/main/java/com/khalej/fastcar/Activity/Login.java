package com.khalej.fastcar.Activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.khalej.fastcar.LocationTrack;
import com.khalej.fastcar.R;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.apiinterface_home;
import com.khalej.fastcar.model.contact_userinfo;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.realm.Realm;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    TextView skip,signUp,forgetPassword,signUpFani;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    ProgressDialog progressDialog;
    private contact_userinfo contactList;
    private apiinterface_home apiinterface;

    String codee;
    LoginButton loginButton;
    AppCompatButton appCompatButtonRegisterservcies;
    LocationTrack locationTrack;
    CallbackManager callbackManager;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ACCESS_COARSE_LOCATION =2;
    double lat=0.0,lng=0.0;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final int REQUEST_LOCATION = 1;
    Realm realm;
    TextInputEditText textInputEditTextpassword,textInputEditTextemail;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_login);
        appCompatButtonRegisterservcies=findViewById(R.id.appCompatButtonRegisterservcies);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        realm = Realm.getDefaultInstance();
      //  FirebaseMessaging.getInstance().subscribeToTopic(sharedpref.getString("token",""));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        locationTrack = new LocationTrack(Login.this);
        if (locationTrack.canGetLocation()) {


            lng = locationTrack.getLongitude();
            lat = locationTrack.getLatitude();

            //    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(lng) + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }



        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        edt.putString("lat", String.valueOf(lat));
        edt.putString("lng", String.valueOf(lng));
        edt.apply();


        if(sharedpref.getString("remember","").trim().equals("yes")){
            edt.putFloat("totalprice",0);
            edt.apply();

            if(sharedpref.getString("user_type","").equals("client")){
                startActivity(new Intent(Login.this,MainActivity.class));}
            else{
                startActivity(new Intent(Login.this,MainDriver.class));
            }


            finish();
        }
        skip=findViewById(R.id.skip);
        textInputEditTextemail=findViewById(R.id.textInputEditTextemail);
        textInputEditTextpassword=findViewById(R.id.textInputEditTextpassword);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,MainActivity.class));
            }
        });
        forgetPassword=findViewById(R.id.forgetPassword);
        signUp=findViewById(R.id.signUp);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(Login.this, ForgetPassword.class));
            }
        });
        appCompatButtonRegisterservcies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textInputEditTextemail.getText().toString().equals("")||textInputEditTextemail.getText().toString()==null){}
                else{
                    fetchInfo();}
            }
        });
    }

    public void fetchInfo(){
        progressDialog = ProgressDialog.show(Login.this,"جاري تسجيل الدخول","Please wait...",false,false);
        progressDialog.show();
        String email=textInputEditTextemail.getText().toString();
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<contact_userinfo> call= apiinterface.getcontacts_login(email,
                textInputEditTextpassword.getText().toString());
        call.enqueue(new Callback<contact_userinfo>() {
            @Override
            public void onResponse(Call<contact_userinfo> call, Response<contact_userinfo> response) {
                progressDialog.dismiss();
                if (response.code() == 404) {
                    Toast.makeText(Login.this,"هناك خطأ فى الهاتف او الرقم السري ",Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }
                if(response.isSuccessful()){

                    contactList = response.body();
                    try {
                        progressDialog.dismiss();
                        edt.putInt("id",contactList.getId());
                        edt.putString("name",contactList.getName());
                        edt.putString("phone",contactList.getPhone());
                        edt.putString("address",contactList.getMaddress());
                        edt.putString("password",contactList.getPassword());
                        edt.putString("createdAt",contactList.getCreatedAt());
                        edt.putString("image","https://applicationme.com/fastcar/"+contactList.getImage());
                        edt.putInt("type",contactList.getUsertype());
                        edt.putString("user_type",contactList.getUser_type());
                        try{
                        if(contactList.getPhone_code().equals("966")){
                            edt.putInt("country_id",2);

                        }
                        else{
                            edt.putInt("country_id",1);
                        }}catch (Exception e){
                            edt.putInt("country_id",2);
                        }
                        if(contactList.getIs_busy().equals("busy")){
                            edt.putString("status", "مشغول");
                        }
                        else {
                            edt.putString("status", "متاح");
                        }
                        edt.putString("remember","yes");
                        edt.apply();

                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);
                        dlgAlert.setMessage("تم تسجيل الدخول بنجاح");
                        dlgAlert.setTitle("FastCar");
                        dlgAlert.setIcon(R.drawable.logo);
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        if(contactList.getUser_type().equals("client")){
                        startActivity(new Intent(Login.this,MainActivity.class));}
                        else{
                            startActivity(new Intent(Login.this,MainDriver.class));
                        }


                    }
                    catch (Exception e){
                        Toast.makeText(Login.this,"هناك خطأ فى الهاتف او الرقم السري /",Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<contact_userinfo> call, Throwable t) {
                Toast.makeText(Login.this,"هناك خطأ فى الهاتف او الرقم السري",Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat=location.getLatitude();
                                    lng=location.getLongitude();

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat=mLastLocation.getLatitude();
            lng=mLastLocation.getLongitude();

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();

            } else  if (location1 != null) {
                lat = location1.getLatitude();
                lng = location1.getLongitude();


            } else  if (location2 != null) {
                lat = location2.getLatitude();
                lng = location2.getLongitude();


            }else{

              //  Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
            if(lat==0){
             /*   String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 23.3728831, 85.3372199);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                Toast.makeText(Login.this,"قمنا بفتح جوجل ماب لتحديد موقعك الحالي",Toast.LENGTH_LONG).show();*/
                getLocation();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        //     startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}