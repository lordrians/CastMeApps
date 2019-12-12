package com.example.castmeapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar  postToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        postToolbar = findViewById(R.id.post_Toolbar);
        postToolbar.setTitle(getString(R.string.new_post));
        postToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        postToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {

    }
}
