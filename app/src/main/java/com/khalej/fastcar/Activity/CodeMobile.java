package com.khalej.fastcar.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import com.khalej.fastcar.R;
import com.khalej.fastcar.model.Reset;
import com.khalej.fastcar.model.apiinterface_home;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class CodeMobile extends AppCompatActivity {
TextInputEditText textInputEditTextphone;
AppCompatButton appCompatButtonRegisterservcies;
    ProgressDialog progressDialog;
    private apiinterface_home apiinterface;
   Reset reset;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId,code;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog1;
    EditText num;  Dialog dialog;
    Intent i;
    String codee="966";
    CountryCodePicker ccp;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        setContentView(R.layout.activity_code_mobile);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Droid.ttf", true);
        mAuth=FirebaseAuth.getInstance();
        ccp = findViewById(R.id.ccp);
        codee = ccp.getSelectedCountryCode();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                codee = ccp.getSelectedCountryCode();
            }
        });
        i=getIntent();
        mAuth=FirebaseAuth.getInstance();
        textInputEditTextphone=findViewById(R.id.textInputEditTextphone);
        appCompatButtonRegisterservcies=findViewById(R.id.appCompatButtonRegisterservcies);
        appCompatButtonRegisterservcies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(Registration.this,"a",Toast.LENGTH_LONG).show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+"+codee+textInputEditTextphone.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        CodeMobile.this,               // Activity (for callback binding)
                        mCallbacks);

                progressDialog1 = ProgressDialog.show(CodeMobile.this, "انتظر قليلا للتاكد من صحة البيانات", "Please wait...", false, false);
                progressDialog1.show();

                //  fetchInfo();
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(final String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                progressDialog1.dismiss();
                dialog = new Dialog(CodeMobile.this);
                dialog.setContentView(R.layout.dialog_code);
                dialog.getWindow().setLayout( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                Button confim;

                confim=dialog.findViewById(R.id.cunti);
// Set up the buttons
                OtpTextView otpTextView;
                otpTextView = dialog.findViewById(R.id.otp_view);
                otpTextView.setOtpListener(new OTPListener() {
                    @Override
                    public void onInteractionListener() {
                        // fired when user types something in the Otpbox
                    }
                    @Override
                    public void onOTPComplete(String otp) {
                     code=otp;
                    }
                });
                confim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVerificationId = verificationId;
                        verifyVerificationCode(code);

                    }
                });



                dialog.show();


                // Save verification ID and resending token so we can use them later

                // ...
            }
        };


    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(CodeMobile.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(CodeMobile.this,Register.class);
                            intent.putExtra("phone",textInputEditTextphone.getText().toString());
                            intent.putExtra("phone_code",codee);
                            intent.putExtra("type",i.getStringExtra("type"));
                            startActivity(intent);


                        } else {
                            Toast.makeText(CodeMobile.this,"كود التاكيد خاطئ",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
