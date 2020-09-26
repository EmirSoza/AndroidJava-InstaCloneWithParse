package com.emirhan.instaclonewithparse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    Bitmap selectedImage;
    Uri imageData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.captureText);
    }

    public void submitPost(View view){

        String comment = editText.getText().toString();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image.png", bytes);

        ParseObject parseObject = new ParseObject("Posts");
        parseObject.put("comment", comment);
        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
        parseObject.put("imageByte", parseFile);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    e.printStackTrace();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void toGallery(View view){
        //If user granted to be reached to gallery or not
        //For permissions-there are levels like normal - dangerous
        //for example internet usage is normal so its enough to write only in manifest
        //but reading storage is dangerous so we have to ask user if he grants

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2&&resultCode==RESULT_OK&&data!=null){
            imageData = data.getData();
            try {
                //Galeriden secim islemi 28 sdk oncesinde sorun cikariyor
                //O yuzden 28 oncesi ve sonrasi icin farkli sekilde sececegiz
                //Ayrica problem olmamasi icin try catch icinde yapiyoruz garanti olsun diye
                if(Build.VERSION.SDK_INT >= 28){
                    //Daha yeni bi method
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);

                }
                else{
                    //Depreciated oldugu icin yeni versiyonda sikinti
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);

                }
                imageView.setImageBitmap(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}