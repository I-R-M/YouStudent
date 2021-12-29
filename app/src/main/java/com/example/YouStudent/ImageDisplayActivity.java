package com.example.YouStudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_display);
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference img = !Data.isShared ? reference.child(Data.getPath()+Data.imagename.split(".png")[0]) : reference.child(Data.getSharedPath()+Data.imagename.split(".png")[0]);
        img.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                ImageView imageView = findViewById( R.id.imageView2);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Data.switchActivity(ImageDisplayActivity.this,UserFolderActivity.class);
                Log.d("Image fail",e.getStackTrace()+"\n\n"+e.getMessage());
            }
        });

    }
}