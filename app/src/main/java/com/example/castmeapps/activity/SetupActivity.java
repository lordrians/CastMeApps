package com.example.castmeapps.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.castmeapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar setupToolbar;
    private CircleImageView setupProfileImg;
    private  Uri mainImageURI = null;
    private EditText etSetupName;
    private Button btnSaveSetting;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressBar pbSetup;
    private String userId;
    private boolean isChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupProfileImg = findViewById(R.id.ivProfileSetup);
        setupToolbar = findViewById(R.id.setup_toolbar);
        etSetupName = findViewById(R.id.etNameSetup);
        btnSaveSetting = findViewById(R.id.btnSaveSetting);
        pbSetup = findViewById(R.id.pbSetup);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.account_setting_text));

        setupProfileImg.setOnClickListener(this);
        btnSaveSetting.setOnClickListener(this);

        btnSaveSetting.setEnabled(false);
        pbSetup.setVisibility(View.VISIBLE);
        LoadData();



    }

    private void LoadData() {
        firestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");

                        etSetupName.setText(name);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.account);
                        Glide.with(SetupActivity.this)
                                .setDefaultRequestOptions(requestOptions)
                                .load(image)
                                .into(setupProfileImg);
                        btnSaveSetting.setEnabled(false);
                        pbSetup.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    String errorMsg = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Error = " + errorMsg, Toast.LENGTH_LONG).show();
                }
                btnSaveSetting.setEnabled(true);
                pbSetup.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivProfileSetup:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else {
                        PickImages();
                    }
                }
                else {
                    PickImages();
                }
                break;

            case R.id.btnSaveSetting:
                final String UserName = etSetupName.getText().toString();
                pbSetup.setVisibility(View.VISIBLE);
                btnSaveSetting.setEnabled(false);

                if (!TextUtils.isEmpty(UserName) && mainImageURI != null){

                    if (isChanged){

                        userId = FirebaseAuth.getInstance().getUid();
                        final StorageReference imgPath = storageReference.child("profile_images").child(userId + ".jpg");

                        imgPath.putFile(mainImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()){
                                            storeFullDtFirestore(task, UserName);

                                        }
                                        else {
                                            String errorMsg = task.getException().getMessage();
                                            Toast.makeText(SetupActivity.this, "Error = " + errorMsg, Toast.LENGTH_LONG).show();
                                        }
                                        pbSetup.setVisibility(View.INVISIBLE);
                                        btnSaveSetting.setEnabled(true);
                                    }
                                });
                            }
                        });

                    }

                }
                else {
                    storeFullDtFirestore(null, UserName);
                    pbSetup.setVisibility(View.INVISIBLE);
                    btnSaveSetting.setEnabled(true);
                }


                break;


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                mainImageURI = result.getUri();
                setupProfileImg.setImageURI(mainImageURI);
                isChanged = true;
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }

    private void storeFullDtFirestore(@NonNull final Task<Uri> task, String UserName) {
        Uri DownloadURI;
        String dumpImg = "";
        Map<String, Object> userMap = new HashMap<>();

        if (task != null){
            DownloadURI = task.getResult();
            userMap.put("name", UserName);
            userMap.put("image", DownloadURI.toString());
        }
        else {
            userMap.put("name", UserName);
            userMap.put("image", dumpImg);
        }

        firestore.collection("Users").document(userId)
                .set(userMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SetupActivity.this, "User information has been Updated", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetupActivity.this, "Error = " + e, Toast.LENGTH_LONG).show();
                    }
                });





    }


    private void PickImages() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(SetupActivity.this);
    }
}
