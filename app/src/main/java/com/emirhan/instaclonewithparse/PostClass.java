package com.emirhan.instaclonewithparse;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {
    private final ArrayList<String> usernames;
    private final ArrayList<String> comments;
    private final ArrayList<Bitmap> images;
    private final Activity context;

    public PostClass(ArrayList<String> usernames, ArrayList<String> comments,ArrayList<Bitmap> images,Activity context ){
        super(context, R.layout.custom_view,usernames);
        this.usernames = usernames;
        this.comments = comments;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view, null, true);
        TextView userNameText = customView.findViewById(R.id.customUserName);
        TextView comment = customView.findViewById(R.id.customComment);
        ImageView image = customView.findViewById(R.id.customImage);
        userNameText.setText(usernames.get(position));
        comment.setText(comments.get(position));
        image.setImageBitmap(images.get(position));
        return customView;
    }
}
