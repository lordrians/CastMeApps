package com.example.castmeapps.adapter;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.castmeapps.R;
import com.example.castmeapps.fragment.HomeFragment;
import com.example.castmeapps.object.Posting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.ViewHolder> {

    private ArrayList<Posting> listPosting;
    private Context context;
    private FirebaseFirestore firestore;

    public PostingAdapter (ArrayList<Posting> listPosting){
        this.listPosting = listPosting;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        context = parent.getContext();
        firestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Posting posting = listPosting.get(position);

        holder.tvCaption.setText(posting.getCaption());
        Glide.with(context)
                .load(posting.getImage_url())
                .apply(new RequestOptions().centerCrop())
                .into(holder.ivPostImg);

        long milisecond = listPosting.get(position).getTimestamp().getTime();
        holder.tvDate.setText(convertTime(milisecond));


        String userId = posting.getUser_id();
        firestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    String name = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");
                    holder.tvUserid.setText(name);
                    Glide.with(context)
                            .load(userImage)
                            .apply(new RequestOptions().centerCrop())
                            .into(holder.userimage);

                }
                else {

                    String errorMsg = task.getException().getMessage();

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listPosting.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCaption, tvUserid, tvUserid2, tvDate;
        private View mView;
        private ImageView ivPostImg;
        private CircleImageView userimage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvCaption = itemView.findViewById(R.id.tv_item_caption);
            tvUserid = itemView.findViewById(R.id.tv_item_username);
            tvUserid2 = itemView.findViewById(R.id.tv_item_username2);
            ivPostImg = itemView.findViewById(R.id.iv_item_post);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            userimage = itemView.findViewById(R.id.iv_item_user);

        }
    }

    public static String convertTime(long milisecond){


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String date = formatter.format(new Date(milisecond));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisecond);
        return formatter.format(calendar.getTime());

    }
}
