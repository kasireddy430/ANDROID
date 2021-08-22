package com.Akshith.KnowYourGovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private TextView loca;
    private TextView office;
    private TextView name;
    private ImageView iv;
    private Official off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        loca= (TextView) findViewById(R.id.location);
        office = (TextView) findViewById(R.id.office);
        name = (TextView) findViewById(R.id.name);
        iv = (ImageView) findViewById(R.id.photo);

        Intent intent = getIntent();

        if(intent.hasExtra("location"))
            loca.setText(intent.getStringExtra("location"));

        if (intent.hasExtra(Official.class.getName())) {
            off = (Official) intent.getSerializableExtra(Official.class.getName());
            office.setText(off.getOffice());
            name.setText(off.getName());
            loadRemoteImage(off.getPhotoURL());
        }

        View view = this.getWindow().getDecorView();
        if(off.getParty().equalsIgnoreCase("republican") || off.getParty().equalsIgnoreCase("Republican Party"))
            view.setBackgroundColor(getResources().getColor(R.color.colorRed));  //red
        else if(off.getParty().equalsIgnoreCase("democratic") || off.getParty().equalsIgnoreCase("Democratic Party"))
            view.setBackgroundColor(getResources().getColor(R.color.colorBlue));  //blue
        else
            view.setBackgroundColor(getResources().getColor(R.color.colorBlack));  //black
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            Toast.makeText(this, "returning to previous screen", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent clickedNoteIntent = new Intent();
        setResult(RESULT_OK, clickedNoteIntent);
        Toast.makeText(this, "returning to previous screen", Toast.LENGTH_SHORT).show();
        finish();
    }

    private  void loadRemoteImage(final String imageURL){

        if (imageURL != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = imageURL.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .fit()
                            .centerCrop()
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(iv);
                }
            }).build();
            picasso.load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(iv);
        } else {
            Picasso.with(this).load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(iv);
        }
    }
}
