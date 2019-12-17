package com.example.castmeapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static io.opencensus.tags.TagValue.MAX_LENGTH;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar  postToolbar;
    private ImageView ivNewPost;
    private EditText etCaptionPost;
    private Button btnNewPost;
    private String UserId;

    private ProgressBar pbNewPost;

    private Uri ImageNewPostURI = null;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseFirestore firestore;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();

        ivNewPost = findViewById(R.id.ivNewPost);
        etCaptionPost = findViewById(R.id.etCaptionPost);
        btnNewPost = findViewById(R.id.btnNewPost);
        pbNewPost = findViewById(R.id.pbNewPost);
        postToolbar = findViewById(R.id.post_Toolbar);

        postToolbar.setTitle(getString(R.string.new_post));
        postToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        ivNewPost.setOnClickListener(this);
        btnNewPost.setOnClickListener(this);

        postToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ivNewPost:
                PickImages();
                break;

            case R.id.btnNewPost:

                final String caption = etCaptionPost.getText().toString();

                if (!TextUtils.isEmpty(caption) && ImageNewPostURI != null){

                    btnNewPost.setEnabled(false);
                    pbNewPost.setVisibility(View.VISIBLE);
                    final String randomImgId = UUID.randomUUID().toString();

                    final StorageReference imgPath = mStorageRef.child("post_images").child(randomImgId + ".jpg");

                    imgPath.putFile(ImageNewPostURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull final Task<Uri> task) {
                                    final String DownloadImgURI = task.getResult().toString();

                                    if (task.isSuccessful()){

                                        File newImagesFile = new File(ImageNewPostURI.getPath());
                                        try {

                                            compressedImageFile = new Compressor(NewPostActivity.this)
                                                    .compressToBitmap(newImagesFile);

                                        } catch (IOException e) {

                                            e.printStackTrace();

                                        }

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                                        byte[] imagesByte = baos.toByteArray();

                                        final UploadTask thumbImages = mStorageRef.child("post_images/thumbs")
                                                .child(randomImgId + ".jpg")
                                                .putBytes(imagesByte);

                                        thumbImages.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                Task<Uri> ThumbImgUri = taskSnapshot.getStorage().getDownloadUrl();
                                                String DownloadThumbImgURI = thumbImages.toString();

                                                Map<String, Object> postMap = new HashMap<>();
                                                postMap.put("image_url", DownloadImgURI);
                                                postMap.put("thumb_uri", DownloadThumbImgURI);
                                                postMap.put("caption", caption);
                                                postMap.put("user_id", UserId);
                                                postMap.put("timestamp", FieldValue.serverTimestamp());

                                                firestore.collection("Post").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if (task.isSuccessful()){

                                                            Toast.makeText(NewPostActivity.this, "Post was Added", Toast.LENGTH_LONG).show();
                                                            finish();

                                                        }
                                                        else {
                                                            String errorMsg = task.getException().getMessage();
                                                            Toast.makeText(NewPostActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                                                        }

                                                        btnNewPost.setEnabled(true);
                                                        pbNewPost.setVisibility(View.INVISIBLE);

                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                String errorMsg = e.getMessage().toString();
                                                Toast.makeText(NewPostActivity.this, errorMsg, Toast.LENGTH_LONG ).show();

                                            }
                                        });


                                    }
                                    else {

                                        String errorMsg = task.getException().getMessage();
                                        Toast.makeText(NewPostActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                                    }

                                    btnNewPost.setEnabled(true);
                                    pbNewPost.setVisibility(View.INVISIBLE);

                                }
                            });
                        }
                    });

                }
                else {

                    Toast.makeText(NewPostActivity.this, "Please fill empty column", Toast.LENGTH_LONG).show();

                }




        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){

                ImageNewPostURI = result.getUri();
                ivNewPost.setImageURI(ImageNewPostURI);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }

    private void PickImages() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .start(NewPostActivity.this);
    }

}
