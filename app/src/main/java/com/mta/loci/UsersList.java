package com.mta.loci;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersList extends ArrayAdapter {
    private Activity context;
    private ArrayList<LociUser> usersList;
    public UsersList(Activity context, ArrayList<LociUser> usersList){
        super(context, R.layout.activity_search, usersList );
        this.context = context;
        this.usersList =usersList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_search, null, true);
        TextView textViewName =(TextView) listViewItem.findViewById(R.id.textViewName);
        LociUser user = usersList.get(position);
        textViewName.setText(user.getName());
        return  listViewItem;
    }
}
