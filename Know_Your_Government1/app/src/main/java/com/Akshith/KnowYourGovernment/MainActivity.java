package com.Akshith.KnowYourGovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";

    private List<Official> officialList = new ArrayList <>();  // Main content is here

    private RecyclerView recyclerView; // Layout's recyclerview

    private OfficialAdapter mAdapter; // Data to recyclerview adapter

    private Locator locator;

    private TextView currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locator = new Locator(this);
        recyclerView = (RecyclerView) findViewById(R.id.official_list);
        RecyclerView.ItemDecoration itemDecor = new Divide(getDrawable(R.drawable.separator));
        mAdapter = new OfficialAdapter(officialList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecor);

        currentLocation = (TextView) findViewById(R.id.loc);
        currentLocation.setText("No Data for location");
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{return false;}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoMenu:
                Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent2);
                break;
            case R.id.locationMenu:
                if(isOnline()) {
                    showDialogBox();
                }
                else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(this);
                    builder.setTitle("No Internet");
                    builder.setMessage("Check your internet before entering details");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        onItemClick(v, pos);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        onItemClick(v, pos);
        return false;
    }

    public void onItemClick(View v, int idx)
    {
        Official m = officialList.get(idx);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra("location", currentLocation.getText());
        intent.putExtra(Official.class.getName(), m);
        startActivity(intent);
    }

    public void showDialogBox(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);

        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                OfficialRunnable officialRun = new OfficialRunnable(MainActivity.this,et.getText().toString());
                new Thread(officialRun).start();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("Enter City, State or a Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 5) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot retrieve address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "Location providers are not available", Toast.LENGTH_LONG).show();
    }

    String doLocationWork(double latitude, double longitude) {
        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address ad = addresses.get(0);
                String zipCode = ad.getPostalCode();
                if(zipCode!=null || zipCode!=""){
                    OfficialRunnable officialRun = new OfficialRunnable(MainActivity.this,zipCode);
                    new Thread(officialRun).start();
//                AsynchTask ast1= new AsynchTask(this);
//                ast1.execute(zipCode);
                break;
                }

            } catch (IOException e) {
                Log.d(TAG, "doLocationWork: " + e.getMessage());

            }
            Toast.makeText(this, "GeoCoder service is slow", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out", Toast.LENGTH_LONG).show();
        return null;
    }

    public void setOfficialList(Object[] results){
        if(results != null && results.length!= 0)
        {
            if(results[0] != null)
                currentLocation.setText(results[0].toString());
            else
                currentLocation.setText("No Data for location");

            officialList.clear();
            List<Official> tempList= (ArrayList)results[1];
            if(tempList!=null) {
                officialList.addAll(tempList);
            }
            mAdapter.notifyDataSetChanged();
        }
        else
        {
            currentLocation.setText("No Data for location");
            officialList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

}
