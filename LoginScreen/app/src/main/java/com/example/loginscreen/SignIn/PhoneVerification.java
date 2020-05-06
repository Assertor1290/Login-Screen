package com.example.loginscreen.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.MainActivity;
import com.example.loginscreen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {

    private String mVerificationId;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private EditText mGetCode;
    Button mSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        mAuth=FirebaseAuth.getInstance();

        mSignIn=findViewById(R.id.buttonSignIn);
        mGetCode=findViewById(R.id.editTextCode);
        mProgressBar=findViewById(R.id.progressbar);
        String phoneNo=getIntent().getStringExtra("phoneNumber");
        sendVerificationCode(phoneNo);

        //have to click button in case auto detect doesnt work
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code=mGetCode.getText().toString().trim();
                if(TextUtils.isEmpty(code))
                {
                    mGetCode.setError("Enter code...");
                    mGetCode.requestFocus();
                    return;
                }
                if(code.length()<6)
                {
                    mGetCode.setError("Enter valid code....");
                    mGetCode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential  credential=PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(PhoneVerification.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
                else{
                    Toast.makeText(PhoneVerification.this,
                            Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number){
        mProgressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //This string has verification id
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId=s;
        }

        //in this method we can get code automatically
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //get code sent on phone. It will be null when auto detect fail
            //if auto detection works, no need to click on sign in button
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                mGetCode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneVerification.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
}
