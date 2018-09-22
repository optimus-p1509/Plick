package com.google.parth.plick20;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class activity_image extends Fragment {

    private RecyclerView mImageRecyclerView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_image,container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("photoUploads");
        mImageRecyclerView = (RecyclerView) rootView.findViewById(R.id.imageRecyclerView);
        mImageRecyclerView.setHasFixedSize(true);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<image,ImageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<image, ImageViewHolder>(
                image.class,
                R.layout.image_row,
                ImageViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ImageViewHolder viewHolder, image model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setImage(getContext(),model.getImage());
            }
        };
        mImageRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setName(String Name){
            TextView postName = (TextView) mView.findViewById(R.id.imageTextView);
            postName.setText(Name);
        }

        public void setImage(Context ctx,String Image){
            ImageView postImage = (ImageView) mView.findViewById(R.id.imageImageView);
            Picasso.with(ctx).load(Image).into(postImage);
        }
    }
}
