package com.example.castmeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private ProgressBar pbLogin;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterConfirmPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegisterNow);
        pbLogin = findViewById(R.id.pbRegister);


        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            sendToMainActivity();
        }
        else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String LoginEmail = etEmail.getText().toString();
                String LoginPass = etPassword.getText().toString();

                if (!TextUtils.isEmpty(LoginEmail) && !TextUtils.isEmpty(LoginPass)){
                    pbLogin.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(LoginEmail, LoginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendToMainActivity();
                            }
                            else {
                                String ErrorMsg = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : "+ ErrorMsg, Toast.LENGTH_LONG).show();
                            }
                            pbLogin.setVisibility(View.INVISIBLE);
                        }

                    });

                }
                else {
                    Toast.makeText(LoginActivity.this, "Please input your email & password", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnRegisterNow:
                sendToRegisterActivity();
        }
    }



    private void sendToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
