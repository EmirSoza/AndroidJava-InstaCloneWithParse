package com.emirhan.instaclonewithparse;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> usernames;
    ArrayList<String> comments;
    ArrayList<Bitmap> images;
    PostClass postClassAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_post){
            //INTENT
            Intent intent = new Intent(getApplicationContext(), PostActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.log_out){
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView = findViewById(R.id.listView);
        usernames = new ArrayList<>();
        comments = new ArrayList<>();
        images = new ArrayList<>();
        postClassAdapter = new PostClass(usernames, comments, images, this);
        listView.setAdapter(postClassAdapter);
        download();
    }


    public void download(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts").orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    if(objects.size() > 0){
                        for(final ParseObject object : objects){

                            ParseFile parseFile = object.getParseFile("imageByte");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        images.add(bitmap);
                                        usernames.add(object.getString("username")) ;
                                        comments.add(object.getString("comment"));
                                        postClassAdapter.notifyDataSetChanged();

                                    }

                                }
                            });

                        }
                    }
                }
            }
        });
    }
}