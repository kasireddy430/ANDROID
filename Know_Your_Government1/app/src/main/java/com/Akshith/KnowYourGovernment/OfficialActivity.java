package com.Akshith.KnowYourGovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "OfficialActivity";
    private TextView location;
    private TextView office;
    private TextView name;
    private TextView party;
    private ImageView officialLogo;
    private TextView address;
    private TextView number;
    private TextView website;
    private TextView email;
    private ImageView imageView;
    Official officeDetailsMain;
    private Official o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        Intent intent = getIntent();

        location = (TextView) findViewById(R.id.loc);

        if(intent.hasExtra("location"))
            location.setText(intent.getStringExtra("location"));

        if (intent.hasExtra(Official.class.getName())) {

            o = (Official) intent.getSerializableExtra(Official.class.getName());

            office = (TextView) findViewById(R.id.location);
            office.setText(o.getOffice());
            name = (TextView) findViewById(R.id.officialName);
            name.setText(o.getName());
            if(!o.getParty().equalsIgnoreCase("Unknown") && !o.getParty().equalsIgnoreCase("No data provided")) {
                party = (TextView) findViewById(R.id.officialParty);
                party.setText("(" + o.getParty() + ")");
            }
            address = (TextView) findViewById(R.id.officialAddress);
            address.setText(o.getAddress()+","+location.getText().toString());
            number = (TextView) findViewById(R.id.officialPhone);
            number.setText(o.getPhone());
            website = (TextView) findViewById(R.id.officialWebsite);
            website.setText(o.getWebsiteURL());
            email = (TextView) findViewById(R.id.officialEmail);
            email.setText(o.getEmail());

            imageView = (ImageView)findViewById(R.id.officialPhoto);
            loadRemoteImage(o.getPhotoURL());
//            setImage(o.getPhotoURL());
//            ImageView officialLogo = findViewById(R.id.imgLogo);
            ImageView fbImage = (ImageView) findViewById(R.id.fbLink);
            ImageView twImage = (ImageView) findViewById(R.id.twitterLink);
            ImageView ytImage = (ImageView) findViewById(R.id.ytLink);

            if(o.getFacebookURL()!= null && !o.getFacebookURL().equals(""))
                fbImage.setVisibility(View.VISIBLE);
            else
                fbImage.setVisibility(View.INVISIBLE);


            if(o.getYouTubeURL()!= null && !o.getYouTubeURL().equals(""))
                ytImage.setVisibility(View.VISIBLE);
            else
                ytImage.setVisibility(View.INVISIBLE);

            if(o.getTwitterURL()!= null && !o.getTwitterURL().equals(""))
                twImage.setVisibility(View.VISIBLE);
            else
                twImage.setVisibility(View.INVISIBLE);


            View view = this.getWindow().getDecorView();
            if(o.getParty().equalsIgnoreCase("democratic")  || o.getParty().equalsIgnoreCase("Democratic party"))
                view.setBackgroundColor(getResources().getColor(R.color.colorBlue));  //red
            else if(o.getParty().equalsIgnoreCase("Republican") || o.getParty().equalsIgnoreCase("Republican party"))
                view.setBackgroundColor(getResources().getColor(R.color.colorRed));  //blue
            else
                view.setBackgroundColor(getResources().getColor(R.color.colorBlack));  //black
        }

        Linkify.addLinks(website, Linkify.WEB_URLS);
        Linkify.addLinks(number, Linkify.PHONE_NUMBERS);
//        Linkify.addLinks(address, Linkify.MAP_ADDRESSES);
        Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);

        Linkify.addLinks(address, Linkify.MAP_ADDRESSES);
        address.setLinkTextColor(Color.parseColor("#ffffff"));


        website.setLinkTextColor(Color.WHITE);
        number.setLinkTextColor(Color.WHITE);
        address.setLinkTextColor(Color.WHITE);
        email.setLinkTextColor(Color.WHITE);
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
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
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
                            .into(imageView);
                }
            }).build();
            picasso.load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        } else {
            Picasso.with(this).load(imageURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(imageView);
        }
    }

    public void openPhotoActivity(View v){
        Log.d(TAG, "openPhotoActivity: ");
        if(o.getPhotoURL()==null)
            return;

        Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
        intent.putExtra("location", location.getText());
        intent.putExtra(Official.class.getName(), o);
        startActivity(intent);
    }

  public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + o.getFacebookURL();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + o.getFacebookURL();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = o.getTwitterURL();
        try {
        // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
        // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void youTubeClicked(View v) {
        String name = o.getYouTubeURL();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
//    private void setImage(final String url){
//        if(url != null)
//        {
//        final String photoUrl = url;
////        final Intent intent;
//        if(o.getParty().equalsIgnoreCase("Democratic Party")){
//            Picasso picasso = new Picasso.Builder(this).build();
//            picasso.load(R.drawable.dem_logo).into(officialLogo);
//            officialLogo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse("https://democrats.org"));
//                    startActivity(intent);
//
//                }
//            });
//        }
//        else if(o.getOfficial().getParty().equalsIgnoreCase("Republican Party")){
//            Picasso picasso = new Picasso.Builder(this).build();
//            picasso.load(R.drawable.rep_logo).into(officialLogo);
//            officialLogo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse("https://www.gop.com"));
//                    startActivity(intent);
//
//                }
//            });
//        }
//        if ( photoUrl != null) {
//            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//
//                    final String changedUrl = photoUrl.replace("http:", "https:");
//
//                    picasso.load(changedUrl)
//                            .fit()
//                            .error(R.drawable.brokenimage)
//                            .placeholder(R.drawable.placeholder)
//                            .into(officialLogo);
//                }
//            }).build();
//
//            picasso.load(photoUrl)
//                    .fit()
//                    .placeholder(R.drawable.placeholder)
//                    .error(R.drawable.brokenimage)
//                    .into(officialLogo);
//
//        } else {
//            Picasso.with(this).load((String) null)
//                    .fit()
//                    .error(R.drawable.brokenimage)
//                    .placeholder(R.drawable.missingimage)
//                    .into(officialLogo);
//        }
//        }else {
//            Picasso.with(this).load(url)
//                    .fit()
//                    .centerCrop()
//                    .error(R.drawable.brokenimage)
//                    .placeholder(R.drawable.missingimage)
//                    .into(officialLogo);
//        }
//    }



//    @Override
//    public void onClick(View view) {
//        if(officeDetailsMain.getOfficial().getOfficialPhotoLink() != null) {
//            if(officeDetailsMain.getOfficial().getOfficialPhotoLink().length() > 0) {
//                startPhotoActivity(officeDetailsMain);
//            }
//       }
//    }
//    public void startPhotoActivity(Official officeDetails){
//        Intent intent = new Intent(this, PhotoActivity.class);
//       intent.putExtra(Official.class.getName(), officeDetails);
//        startActivityForResult(intent,0);
//   }

}