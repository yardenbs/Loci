package com.mta.loci;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;



public class SearchUsersActivity extends AppCompatActivity {

    private EditText mSearchField;
    private FloatingActionButton mSearchButton;

    private RecyclerView mResultList;

    private DatabaseReference mUserDatabase;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        mSearchField = findViewById(R.id.SerachField);
        mSearchButton = findViewById(R.id.SearchButton);

        mResultList = findViewById(R.id.ResultList);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                String searchText = mSearchField.getText().toString();
                firebaseUserSearch(searchText);
            }
        });

        firebaseUserSearch("");

    }


    private void firebaseUserSearch(String searchText) {

        Toast.makeText(this, "Started search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        //set Options
        FirebaseRecyclerOptions<LociUser> options =
                new FirebaseRecyclerOptions.Builder<LociUser>()
                        .setQuery(firebaseSearchQuery, LociUser.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseRecyclerAdapter<LociUser, UserviewHolder>(options) {
            @NonNull
            @Override
            public UserviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
                return new UserviewHolder(mView);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserviewHolder holder, int position, @NonNull LociUser model) {

                holder.setName(model.getName());
                holder.setImageView(model.getmPhotoUrl());

                final String user_id = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(SearchUsersActivity.this, UserProfileActivity.class);
                        i.putExtra("uid", user_id);
                        startActivity(i);

                    }
                });
            }
        };

        mResultList.setLayoutManager(new LinearLayoutManager(this));
        mResultList.setAdapter(adapter);


    }

    private void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow( getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    protected void onStart() {
        super.onStart();

        //important for fibrebase database ui
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //important for fibrebase database ui
        adapter.stopListening();
    }

    //VIEW HOLDER
    public static class UserviewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mdisplayname;
        ImageView mImageView;


        public UserviewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mdisplayname = (TextView) mView.findViewById(R.id.UserName);
            mImageView = (ImageView) mView.findViewById(R.id.ProfilePic);



        }
        public void setName(String display_name){

            mdisplayname.setText(display_name);

        }

        public void setImageView(final String img_uri){

            if (img_uri!=null && !img_uri.equals("")){
                LociUtil.loadImage(img_uri, mImageView);
            }else{
                mImageView.setImageResource(R.drawable.profile_default_pic);
            }
        }
    }
}
