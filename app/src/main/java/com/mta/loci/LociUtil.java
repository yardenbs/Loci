package com.mta.loci;

import android.content.Intent;

class LociUtil {

    public static void InitUserFromIntent(Intent intent, LociUser mUser) {
        mUser = new LociUser();
        mUser.SetUserId(intent.getIntExtra("0", -1));
        mUser.SetUserPostsIds(intent.getIntegerArrayListExtra("1"));
        mUser.SetTotalPostsIds(intent.getIntegerArrayListExtra("2"));
        mUser.SetUnlockedPostsIds(intent.getIntegerArrayListExtra("3"));
    }
}
