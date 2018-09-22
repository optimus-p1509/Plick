package com.google.parth.plick20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Parth on 4/11/2017.
 */

public class activity_camera extends Fragment {
    public Button cameraButton;
    public Button databaseButton;
    private static final int CAMERA_REQUEST = 1888;
    public ImageView cameraImageView;
    private Uri mImageUri = null;
    private StorageReference mStorageReference;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private Bitmap mbitmap;
    private byte[] dataBAOS;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_camera,container,false);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mStorageReference = FirebaseStorage.getInstance().getReference().child("photoUploads");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("photoUploads");

        cameraButton = (Button) rootView.findViewById(R.id.camera_Button);
        databaseButton = (Button) rootView.findViewById(R.id.database_Button);
        cameraImageView = (ImageView) rootView.findViewById(R.id.camera_ImageView);

        mProgress = new ProgressDialog(getContext());

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDatabase();
            }
        });
        return rootView;
    }

    private void sendToDatabase() {

        mProgress.setMessage("Adding To Database...");
        mProgress.show();
        StorageReference imagesRef = mStorageReference.child("filename" + new Date().getTime());

        UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Upload Failed",Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(),"Uploaded",Toast.LENGTH_LONG).show();
                @SuppressWarnings("VisibleForTests") Uri downloadURI = taskSnapshot.getDownloadUrl();
                DatabaseReference newImage = mDatabase.push();
                if (mAuth.getCurrentUser() != null) {
                    newImage.child("Name").setValue(mAuth.getCurrentUser().getEmail());
                    newImage.child("Image").setValue(downloadURI.toString());
                }
                mProgress.dismiss();
            }
    });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            mbitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (mbitmap != null  ) {
                mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            }
            dataBAOS = baos.toByteArray();

            cameraImageView.setImageBitmap(mbitmap);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}
