package com.example.loginscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.widget.Toolbar;

public class SignInActivity extends AppCompatActivity {

    Toolbar mToolbar;
    EditText mEmail, mPassword;
    TextView mForgotPassword;
    CheckBox mCheckBox;
    Button mSignin;
    TextView mSignup;
    ImageButton googleSignIn,phoneSignIn;

    //1. Declare an Instance of Firebase Auth
    private FirebaseAuth mAuthIn;

    //3. Check Current Auth State.If user is already logged in, take to MainActivity
    //It is useful when user has not reinstalled the application
    @Override
    protected void onStart() {
        super.onStart();
        //Get Current user
        FirebaseUser currentUser = mAuthIn.getCurrentUser();

        //If user is not null, means already signed in then take to Main Activity
        if (currentUser != null) {
            Intent i = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mCheckBox = findViewById(R.id.checkbox);
        mSignin = findViewById(R.id.signin);
        mSignup = findViewById(R.id.signup);
        mForgotPassword = findViewById(R.id.forgotPassword);
        googleSignIn=findViewById(R.id.google_Sign_In);
        phoneSignIn=findViewById(R.id.phone_Sign_In);

        //2. Initialize Firebase auth instance
        mAuthIn = FirebaseAuth.getInstance();

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    mEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password required");
                    mPassword.requestFocus();
                    return;
                }
                //4. To sign in existing users. This will work in case user re-installs the
                //application.
                mAuthIn.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    overridePendingTransition(0, 0);
                                } else {
                                    mPassword.setError("Password invalid");
                                    mPassword.requestFocus();
                                }
                            }
                        });
            }
        });

        //Take to sign up screen
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        //To get a reset password mail
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Reset Password")
                        .setMessage("Enter you email to receive reset link")
                        .setView(resetMail)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String mail = resetMail.getText().toString().trim();
                                mAuthIn.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignInActivity.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignInActivity.this, "Error! Reset Link not sent " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }

    //Used when logout was clicked and it returned here
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
