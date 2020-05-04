package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mEmail,mPassword,mName;
    CheckBox mCheckBox;
    ImageButton mSignup;
    Button mSignin;

    private FirebaseAuth mAuth;
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser=mAuth.getCurrentUser();
//        //if user already logged in, take to MainActivity
//
//        if(currentUser!=null)
//        {
//            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
//            overridePendingTransition(0,0);
//            finish();
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mToolbar = findViewById(R.id.toolbar);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mName = findViewById(R.id.name);
        mCheckBox =findViewById(R.id.checkbox);
        mSignup =findViewById(R.id.signup);
        mSignin =findViewById(R.id.signin);

        mAuth=FirebaseAuth.getInstance();

        mSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(SignUpActivity.this,"Sign Up Button Clicked",Toast.LENGTH_LONG).show();
                String email=mEmail.getText().toString();
                String password=mPassword.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password required");
                    return;
                }
                if(password.length()<6)
                {
                    mPassword.setError("Password must be >=6 characters");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this,"Successfully created",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            overridePendingTransition(0,0);
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        mSignin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,SignInActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
