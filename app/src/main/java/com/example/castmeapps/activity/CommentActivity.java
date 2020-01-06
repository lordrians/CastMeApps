package com.example.castmeapps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.castmeapps.R;
import com.example.castmeapps.object.Posting;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView rvComment;
    private ArrayList<Posting> listComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        listComment = new ArrayList<>();

        rvComment = findViewById(R.id.rv_comment);

        rvComment.setLayoutManager(new LinearLayoutManager(this));
//        rvComment.setAdapter();

    }
}
