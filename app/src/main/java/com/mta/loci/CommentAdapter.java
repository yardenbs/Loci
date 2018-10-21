package com.mta.loci;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> mCommentsList;

    public CommentAdapter(List<Comment> commentsList) {

        this.mCommentsList = commentsList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);

        return new CommentViewHolder(parent.getContext(),itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {

        Comment comment = mCommentsList.get(position);
        holder.getCommentText().setText(comment.getmComment());
    }


    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView mCommentText;

        public CommentViewHolder(Context context, View view) {

            super(view);
            mCommentText = (TextView) view.findViewById(R.id.comment_text);
        }

        public TextView getCommentText() {
            return mCommentText;
        }

        public void setUserReview(TextView commentText) {
            this.mCommentText = commentText;
        }
    }
}