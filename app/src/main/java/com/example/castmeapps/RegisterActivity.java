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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail, etPass, etConfPass;
    private Button btnRegister;
    private ProgressBar pbRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etRegisterEmail);
        etPass = findViewById(R.id.etRegisterPass);
        etConfPass = findViewById(R.id.etRegisterConfirmPass);
        btnRegister = findViewById(R.id.btnRegister);
        pbRegister = findViewById(R.id.pbRegister);


        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                String Email = etEmail.getText().toString();
                final String Pass = etPass.getText().toString();
                String ConfPass = etConfPass.getText().toString();

                if (!TextUtils.equals(Pass,ConfPass)){
                    etPass.getText().clear();
                    etConfPass.getText().clear();

                    Toast.makeText(RegisterActivity.this,"Your password not equal", Toast.LENGTH_LONG ).show();
                }
                else if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ConfPass)){
                    pbRegister.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Create Account Success", Toast.LENGTH_LONG ).show();
                                sendToSetup();
                            }
                            else {
                                String errorMsg = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            }
                            pbRegister.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                break;
        }
    }

    private void sendToSetup() {
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
        finish();
    }
}
