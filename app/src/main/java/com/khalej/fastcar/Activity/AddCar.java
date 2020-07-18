package com.khalej.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.khalej.fastcar.R;
import com.khalej.fastcar.model.Apiclient_home;
import com.khalej.fastcar.model.apiinterface_home;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddCar extends AppCompatActivity {
    EditText brand,model,numberr,colorr;
    TextView carId,driveId,frontId,backId;
    AppCompatButton add;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    ProgressBar progressBar;
    private static final int MY_CAMERA_PERMISSION_CODE = 1;
    private  static final int IMAGEA = 100;
    private  static final int IMAGEB = 99;
    private  static final int IMAGED = 97;
    private  static final int IMAGEC= 98;
    private static final int CAMERA_REQUEST_A = 1;
    private static final int CAMERA_REQUEST_B = 2;
    private static final int CAMERA_REQUEST_C = 3;
    private static final int CAMERA_REQUEST_D = 4;
    String imagePathA,imagePathB,imagePathC,imagePathD;
    String mediaPathA,mediaPathB,mediaPathC,mediaPathD;
    private apiinterface_home apiinterface;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        this.setTitle("");
        brand=findViewById(R.id.brand);
        model=findViewById(R.id.model);
        numberr=findViewById(R.id.numberr);
        colorr=findViewById(R.id.colorr);
        carId=findViewById(R.id.carId);
        driveId=findViewById(R.id.driveId);
        frontId=findViewById(R.id.frontId);
        backId=findViewById(R.id.backId);
        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchInfo();
            }
        });
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
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(AddCar.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddCar.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        carId.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    showPictureDialogA();
                }
            }
        });
        driveId.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    showPictureDialogB();
                }
            }
        });
        frontId.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    showPictureDialogC();
                }
            }
        });
        backId.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    showPictureDialogD();
                }
            }
        });
    }
    private void showPictureDialogA(){
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
                                choosePhotoFromGallaryA();
                                break;
                            case 1:
                                takePhotoFromCameraA();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallaryA() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGEA);

    }

    private void takePhotoFromCameraA() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir     /* directory */
            );
            Uri uri = FileProvider.getUriForFile(AddCar.this, getPackageName(), image);
            imagePathA=image.getAbsolutePath();//Store this path as globe variable

            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_A);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    private void showPictureDialogB(){
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
                                choosePhotoFromGallaryB();
                                break;
                            case 1:
                                takePhotoFromCameraB();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallaryB() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGEB);

    }

    private void takePhotoFromCameraB() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir     /* directory */
            );
            Uri uri = FileProvider.getUriForFile(AddCar.this, getPackageName(), image);
            imagePathB=image.getAbsolutePath();//Store this path as globe variable

            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_B);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    private void showPictureDialogC(){
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
                                choosePhotoFromGallaryC();
                                break;
                            case 1:
                                takePhotoFromCameraC();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallaryC() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGEC);

    }

    private void takePhotoFromCameraC() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir     /* directory */
            );
            Uri uri = FileProvider.getUriForFile(AddCar.this, getPackageName(), image);
            imagePathC=image.getAbsolutePath();//Store this path as globe variable

            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_C);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    private void showPictureDialogD(){
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
                                choosePhotoFromGallaryD();
                                break;
                            case 1:
                                takePhotoFromCameraD();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallaryD() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGED);

    }

    private void takePhotoFromCameraD() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir     /* directory */
            );
            Uri uri = FileProvider.getUriForFile(AddCar.this, getPackageName(), image);
            imagePathD=image.getAbsolutePath();//Store this path as globe variable

            Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_REQUEST_D);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_A && resultCode == Activity.RESULT_OK)
        {

            mediaPathA = imagePathA;
            String name=random();

            mediaPathA=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathA  ,name);

            carId.setText(mediaPathA);

        }
        if(requestCode == IMAGEA && resultCode == RESULT_OK && null != data)
        {
            Uri pathImag = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(pathImag, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPathA = cursor.getString(columnIndex);
            // Toast.makeText(Registration.this,mediaPath,Toast.LENGTH_LONG).show();
            String namee=random();

            mediaPathA=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathA,namee);

            carId.setText(mediaPathA);
            cursor.close();
        }
        if (requestCode == CAMERA_REQUEST_B && resultCode == Activity.RESULT_OK)
        {

            mediaPathB = imagePathB;
            String name=random();

            mediaPathB=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathB  ,name);

            driveId.setText(mediaPathB);

        }
        if(requestCode == IMAGEB && resultCode == RESULT_OK && null != data)
        {
            Uri pathImag = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(pathImag, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPathB = cursor.getString(columnIndex);
            // Toast.makeText(Registration.this,mediaPath,Toast.LENGTH_LONG).show();
            String namee=random();

            mediaPathB=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathB,namee);

            driveId.setText(mediaPathB);
            cursor.close();
        }
        if (requestCode == CAMERA_REQUEST_C && resultCode == Activity.RESULT_OK)
        {

            mediaPathC = imagePathC;
            String name=random();

            mediaPathC=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathC  ,name);

            frontId.setText(mediaPathC);

        }
        if(requestCode == IMAGEC && resultCode == RESULT_OK && null != data)
        {
            Uri pathImag = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(pathImag, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPathC = cursor.getString(columnIndex);
            // Toast.makeText(Registration.this,mediaPath,Toast.LENGTH_LONG).show();
            String namee=random();

            mediaPathC=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathC,namee);

            frontId.setText(mediaPathC);
            cursor.close();
        }
        if (requestCode == CAMERA_REQUEST_D && resultCode == Activity.RESULT_OK)
        {

            mediaPathD = imagePathD;
            String name=random();

            mediaPathD=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathD ,name);

            backId.setText(mediaPathD);

        }
        if(requestCode == IMAGED && resultCode == RESULT_OK && null != data)
        {
            Uri pathImag = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(pathImag, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPathD = cursor.getString(columnIndex);
            // Toast.makeText(Registration.this,mediaPath,Toast.LENGTH_LONG).show();
            String namee=random();

            mediaPathD=resizeAndCompressImageBeforeSend(AddCar.this,mediaPathD,namee);

            backId.setText(mediaPathD);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


    public void fetchInfo() {
        progressDialog = ProgressDialog.show(AddCar.this, "جاري حفظ بيانات السيارة", "Please wait...", false, false);
        progressDialog.show();
        String image="";
        File fileA = null;
        try{
            fileA = new File(mediaPathA);}
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره رخصة السيارة",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        MultipartBody.Part fileToUploadA  = null;
        try {
            // Parsing any Media type file
            RequestBody requestBodyId = RequestBody.create(MediaType.parse("*/*"), fileA);
            fileToUploadA = MultipartBody.Part.createFormData("licence_plate1", fileA.getName(), requestBodyId);
        }
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره رخصة السيارة",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        File fileB = null;
        try{
            fileB = new File(mediaPathB);}
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره رخصة القيادة",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        MultipartBody.Part fileToUploadB  = null;
        try {
            // Parsing any Media type file
            RequestBody requestBodyId = RequestBody.create(MediaType.parse("*/*"), fileB);
            fileToUploadB = MultipartBody.Part.createFormData("licence_plate2", fileB.getName(), requestBodyId);
        }
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره رخصة القيادة",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

        File fileC = null;
        try{
            fileC = new File(mediaPathC);}
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره البطاقة من الأمام",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        MultipartBody.Part fileToUploadC = null;
        try {
            // Parsing any Media type file
            RequestBody requestBodyId = RequestBody.create(MediaType.parse("*/*"), fileC);
            fileToUploadC = MultipartBody.Part.createFormData("licence_plate3", fileC.getName(), requestBodyId);
        }
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره البطاقة من الأمام",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        File fileD = null;
        try{
            fileD = new File(mediaPathD);}
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره البطاقة من الخلف",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        MultipartBody.Part fileToUploadD = null;
        try {
            // Parsing any Media type file
            RequestBody requestBodyId = RequestBody.create(MediaType.parse("*/*"), fileD);
            fileToUploadD = MultipartBody.Part.createFormData("licence_plate4", fileD.getName(), requestBodyId);
        }
        catch (Exception e){
            Toast.makeText(AddCar.this,"من فضلك قم بتحديد صوره البطاقة من الخلف",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        RequestBody modell = RequestBody.create(MediaType.parse("text/plain"),model.getText().toString());
        RequestBody numberrr=RequestBody.create(MediaType.parse("text/plain"),numberr.getText().toString());
        RequestBody brandd=RequestBody.create(MediaType.parse("text/plain"),brand.getText().toString());
        RequestBody colorrr=RequestBody.create(MediaType.parse("text/plain"),colorr.getText().toString());
        RequestBody idd=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sharedpref.getInt("id",0)));
        apiinterface = Apiclient_home.getapiClient().create(apiinterface_home.class);
        Call<ResponseBody> call = apiinterface.getcontacts_addCAr(fileToUploadA,fileToUploadB,fileToUploadC
                ,fileToUploadD,modell,colorrr,brandd,numberrr,idd
        );
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
                    //Toast.makeText(AddCar.this,jObjError.toString(),Toast.LENGTH_LONG).show();
                    Toast.makeText(AddCar.this,"هناك بيانات مستخدمة من قبل  أو تأكد من انك ادخلت البيانات بشكل صحيح",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                progressDialog.dismiss();

                try{
                    progressDialog.dismiss();

                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(AddCar.this);
                    dlgAlert.setMessage("تم أضافة السيارة بنجاح");
                    dlgAlert.setTitle("Fast Car");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    startActivity(new Intent(AddCar.this,MainDriver.class));
                    finish();
                  }
                catch (Exception e){
                    Toast.makeText(AddCar.this, "هناك خطأ حدث الرجاء المحاولة مرة اخري ", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}
