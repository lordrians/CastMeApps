package com.example.castmeapps.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.castmeapps.R;
import com.example.castmeapps.object.Comment;
import com.example.castmeapps.object.Posting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Posting> listComment;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCaption, tvDate;
        private CircleImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_item_comment_usermame);
            tvCaption = itemView.findViewById(R.id.tv_item_comment_caption);
            tvDate = itemView.findViewById(R.id.tv_item_comment_date);
            userImage = itemView.findViewById(R.id.iv_item_comment_user);

        }
    }
}
