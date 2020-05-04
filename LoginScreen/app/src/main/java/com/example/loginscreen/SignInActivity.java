package com.example.loginscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import androidx.appcompat.widget.Toolbar;

public class SignInActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mEmail,mPassword;

    CheckBox mCheckBox;
    ImageButton mSignin;
    Button mSignup;
    private FirebaseAuth mAuthIn;
    @Override
    protected void onStart() {
        super.onStart();
        mAuthIn=FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuthIn.getCurrentUser();
        //if user already logged in, take to MainActivity

        if(currentUser!=null)
        {
            Intent i = new Intent(SignInActivity.this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(0,0);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=findViewById(R.id.toolbar);

        mEmail = findViewById(R.id.emailPhone);
        mPassword = findViewById(R.id.password);
        mCheckBox = findViewById(R.id.checkbox);
        mSignin =findViewById(R.id.signin);
        mSignup =findViewById(R.id.signup);

        mSignin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(SignInActivity.this,"Sign In Button Clicked",Toast.LENGTH_LONG).show();
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
                mAuthIn.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //FirebaseUser user=mAuth.getCurrentUser();
                                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                                    overridePendingTransition(0,0);
                                }
                                else{
                                    mPassword.setError("Password invalid");
                                }
                            }
                        });
            }
        });
        mSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this,SignUpActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });
    }

}
