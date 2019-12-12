package com.example.castmeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mainToolbar;
    private FloatingActionButton fbAddMain;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbAddMain = findViewById(R.id.fbAddMain);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mainToolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        fbAddMain.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            userId = mAuth.getCurrentUser().getUid();
            firestore.collection("Users")
                    .document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();

                                if (documentSnapshot.exists()){

                                }
                                else {
                                    sendToSetup();
                                }

                            }

                        }

                    });

        }
        else {
            // No user is signed in
            sendToLogin();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout_btn:
                Logout();
                return true;

            case R.id.action_account_setting_btn:
                sendToSetup();
                return true;

            default:
                return false;
        }

    }

    private void Logout() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendToSetup() {
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }


    private void sendToNewPost() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fbAddMain:
                sendToNewPost();

                break;

        }

    }
}
