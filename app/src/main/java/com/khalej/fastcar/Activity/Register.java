package com.khalej.fastcar.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import com.khalej.fastcar.LocationTrack;
import com.khalej.fastcar.R;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.apiinterface_home;
import com.khalej.fastcar.model.contact_userinfo;
import com.khalej.fastcar.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    TextInputEditText name,phone,email,password,confirmPassword,location;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    AppCompatButton regeister;
    private apiinterface_home apiinterface;
    private contact_userinfo contactList;
    ProgressDialog progressDialog;
    int countryId,categryId;
    LocationTrack locationTrack;
    CircleImageView image;
    CallbackManager callbackManager;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ACCESS_COARSE_LOCATION =2;
    double lat=0.0,lng=0.0;
    private  static final int IMAGEUser = 100;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final int REQUEST_LOCATION = 1;
    CheckBox Terms;
    Intent intent;
    TextView ShowTerms;
    user userData=new user();
    Spinner spin,spinCategory;
    private static final int MY_CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_REQUEST_User = 1;
    String mediaPath;
    String imagePath;
    int x;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId,code;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog1;
    EditText num;  Dialog dialog;

    String codee="966";
    CountryCodePicker ccp;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        setContentView(R.layout.activity_register);
        inisialize();
        mAuth=FirebaseAuth.getInstance();
        ccp = findViewById(R.id.ccp);
        codee = ccp.getSelectedCountryCode();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                codee = ccp.getSelectedCountryCode();
            }
        });
        mAuth=FirebaseAuth.getInstance();
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        intent=getIntent();
        phone.setText(intent.getStringExtra("phone"));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        locationTrack = new LocationTrack(Register.this);
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
            try{
            getLocation();}catch (Exception e){}
        }
        sharedpref = getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        spin=findViewById(R.id.spinCountry);
        edt.putString("lat", String.valueOf(lat));
        edt.putString("lng", String.valueOf(lng));
        edt.apply();
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        spinCategory=findViewById(R.id.spinCategory);

        image=findViewById(R.id.image);
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(Register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    showPictureDialog();
                }
            }
        });
        location.setText(addresses.get(0).getAddressLine(0));
        regeister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("") || name.getText().toString() == null) {

                    name.setError("أدخل اسم المستخدم");

                } else if (phone.getText().toString().equals("") || phone.getText().toString() == null) {

                    phone.setError("أدخل رقم الموبيل");

                } else if (password.getText().toString().equals("") || password.getText().toString() == null) {

                    password.setError("أدخل كلمة المرور");

                } else if (confirmPassword.getText().toString().equals("") || confirmPassword.getText().toString() == null) {

                    confirmPassword.setError("أدخل  تأكيد كلمة المرور");

                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    confirmPassword.setError("كلمة تأكيد مختلفة");

                    confirmPassword.setText("");
                }
//                else if(Terms.isChecked()==false){
//
//                    Toast.makeText(Register.this,"من فضلك قم بلموافقة على الشروط والأحكام",Toast.LENGTH_LONG).show();
//                }
                else {

                     fetchInfo();
                }
            }
        });

        ShowTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(Register.this,Terms.class));
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(Register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        x=0;
        startActivityForResult(galleryIntent, IMAGEUser);

    }

    private void takePhotoFromCamera() {
        x=1;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir     /* directory */
            );
            Uri uri = FileProvider.getUriForFile(Register.this, getPackageName(), image);
            imagePath=image.getAbsolutePath();//Store this path as globe variable

            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_User);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_User && resultCode == Activity.RESULT_OK)
        {

            mediaPath = imagePath;
            String name=random();

            mediaPath=resizeAndCompressImageBeforeSend(Register.this,mediaPath  ,name);

            image.setImageBitmap(BitmapFactory.decodeFile(mediaPath));

        }
        if(requestCode == IMAGEUser && resultCode == RESULT_OK && null != data)
        {
            Uri pathImag = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(pathImag, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);
            // Toast.makeText(Registration.this,mediaPath,Toast.LENGTH_LONG).show();
            String namee=random();

            mediaPath=resizeAndCompressImageBeforeSend(Register.this,mediaPath,namee);

            image.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            cursor.close();
        }
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    public static String resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName){
        final int MAX_IMAGE_SIZE = 300 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap","cacheDir: "+context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir()+fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return  context.getCacheDir()+fileName;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag,"image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag,"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }


    public void fetchInfo() {
        progressDialog = ProgressDialog.show(Register.this, "جاري انشاء الحساب", "Please wait...", false, false);
        progressDialog.show();
        String image="";
        File file = null;
        try{
         file = new File(mediaPath);}
        catch (Exception e){
            Toast.makeText(Register.this,"من فضلك قم بتحديد صوره شخصيه",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        MultipartBody.Part fileToUpload  = null;
        try {
            // Parsing any Media type file
            RequestBody requestBodyId = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("logo", file.getName(), requestBodyId);
        }
        catch (Exception e){
            Toast.makeText(Register.this,"من فضلك قم بتحديد صوره شخصيه",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        RequestBody nameR = RequestBody.create(MediaType.parse("text/plain"),name.getText().toString());
        RequestBody phoneR=RequestBody.create(MediaType.parse("text/plain"),phone.getText().toString());
        RequestBody emailR=RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody passwordR=RequestBody.create(MediaType.parse("text/plain"),password.getText().toString());
        RequestBody phone_code=RequestBody.create(MediaType.parse("text/plain"), intent.getStringExtra("phone_code"));
        RequestBody Type=RequestBody.create(MediaType.parse("text/plain"),intent.getStringExtra("type"));
        RequestBody gender=RequestBody.create(MediaType.parse("text/plain"), "male");
        RequestBody latt=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lat));
        RequestBody lnng=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lng));
        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<contact_userinfo> call = apiinterface.getcontacts_newaccountFani(fileToUpload,nameR,phoneR,emailR,passwordR,phone_code,
                Type,gender,latt,lnng
                );
        call.enqueue(new Callback<contact_userinfo>() {
            @Override
            public void onResponse(Call<contact_userinfo> call, Response<contact_userinfo> response) {
                if (response.code() == 422) {
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // Toast.makeText(Register.this,jObjError.toString(),Toast.LENGTH_LONG).show();
                    Toast.makeText(Register.this,"هناك بيانات مستخدمة من قبل  أو تأكد من انك ادخلت البيانات بشكل صحيح",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                progressDialog.dismiss();
                contactList = response.body();

                try{
                    progressDialog.dismiss();
                    edt.putInt("id",contactList.getId());
                    edt.putString("name",contactList.getName());
                    edt.putString("phone",contactList.getPhone());
                    edt.putString("address",contactList.getMaddress());
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
                    edt.putString("remember","yes");
                    edt.apply();

                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Register.this);
                    dlgAlert.setMessage("تم تسجيل الدخول بنجاح");
                    dlgAlert.setTitle("Fast Car");
                    dlgAlert.setIcon(R.drawable.logo);
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    if(contactList.getUser_type().equals("client")){
                    startActivity(new Intent(Register.this,MainActivity.class));}
                    else{
                        startActivity(new Intent(Register.this,AddCar.class));
                }}
                catch (Exception e){
                    Toast.makeText(Register.this, "هناك خطأ حدث الرجاء المحاولة مرة اخري ", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<contact_userinfo> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
    public void inisialize() {

        name = (TextInputEditText) findViewById(R.id.textInputEditTextname);
        phone = (TextInputEditText) findViewById(R.id.textInputEditTextphone);
        email = (TextInputEditText) findViewById(R.id.textInputEditTextemail);
        password = (TextInputEditText) findViewById(R.id.textInputEditTextpassword);
        location=findViewById(R.id.textInputEditTextLocation);
        Terms=findViewById(R.id.check);
        ShowTerms=findViewById(R.id.showterms);
        confirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmpassword);
        regeister = (AppCompatButton) findViewById(R.id.appCompatButtonRegisterservcies);

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
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                try {


                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST_User);
                }
                catch (Exception e){}
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
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
        if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Register.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
