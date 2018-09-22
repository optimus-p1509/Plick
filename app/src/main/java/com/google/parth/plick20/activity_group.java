package com.google.parth.plick20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Parth on 4/17/2017.
 */

public class activity_group extends Fragment {

    private RecyclerView mGroupRecyclerView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_group,container,false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mGroupRecyclerView = (RecyclerView) rootView.findViewById(R.id.groupRecyclerView);
        mGroupRecyclerView.setHasFixedSize(true);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<group,GroupViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<group, GroupViewHolder>(

                group.class,
                R.layout.group_row,
                GroupViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(GroupViewHolder viewHolder, group model, int position) {

                viewHolder.setName1(model.getName1());

            }
        };
        mGroupRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName1(String name1){
            TextView group_user = (TextView) mView.findViewById(R.id.groupTextView);
            group_user.setText(name1);
        }
    }
}
