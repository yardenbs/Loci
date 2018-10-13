package com.mta.loci;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

class PostUtils {
    public static final long SECONDS_IN_DAY = 24*60*60;
    public static final int MINIMUM_UNLOCKING_DISTANCE = 15;


    public static boolean GetIsOldPost(long datePosted){
        return new Date().after(new Date(datePosted + PostUtils.SECONDS_IN_DAY));
    }

    //generate markerOptions object from the post provided
    public static MarkerOptions GenerateMarkerOptions(Post post) {
        LociUser user = LociUtil.getUserFromDatabase(post.getCreatorId());
        MarkerOptions mo = new MarkerOptions().position(post.getLatlng())
                .title(user.getName()); //TODO    user.icon(); need to get the user's icon here

        return mo;
    }

}
