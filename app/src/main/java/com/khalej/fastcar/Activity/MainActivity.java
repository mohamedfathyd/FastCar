package com.khalej.fastcar.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.khalej.fastcar.Adapter.RecyclerAdapter_rides;
import com.khalej.fastcar.R;
import com.khalej.fastcar.directionhelpers.FetchURL;
import com.khalej.fastcar.directionhelpers.TaskLoadedCallback;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.CarDistance;
import com.khalej.fastcar.model.My_Order;
import com.khalej.fastcar.model.NearestCar;
import com.khalej.fastcar.model.Order;
import com.khalej.fastcar.model.apiinterface_home;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback ,
        bottomsheet_fragment.ItemClickListener,
        bottomsheet_driverData_fragment.ItemClickListener,
        bottomsheet_driver_fragment.ItemClickListener, TaskLoadedCallback, PopupMenu.OnMenuItemClickListener {
ImageView sideMenu;
ProgressDialog progressDialog;
    DrawerLayout drawer;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    LatLng latLngValue = null;
    TextView address;
    LinearLayout profile,notification,myorders,whous,callus,terms,logout,subscribe;
    TextView name;
    CircleImageView userImage;
    double lat,lng;
    private GoogleMap mMap;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    double arrival_time;
    String distance;
    double totalprice;
    double totaltime;
    String lang;
    String driverimage;
    String carnumber;
    int driverId;
    String driverName;
    String driverphone;
    ImageView options;
    String carModel;
    Intent intent;
    String addressFrom,addressTo;
    Order order;
    CarDistance carDistance;
    NearestCar nearestCar;
    LinearLayout aa;
    private apiinterface_home apiinterface;
    SearchView searchView;
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

        setContentView(R.layout.activity_main);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);

        sideMenu=findViewById(R.id.sidemenu);
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
        View header = navigationView.getHeaderView(0);
        profile= header.findViewById(R.id.profile);
        notification=header.findViewById(R.id.notification);
        myorders=header.findViewById(R.id.myorders);
        options=findViewById(R.id.options);
        callus=header.findViewById(R.id.callus);

        logout=header.findViewById(R.id.logout);
        name=header.findViewById(R.id.name);
        userImage=header.findViewById(R.id.image);
        name.setText(sharedpref.getString("name",""));
        aa=header.findViewById(R.id.aa);
        aa.setVisibility(View.GONE);
        Glide.with(this).load(sharedpref.getString("image","")).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ww).into(userImage);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UserProfile.class));
            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this , view);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.menu_bar);
                popup.show();
            }
        });
        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Rides.class));
            }
        });

        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Mores.class));
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Notification.class));
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
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });
        searchView=findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String Location=searchView.getQuery().toString();
                List<Address>addressList=null;
                if(Location !=null||!Location.equals("")){
                    Geocoder geocoder=new Geocoder(MainActivity.this);
                    try{
                        addressList=geocoder.getFromLocationName(Location,1);
                    }catch (Exception e){}
                }
                Address addresss =null;
                try{
                    addresss =addressList.get(0);}
                catch (Exception e){}
                try{
                LatLng latLng= new LatLng(addresss.getLatitude(),addresss.getLongitude());
                CameraUpdate location= CameraUpdateFactory.newLatLngZoom(latLng,18);
                mMap.animateCamera(location); Geocoder geocoder;
                List<Address> addresses = null;

                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {

                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                latLngValue=latLng;
                }
                catch (Exception e){
 Toast.makeText(MainActivity.this,"يمكنك تحريك الخريطه لتحديد المكان الذي تريد التوجه أليه" ,Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
            Toast.makeText(MainActivity.this,"من فضلك تأكد ان الخريطة تحديد الموقع يعمل بشكل جيد عن طريق فتح جوجل ماب وغلقه لكي يمكننا تحديد موقعك الحالي", Toast.LENGTH_LONG).show();

        }
        else{
            sydney = new LatLng(Double.valueOf(sharedpref.getString("lat","")), Double.valueOf(sharedpref.getString("lng","")));
        }
        // mMap.addMarker(new MarkerOptions().position(sydney).title("HandMade"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraUpdate location= CameraUpdateFactory.newLatLngZoom(sydney,16);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("FastCar").icon(BitmapDescriptorFactory.fromResource(R.drawable.locationn)));
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                latLngValue=latLng;

                place1 = new MarkerOptions().position(new LatLng(Double.valueOf(sharedpref.getString("lat","")),
                        Double.valueOf(sharedpref.getString("lng","")))).title("My Location");
                place2 = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Destination");
                new FetchURL(MainActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
           lat=latLng.latitude;
           lng=latLng.longitude;
                fetchInfo();

            }
        });
        mMap.animateCamera(location);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void showBottomSheet() {
        Bundle bundle = new Bundle();
        bundle.putString("distance", distance );
        bundle.putDouble("totalprice",totalprice);
        bundle.putDouble("totaltime",totaltime);
        bottomsheet_fragment addPhotoBottomDialogFragment =
                bottomsheet_fragment.newInstance();
        addPhotoBottomDialogFragment.setArguments(bundle);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                bottomsheet_fragment.TAG);
    }
    public void showBottomSheetConfirm() {
        bottomsheet_driver_fragment addPhotoBottomDialogFragment =
                bottomsheet_driver_fragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                bottomsheet_driver_fragment.TAG);
    }
    public void showBottomSheetDriver() {
        Bundle bundle = new Bundle();
        bundle.putString("name", driverName );
        bundle.putString("image",driverimage);
        bundle.putString("phone",driverphone);
        bundle.putString("carNumber",carnumber);
        bundle.putString("carModel",carModel);
        bundle.putDouble("arrival_time",arrival_time);
        bottomsheet_driverData_fragment addPhotoBottomDialogFragment =
                bottomsheet_driverData_fragment.newInstance();
        addPhotoBottomDialogFragment.setArguments(bundle);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                bottomsheet_driverData_fragment.TAG);
    }
    @Override
    public void onItemClick(String item) {
         if(item.equals("test")){
             showBottomSheetConfirm();
         }
        if(item.equals("cash")){
            fetchInfo_near();
        }
        if(item.equals("credit")){
            payment();
        }
        if(item.equals("confirm")){
      fetchInfo_addOrder();
        }
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

    public void fetchInfo(){
        progressDialog = ProgressDialog.show(MainActivity.this, "جاري التحقق من المعلومات", "Please wait...", false, false);
        progressDialog.show();
        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<CarDistance> call = apiinterface.getcontacts_Distance(Double.valueOf(sharedpref.getString("lat","")),
                Double.valueOf(sharedpref.getString("lng","")),lat,lng,sharedpref.getInt("country_id",2));
        call.enqueue(new Callback<CarDistance>() {
            @Override
            public void onResponse(Call<CarDistance> call, Response<CarDistance> response) {
               progressDialog.dismiss();
                try {
                    carDistance = response.body();
                    distance=carDistance.getDistance();
                    totalprice=carDistance.getTotalPrice();
                    totaltime=(carDistance.getTotalTime()/60);
                    totalprice= aroundUp(totalprice,2);
                    totaltime= aroundUp(totaltime,2);
                    showBottomSheet();


                    }
                catch (Exception e){
                      //Toast.makeText(MainActivity.this,e+"",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<CarDistance> call, Throwable t) {
               // Toast.makeText(MainActivity.this,t+"",Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });
    }

    public void fetchInfo_near(){
        progressDialog = ProgressDialog.show(MainActivity.this, "جاري التحقق من المعلومات", "Please wait...", false, false);
        progressDialog.show();

        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<NearestCar> call = apiinterface.getcontacts_Nearest(Double.valueOf(sharedpref.getString("lat","")),
                Double.valueOf(sharedpref.getString("lng","")));
        call.enqueue(new Callback<NearestCar>() {
            @Override
            public void onResponse(Call<NearestCar> call, Response<NearestCar> response) {
       progressDialog.dismiss();
                try {
                    nearestCar = response.body();

                    driverimage=nearestCar.getLogo();
                    driverName=nearestCar.getName();
                    driverphone=""+nearestCar.getPhone();
                    try {


                    driverId=nearestCar.getCar().getDriver_id();
                    carnumber=nearestCar.getCar().getNumber();
                        arrival_time=(nearestCar.getArrival_time() /60);
                        arrival_time= aroundUp(arrival_time,2);
                    carModel=nearestCar.getCar().getModel();}
                    catch (Exception e){
                        driverId=1;
                    }

                    showBottomSheetDriver();
                }
                catch (Exception e){
                     //Toast.makeText(MainActivity.this,e+"",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<NearestCar> call, Throwable t) {
                //Toast.makeText(MainActivity.this,t+"",Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });
    }

    public void fetchInfo_addOrder(){
        progressDialog = ProgressDialog.show(MainActivity.this, "جاري تقديم الطلب ", "Please wait...", false, false);
        progressDialog.show();
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.valueOf(sharedpref.getString("lat","")),
                    Double.valueOf(sharedpref.getString("lng","")), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        addressFrom=addresses.get(0).getAddressLine(0);
        Geocoder geocoder2;
        List<Address> addresses2 = null;
        geocoder2 = new Geocoder(MainActivity.this, Locale.getDefault());

        try {
            addresses2 = geocoder2.getFromLocation(lat,lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        addressTo=addresses2.get(0).getAddressLine(0);

        apiinterface= Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<Order> call = apiinterface.content_addOrder(sharedpref.getInt("id",0),Double.valueOf(sharedpref.getString("lat","")),
                Double.valueOf(sharedpref.getString("lng","")),lat,lng,addressFrom,addressTo,totalprice,driverId);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
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
                    Toast.makeText(MainActivity.this,"هناك بيانات مستخدمة من قبل  أو تأكد من انك ادخلت البيانات بشكل صحيح",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                progressDialog.dismiss();

                try{
                    progressDialog.dismiss();
                       order=response.body();
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
                    dlgAlert.setMessage("تم الطلب بنجاح");
                    dlgAlert.setTitle("Fast Car");
                    dlgAlert.setIcon(R.drawable.logo);
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    Intent intent= new Intent(MainActivity.this,TrackCar.class);
                    intent.putExtra("latfrom",order.getFrom_latitude());
                    intent.putExtra("lngfrom",order.getFrom_longitude());
                    intent.putExtra("latto",order.getTo_latitude());
                    intent.putExtra("lngto",order.getTo_longitude());
                    intent.putExtra("driver_id",order.getDriver_id());
                    intent.putExtra("order_id",order.getId());
                    startActivity(intent);
                    dlgAlert.create().dismiss();
                    }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "هناك خطأ حدث الرجاء المحاولة مرة اخري ", Toast.LENGTH_LONG).show();

                }



            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                //Toast.makeText(MainActivity.this,t+"",Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        });
    }
    public static double aroundUp(double number, int canDecimal) {
        int cifras = (int) Math.pow(10, canDecimal);
        return Math.ceil(number * cifras) / cifras;
    }

    public void payment(){
        Intent in = new Intent(MainActivity.this, PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "admin@atlatalrowad.com"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY,"jtinHAWuiY8oTrhhY1wOr2PSeRAZKT04mis3SoybmGzg7Qy6S4Vj7LclCMknGls2DU6viEGFkgVs7tyZxujFfv0SilgYGI0jC6PV");//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE,PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Test Paytabs android library");
        in.putExtra(PaymentParams.AMOUNT,totalprice);

        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "009733");
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, "customer-email@example.com");
        in.putExtra(PaymentParams.ORDER_ID, "123456");
        in.putExtra(PaymentParams.PRODUCT_NAME,"FastCar");

//Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");

//Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
         //   fetchInfo(true);
            fetchInfo_near();
            Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.item2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
