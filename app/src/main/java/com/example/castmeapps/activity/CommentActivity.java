package com.example.castmeapps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.castmeapps.R;
import com.example.castmeapps.adapter.CommentAdapter;
import com.example.castmeapps.object.Comments;
import com.example.castmeapps.object.PostId;
import com.example.castmeapps.object.Posting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView rvComment;
    private ArrayList<Comments> listComment;
    private CommentAdapter commentAdapter;
    private boolean isFirstPageLoad = true;
    private boolean isNull = true;
    private String postId;

    private FirebaseAuth firebaseAuth;
    private PostId postIdClass;
    private Context context;

    private FirebaseFirestore firestore;
    private DocumentSnapshot lastComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Bundle bundle = getIntent().getExtras();
        postId = bundle.getString("postId");


        listComment = new ArrayList<>();
        context = getApplicationContext();

        commentAdapter = new CommentAdapter(listComment);
        rvComment = findViewById(R.id.rv_comment);

        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setAdapter(commentAdapter);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if (reachedBottom){
                    loadMorePost();
                }
                else {

                }

            }
        });


        firestore.collection("Post").document(postId).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (!task.getResult().isEmpty()){

                    isNull = false;

                }

            }
        });

        if (!isNull){
            Query queryFirstLoad = firestore.collection("Post").document(postId).collection("Comments").orderBy("timestamp", Query.Direction.ASCENDING).limit(5);
            queryFirstLoad.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (isFirstPageLoad){
                        lastComments = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() -1);
                    }

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String postId = doc.getDocument().getId();

                            Comments comments = doc.getDocument().toObject(Comments.class).withId(postId);

                            if (isFirstPageLoad) {
                                listComment.add(comments);
                            }
                            else {
                                listComment.add(0, comments);
                            }

                            commentAdapter.notifyDataSetChanged();

                        }

                    }

                    isFirstPageLoad = false;

                }
            });
        }



    }




    private void loadMorePost(){


        Query queryMoreComments = firestore.collection("Post")
                .document(postId).collection("Comments")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .startAfter(lastComments)
                .limit(5);


        queryMoreComments.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {

                    lastComments = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String postId = doc.getDocument().getId();

                            Comments comments = doc.getDocument().toObject(Comments.class).withId(postId);
                            listComment.add(comments);
                            commentAdapter.notifyDataSetChanged();

                        }

                    }

                }
                else {

                    Toast.makeText(context, "No more post", Toast.LENGTH_LONG).show();

                }


            }
        });
    }
}
