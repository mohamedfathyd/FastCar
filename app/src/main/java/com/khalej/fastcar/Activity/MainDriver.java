package com.khalej.fastcar.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.khalej.fastcar.LocationTrack;
import com.khalej.fastcar.R;
import com.khalej.fastcar.directionhelpers.FetchURL;
import com.khalej.fastcar.directionhelpers.TaskLoadedCallback;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.Edit;
import com.khalej.fastcar.model.Order;
import com.khalej.fastcar.model.Status;
import com.khalej.fastcar.model.Track;
import com.khalej.fastcar.model.apiinterface_home;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainDriver  extends AppCompatActivity
       implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback , TaskLoadedCallback {
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    ProgressBar progressBar;
    private GoogleMap mMap;
    private apiinterface_home apiinterface;
    LinearLayout details;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    AppCompatButton accept;
    double latfrom=0,latto=0,lngfrom,lngto;
    Edit userData = new Edit();
    Status statusdata;
    CircleImageView userImage;
    TextView name,price,phone,from,to;
    DrawerLayout drawer;
    LinearLayout profile,notification,myorders,whous,callus,terms,logout,subscribe;
    ImageView sideMenu;
    Order tracks;
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
    int order_id;
    TextView status;
    Button changestatus;
    String lang;
    Intent intent;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        lang=sharedpref.getString("language","").trim();
        if(lang.equals(null)){
            edt.putString("language","ar");
            lang="en";
            edt.apply();
        }
        intent=getIntent();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        setContentView(R.layout.activity_main_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        this.setTitle("");
     //   progressBar=(ProgressBar)findViewById(R.id.progressBar_subject);
       // progressBar.setVisibility(View.VISIBLE);

        token=  FirebaseInstanceId.getInstance().getToken();
        Log.i("t", "onTokenRefresh completed with token: " + token);

        edt.putString("token",token);
        edt.apply();

        setToken();
        details=findViewById(R.id.details);
        details.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        sideMenu=findViewById(R.id.sidemenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    drawer.openDrawer(Gravity.LEFT);
                }catch(Exception e){
                    drawer.openDrawer(Gravity.RIGHT);
                }

            }
        });
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();

        View header = navigationView.getHeaderView(0);
        profile= header.findViewById(R.id.profile);
        notification=header.findViewById(R.id.notification);
        myorders=header.findViewById(R.id.myorders);

        callus=header.findViewById(R.id.callus);
        changestatus=header.findViewById(R.id.changeStatus);
        status=header.findViewById(R.id.status);
        status.setText("متاح");
        logout=header.findViewById(R.id.logout);
        name=header.findViewById(R.id.name);
        userImage=header.findViewById(R.id.image);
        name.setText(sharedpref.getString("name",""));
        status.setText(sharedpref.getString("status",""));
        Glide.with(this).load(sharedpref.getString("image","")).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ww).into(userImage);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDriver.this,UserProfile.class));
            }
        });
        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDriver.this,Rides.class));
            }
        });

        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDriver.this,Mores.class));
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDriver.this,Notification.class));
            }
        });

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
                startActivity(new Intent(MainDriver.this, Login.class));
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        phone=findViewById(R.id.phone);
        accept=findViewById(R.id.accept);
        fetchget_lastOrder();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        locationTrack = new LocationTrack(MainDriver.this);
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
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                   updatelatlng();
                }
                ha.postDelayed(this, 8000);
            }
        }, 8000);

     accept.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent =new Intent(MainDriver.this,TrackCar_driver.class);
             intent.putExtra("latfrom",latfrom);
             intent.putExtra("lngfrom",lngfrom);
             intent.putExtra("latto",latto);
             intent.putExtra("lngto",lngto);
             intent.putExtra("order_id",order_id);
             startActivity(intent);

         }
     });
     changestatus.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             changeStatus();
         }
     });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone.getText().toString()));
                startActivity(intent);
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try{
            boolean success =googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map));
            if(success){}
        }catch (Exception e){}
        // Add a marker in Sydney and move the camera
        LatLng sydney;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        if(Double.valueOf(sharedpref.getString("lat",""))==0){
            sydney = new LatLng(23.6587778, 43.0343392);
            Toast.makeText(MainDriver.this,"من فضلك تأكد ان الخريطة تحديد الموقع يعمل بشكل جيد عن طريق فتح جوجل ماب وغلقه لكي يمكننا تحديد موقعك الحالي", Toast.LENGTH_LONG).show();

        }
        else{
            sydney = new LatLng(Double.valueOf(sharedpref.getString("lat","")), Double.valueOf(sharedpref.getString("lng","")));
        }
        // mMap.addMarker(new MarkerOptions().position(sydney).title("HandMade"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
  if(latfrom!=0) {
      place1 = new MarkerOptions().position(new LatLng(latfrom, lngfrom)).title("My Location");
      place2 = new MarkerOptions().position(new LatLng(latto, lngto)).title("Destination");
      new FetchURL(MainDriver.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
  }
        CameraUpdate location= CameraUpdateFactory.newLatLngZoom(sydney,15);
        mMap.animateCamera(location);
    }

    public void fetchget_lastOrder(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<Order> call = apiinterface.getcontacts_lastOrder(sharedpref.getInt("id",0));
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {

                try {


                    tracks = response.body();
                    if(tracks.getUser_name()==null||tracks.getUser_name().equals("")){
                        return;
                    }
                    details.setVisibility(View.VISIBLE);
                        //  Toast.makeText(ChatActivity.this, "22", Toast.LENGTH_LONG).show();
                       phone.setText(tracks.getUser_phone());

                       name.setText(tracks.getUser_name());
                       from.setText(tracks.getFrom_address());
                       to.setText(tracks.getTo_address());
                    if(sharedpref.getInt("country_id",0)==1){
                        price.setText(tracks.getTotal_cost()+"EGP");}
                    else{
                        price.setText(tracks.getTotal_cost()+"SAR");
                    }
                       latfrom=tracks.getFrom_latitude();
                       lngfrom=tracks.getFrom_longitude();
                       latto=tracks.getTo_latitude();
                       lngto=tracks.getTo_longitude();
                       order_id=tracks.getId();
                    place1 = new MarkerOptions().position(new LatLng(latfrom, lngfrom)).title("My Location");
                    place2 = new MarkerOptions().position(new LatLng(latto, lngto)).title("Destination");
                    new FetchURL(MainDriver.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                    CameraUpdate location= CameraUpdateFactory.newLatLngZoom(new LatLng(latfrom, lngfrom),15);
                    mMap.animateCamera(location);
                }
                catch (Exception e){
                    //  Toast.makeText(ChatActivity.this,e+"",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                // Toast.makeText(MainActivity.this,t+"",Toast.LENGTH_LONG).show();


            }
        });
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +"AIzaSyAmhHLBk_IDyZ6MKjv3kmk9Y1VtZzDV-3w";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainDriver.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainDriver.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainDriver.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
            if(lat!=0){
               // updatelatlng();
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
    public void updatelatlng(){
        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<Edit> call = apiinterface.getcontacts_updatelatlng(lat,lng,sharedpref.getInt("id",0));
        call.enqueue(new Callback<Edit>() {
            @Override
            public void onResponse(Call<Edit> call, Response<Edit> response) {
                if (response.code() == 422) {
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   //  Toast.makeText(MainDriver.this,jObjError.toString(),Toast.LENGTH_LONG).show();

                    return;
                }
                userData=response.body();
                edt.putString("lat", String.valueOf(lat));
                edt.putString("lng", String.valueOf(lng));
               // Toast.makeText(MainDriver.this,String.valueOf(lat),Toast.LENGTH_LONG).show();
                edt.apply();

            }

            @Override
            public void onFailure(Call<Edit> call, Throwable t) {
            }
        });
    }
    public void changeStatus(){
        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
        String a="";
        if(status.getText().equals("متاح")){
            a="busy";
        }
        else{
            a="not_busy";
        }
        Call<Status> call = apiinterface.getcontacts_updatebusy(sharedpref.getInt("id",0),a);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.code() == 422) {
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                  //    Toast.makeText(MainDriver.this,jObjError.toString(),Toast.LENGTH_LONG).show();

                    return;
                }
                statusdata=response.body();
                if(statusdata.getIs_busy().equals("busy")){
                edt.putString("status", "مشغول");
                status.setText("مشغول");
                }
               else {
                    edt.putString("status", "متاح");
                    status.setText("متاح");
                }
               //   Toast.makeText(MainDriver.this,statusdata.getIs_busy(),Toast.LENGTH_LONG).show();
                edt.apply();

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
            }
        });
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
