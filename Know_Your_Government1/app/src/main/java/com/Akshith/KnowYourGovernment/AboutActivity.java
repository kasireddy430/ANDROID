package com.Akshith.KnowYourGovernment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        text = findViewById(R.id.textView5);
        SpannableString content = new SpannableString("Google Civic Information API");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        text.setText(content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            Toast.makeText(getApplicationContext(),"Returning to previous window",Toast.LENGTH_LONG).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        Toast.makeText(getApplicationContext(),"Returning to previous window",Toast.LENGTH_LONG).show();
        finish();
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String market = "https://developers.google.com/civic-information/";
        intent.setData(Uri.parse(market));
//        Toast.makeText(getApplicationContext(),"Redirected to civic-information",Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
