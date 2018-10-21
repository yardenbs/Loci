package com.mta.loci;

public class Comment {
   private String mUid;
    private String mPostId;
    private String mComment;

    public Comment() {}

    public Comment(String uId, String postId, String comment) {
        this.mUid = uId;
        this.mPostId = postId;
        this.mComment = comment;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmPostId() {
        return mPostId;
    }

    public void setmPostId(String mPostId) {
        this.mPostId = mPostId;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }
}
