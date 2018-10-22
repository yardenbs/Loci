package com.mta.loci;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class LociPostActivity extends AppCompatActivity {

    private Post mPost;
    private LociUser mCreator;
    private  DatabaseReference mCommentsRef;
    private  DatabaseReference mCurrentUserRef;
    private ArrayList<Comment> mCommentsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FloatingActionButton mNewCommentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loci_post);
        String creatorId = getIntent().getStringExtra("creatorId");
        String postId = getIntent().getStringExtra("postId");
        getPostAndUserFromDatabase(postId, creatorId);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.commentsList) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new CommentAdapter(mCommentsList));
        mCommentsRef = FirebaseDatabase.getInstance().getReference("Comments/" + mPost.getId());
        mCommentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    mCommentsList.add(comment);
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initUI() {
        initRecyclerView();
        mNewCommentButton = findViewById(R.id.buttonNewComment);
        mNewCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewComment();
            }
        });

        ImageView ImageViewpost = (ImageView) findViewById(R.id.imageViewPost);
        LociUtil.loadImage(mPost.getmMediaUrl(), ImageViewpost);

        Button creatorNameButton = (Button) findViewById(R.id.buttonCreatorName);
        creatorNameButton.setText(mCreator.getName());

        creatorNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LociPostActivity.this, UserProfileActivity.class);
                intent.putExtra("uid", mCreator.getmUserId());
                startActivity(intent);
            }
        });
    }

    private void writeNewComment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LociPostActivity.this);
        builder.setTitle("Write Comment");
        final EditText input = new EditText(LociPostActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String commentText = input.getText().toString();
                uploadCommentToDatebase(commentText);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void uploadCommentToDatebase(final String commentText) {

         final String uid = FirebaseAuth.getInstance().getUid();
        mCurrentUserRef = FirebaseDatabase.getInstance().getReference().child("Users/").child(uid).child("name");

        mCurrentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String CurrentUsername = dataSnapshot.getValue(String.class);
                Comment comment = new Comment(uid, mPost.getId(), commentText, CurrentUsername);
                mCommentsList.add(comment);
                mCommentsRef.push().setValue(comment);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPostAndUserFromDatabase(String postId ,final String uid) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databasePosts = database.getReference("Posts");

        databasePosts.child(uid).child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost = dataSnapshot.getValue(Post.class);
                getUserFromDatabase(uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LociUtil.class.getSimpleName(), "loadPost:onCancelled", databaseError.toException());
            }
        });

    }
    private void getUserFromDatabase(String uid) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUsers = database.getReference("Users");

        databaseUsers.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mCreator = dataSnapshot.getValue(LociUser.class);
                initUI();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LociUtil.class.getSimpleName(), "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
