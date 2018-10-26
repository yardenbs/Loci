package com.mta.loci;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        holder.getCommentCreator().setText(comment.getmCreator() + ":");
    }


    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView mCommentText;
        private TextView mCommentCreator;

        public CommentViewHolder(Context context, View view) {
            super(view);
            mCommentText = (TextView) view.findViewById(R.id.comment_text);
            mCommentCreator = (TextView) view.findViewById(R.id.comment_creator);
        }

        public TextView getCommentText() {
            return mCommentText;
        }

        public TextView getCommentCreator() {
            return mCommentCreator;
        }

        public TextView getmCommentCreator() {
            return mCommentCreator;
        }
    }
}