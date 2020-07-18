package com.khalej.fastcar.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.khalej.fastcar.LocationTrack;
import com.khalej.fastcar.R;
import com.khalej.fastcar.directionhelpers.FetchURL;
import com.khalej.fastcar.directionhelpers.TaskLoadedCallback;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.Edit;
import com.khalej.fastcar.model.Track;
import com.khalej.fastcar.model.apiinterface_home;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackCar_driver extends AppCompatActivity
        implements OnMapReadyCallback , TaskLoadedCallback {
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    ProgressBar progressBar;
    private GoogleMap mMap,mMap2;
    LinearLayout details;
    AppCompatButton accept;
   Intent i;
   double latfrom,lngfrom,latto,lngto;
   double carlat=0.0;
   int x=0;
   double carlng=0.0;
    TextView name,price,space;
    ImageView logout;
    private apiinterface_home apiinterface;
    private MarkerOptions place1, place2;
    List<Track> tracks=new ArrayList<>();
    int order_id;
    private Polyline currentPolyline;
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
    List<Marker> markers=new ArrayList<>() ;
    Marker currentMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_car_d);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        this.setTitle("");
        //   progressBar=(ProgressBar)findViewById(R.id.progressBar_subject);
        // progressBar.setVisibility(View.VISIBLE);

        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        i=getIntent();
        latfrom=i.getDoubleExtra("latfrom",0.0);
        lngfrom=i.getDoubleExtra("lngfrom",0.0);
        latto=i.getDoubleExtra("latto",0.0);
        lngto=i.getDoubleExtra("lngto",0.0);
        order_id=i.getIntExtra("order_id",0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        accept=findViewById(R.id.accept);
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
              fetchget_track();
                ha.postDelayed(this, 8000);
            }
        }, 8000);
        final Handler haa=new Handler();
        haa.postDelayed(new Runnable() {

            @Override
            public void run() {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
                haa.postDelayed(this, 7000);
            }
        }, 7000);

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
      //  mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        if(Double.valueOf(sharedpref.getString("lat",""))==0){
            sydney = new LatLng(23.6587778, 43.0343392);
            Toast.makeText(TrackCar_driver.this,"من فضلك تأكد ان الخريطة تحديد الموقع يعمل بشكل جيد عن طريق فتح جوجل ماب وغلقه لكي يمكننا تحديد موقعك الحالي", Toast.LENGTH_LONG).show();

        }
        else{
            sydney = new LatLng(latfrom,lngfrom);
        }
        // mMap.addMarker(new MarkerOptions().position(sydney).title("HandMade"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraUpdate location= CameraUpdateFactory.newLatLngZoom(sydney,18);
        place1 = new MarkerOptions().position(new LatLng(latfrom,lngfrom)).title("Start Location");
        place2 = new MarkerOptions().position(new LatLng(latto,lngto)).title("Destination");
        new FetchURL(TrackCar_driver.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
   if(carlat!=0.0){
       mMap.clear();
       mMap.addMarker(new MarkerOptions().position(new LatLng(carlat,carlng)).title("FastCar").icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
   }
        mMap.animateCamera(location);
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
   public void fetchget_track(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<List<Track>> call = apiinterface.getcontacts_Track(sharedpref.getInt("id",0),order_id);
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {

                try {


                    tracks = response.body();
                    if (response.code() == 404) {
                        tracks=new ArrayList<>();
                        return;
                    }
                    if(tracks.isEmpty()){
                        tracks=new ArrayList<>();
                    }
                    else {
                        carlat=tracks.get(0).getLatitude();
                        carlng=tracks.get(0).getLongitude();

                        if(carlat!=0.0){

                            if (currentMarker!=null) {
                                currentMarker.remove();
                                currentMarker=null;
                            }

                            if (currentMarker==null) {
                                currentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(carlat, carlng)).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                            }
                           CameraUpdate location= CameraUpdateFactory.newLatLngZoom(new LatLng(carlat,carlng),18);

                            mMap.animateCamera(location);
                        }
                    }
                }
                catch (Exception e){
                    //  Toast.makeText(ChatActivity.this,e+"",Toast.LENGTH_LONG).show();
                    tracks=new ArrayList<>();
                }

            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
               // Toast.makeText(MainActivity.this,t+"",Toast.LENGTH_LONG).show();

               
            }
        });
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(TrackCar_driver.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (TrackCar_driver.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TrackCar_driver.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
                fetchget_Addtrack();
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

    public void fetchget_Addtrack(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<ResponseBody> call = apiinterface.getcontacts_AddTrack(sharedpref.getInt("id",0),order_id,lat,lng,1234);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 422) {
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                  //  Toast.makeText(TrackCar_driver.this,jObjError.toString(),Toast.LENGTH_LONG).show();

                    return;
                }

                try{
                 //   Toast.makeText(TrackCar_driver.this,"dsa",Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                 //   Toast.makeText(TrackCar_driver.this,e+"",Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        AlertDialog.Builder builder1 = new AlertDialog.Builder(TrackCar_driver.this);
        builder1.setMessage("يجب عليك البقاء هنا حتى نهاية الرحلة");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.logo);
        builder1.setPositiveButton(
                "حسنا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        builder1.setNegativeButton(
                "غادر",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        // optional depending on your needs
    }
}