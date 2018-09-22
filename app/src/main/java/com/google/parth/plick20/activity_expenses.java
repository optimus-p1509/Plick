package com.google.parth.plick20;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Parth on 4/9/2017.
 */

public class activity_expenses extends Fragment {

    private Button downloadImages;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_expense,container,false);
        downloadImages = (Button) rootView.findViewById(R.id.downloadImagesButton);
        downloadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),activity_image.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
