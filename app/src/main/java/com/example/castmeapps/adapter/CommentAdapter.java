package com.example.castmeapps.adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.castmeapps.R;
import com.example.castmeapps.object.Comments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comments> listComment;
    private Context context;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    public CommentAdapter(ArrayList<Comments> listComment) {
        this.listComment = listComment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        context = parent.getContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder holder, int position) {


        final Comments comment = listComment.get(position);
        final String postId = listComment.get(position).PostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();
        long milisecond = listComment.get(position).getTimestamp().getTime();


        holder.tvCaptions.setText(comment.getComment_text());
        holder.tvDate.setText(convertTime(milisecond));

        String userId = comment.getUser_id();
        firestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    String name = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");
                    holder.tvUsername.setText(name);
                    Glide.with(context)
                            .load(userImage)
                            .apply(new RequestOptions().centerCrop())
                            .into(holder.userImage);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCaptions, tvDate;
        private CircleImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_item_comment_usermame);
            tvCaptions = itemView.findViewById(R.id.tv_item_comment_captions);
            tvDate = itemView.findViewById(R.id.tv_item_comment_date);
            userImage = itemView.findViewById(R.id.iv_item_comment_user);

        }
    }

    public static String convertTime(long milisecond){


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Long now = System.currentTimeMillis();

        String date = formatter.format(new Date(milisecond));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisecond);
        String ago = DateUtils.getRelativeTimeSpanString(milisecond, now, DateUtils.MINUTE_IN_MILLIS).toString();
//        return formatter.format(calendar.getTime());
        return ago;
    }
}
