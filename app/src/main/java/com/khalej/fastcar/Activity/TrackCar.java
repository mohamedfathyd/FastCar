package com.khalej.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.khalej.fastcar.Adapter.RecyclerAdapter_notification;
import com.khalej.fastcar.R;
import com.khalej.fastcar.directionhelpers.FetchURL;
import com.khalej.fastcar.directionhelpers.TaskLoadedCallback;
import com.khalej.fastcar.model.Apiclient_home;

import com.khalej.fastcar.model.Track;
import com.khalej.fastcar.model.apiinterface_home;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackCar   extends AppCompatActivity
        implements OnMapReadyCallback , TaskLoadedCallback {
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    private GoogleMap mMap;
    LinearLayout details;
    AppCompatButton accept;
   Intent i;
   double latfrom,lngfrom,latto,lngto;
   double carlat=0.0;
   double carlng=0.0;
   int driver_id;
   int x=0;
    TextView name,price,space;
    ImageView logout;
    private apiinterface_home apiinterface;
    private MarkerOptions place1, place2;
    List<Track> tracks=new ArrayList<>();
    int order_id;

    Marker currentMarker = null;
    private Polyline currentPolyline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        this.setTitle("");
        //   progressBar=(ProgressBar)findViewById(R.id.progressBar_subject);
        // progressBar.setVisibility(View.VISIBLE);
        StyleableToast.makeText(this, "أنتظر قليلا حتي يقبل السائق الطلب    ", R.style.mytoast).show();
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        i=getIntent();
        latfrom=i.getDoubleExtra("latfrom",0.0);
        lngfrom=i.getDoubleExtra("lngfrom",0.0);
        latto=i.getDoubleExtra("latto",0.0);
        lngto=i.getDoubleExtra("lngto",0.0);
        order_id=i.getIntExtra("order_id",0);
        driver_id=i.getIntExtra("driver_id",0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        accept=findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code here to show dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TrackCar.this);
                builder1.setMessage("هل وصلت الى وجهتك بنجاح ؟");
                builder1.setCancelable(true);
                builder1.setIcon(R.drawable.logo);
                builder1.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finishOrder();
                            }
                        });

                builder1.setNegativeButton(
                        "في الطريق",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
              fetchget_track();
                ha.postDelayed(this, 8000);
            }
        }, 8000);


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
            Toast.makeText(TrackCar.this,"من فضلك تأكد ان الخريطة تحديد الموقع يعمل بشكل جيد عن طريق فتح جوجل ماب وغلقه لكي يمكننا تحديد موقعك الحالي", Toast.LENGTH_LONG).show();

        }
        else{
            sydney = new LatLng(latfrom,lngfrom);
        }
        // mMap.addMarker(new MarkerOptions().position(sydney).title("HandMade"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        CameraUpdate location= CameraUpdateFactory.newLatLngZoom(sydney,18);
        place1 = new MarkerOptions().position(new LatLng(latfrom,lngfrom)).title("Start Location");
        place2 = new MarkerOptions().position(new LatLng(latto,lngto)).title("Destination");
        new FetchURL(TrackCar.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
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
        Call<List<Track>> call = apiinterface.getcontacts_Track(driver_id,order_id);
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
                        Marker marker=null;
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
                      //Toast.makeText(TrackCar.this,e+"",Toast.LENGTH_LONG).show();
                    tracks=new ArrayList<>();
                }

            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
               // Toast.makeText(TrackCar.this,t+"",Toast.LENGTH_LONG).show();

               
            }
        });
    }
   /* public void fetchget_Addtrack(){
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<ResponseBody> call = apiinterface.getcontacts_AddTrack();
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
                    //Toast.makeText(Register_Fani.this,jObjError.toString(),Toast.LENGTH_LONG).show();

                    return;
                }

                try{

                }
                catch (Exception e){

                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    } */
   @Override
   public void onBackPressed()
   {
       // code here to show dialog
       AlertDialog.Builder builder1 = new AlertDialog.Builder(TrackCar.this);
       builder1.setMessage("هل تريد ألغاء متابعة الرحلة بالفعل؟ - لن يمكنك العودة مره اخري");
       builder1.setCancelable(true);
       builder1.setIcon(R.drawable.logo);
       builder1.setPositiveButton(
               "نعم",
               new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                      finish();
                   }
               });

       builder1.setNegativeButton(
               "أبقي هنا",
               new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                   }
               });

       AlertDialog alert11 = builder1.create();
       alert11.show();
     // optional depending on your needs
   }
    public void finishOrder(){
        progressDialog = ProgressDialog.show(TrackCar.this, "جاري أنهاء الرحلة", "Please wait...", false, false);
        progressDialog.show();

        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);

        Call<ResponseBody> call = apiinterface.getcontacts_finishOrder(sharedpref.getInt("id",0),order_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(TrackCar.this);
                dlgAlert.setMessage("تم أنهاء الرحلة بنجاح .... شكرا على ثقتك بنا");
                dlgAlert.setTitle("FastCar");
                dlgAlert.setIcon(R.drawable.logo);
                dlgAlert.setPositiveButton("حسناً", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}