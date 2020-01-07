package com.example.castmeapps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView rvComment;
    private ArrayList<Comments> listComment;
    private CommentAdapter commentAdapter;
    private boolean isFirstPageLoad = true;
    private boolean isNull = true;
//    private String postId;

    private CircleImageView userImageAddComment;
    private ImageView btnSend;
    private EditText etComment;

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
        final String postId = bundle.getString("postId");

        userImageAddComment = findViewById(R.id.iv_addcomment_user);
        btnSend = findViewById(R.id.btn_comment_send);
        etComment = findViewById(R.id.et_addcomment);

        listComment = new ArrayList<>();
        context = getApplicationContext();

        rvComment = findViewById(R.id.rv_comment);
        commentAdapter = new CommentAdapter(listComment);

        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setAdapter(commentAdapter);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String currentUserId = firebaseAuth.getCurrentUser().getUid();

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
                    isNull =false;
                }
            }
        });

        firestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    String userImage = task.getResult().getString("image");
                    String name = task.getResult().getString("name");
                    etComment.setHint("Comment as " + name);
                    Glide.with(context)
                            .load(userImage)
                            .apply(new RequestOptions().centerCrop())
                            .into(userImageAddComment);

                }
            }
        });


        if (isNull){
            Toast.makeText(this, postId, Toast.LENGTH_LONG).show();
            Query queryFirstLoad = firestore.collection("Post").document(postId).collection("Comments").orderBy("timestamp", Query.Direction.ASCENDING);
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



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String caption = etComment.getText().toString();
                final String currentUserId = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("comment_text", caption);
                commentMap.put("user_id",currentUserId);
                commentMap.put("timestamp", FieldValue.serverTimestamp());

                firestore.collection("Post/" + postId + "/Comments").add(commentMap);
                Toast.makeText(CommentActivity.this, "Komentar Terkirim", Toast.LENGTH_LONG).show();


            }
        });



    }




    private void loadMorePost(){


//        Query queryMoreComments = firestore.collection("Post")
//                .document(postId).collection("Comments")
//                .orderBy("timestamp", Query.Direction.ASCENDING)
//                .startAfter(lastComments)
//                .limit(5);
//
//
//        queryMoreComments.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                if (!queryDocumentSnapshots.isEmpty()) {
//
//                    lastComments = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
//                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
//
//                        if (doc.getType() == DocumentChange.Type.ADDED) {
//
//                            String postId = doc.getDocument().getId();
//
//                            Comments comments = doc.getDocument().toObject(Comments.class).withId(postId);
//                            listComment.add(comments);
//                            commentAdapter.notifyDataSetChanged();
//
//                        }
//
//                    }
//
//                }
//                else {
//
//                    Toast.makeText(context, "No more post", Toast.LENGTH_LONG).show();
//
//                }
//
//
//            }
//        });
    }
}
