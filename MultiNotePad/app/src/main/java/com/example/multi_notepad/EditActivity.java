package com.example.multi_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class EditActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private Multi mulIn;
    String t;
    String d;
    String list_title="";
    String list_description="";
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_entry);

        intent = getIntent();
        title =(EditText) findViewById(R.id.edit_title);
        description = (EditText) findViewById(R.id.edit_decription);
        intent = getIntent();



        if(intent.hasExtra("Multi_Cls")){
            mulIn = (Multi)intent.getSerializableExtra("Multi_Cls");
            assert mulIn != null;
            list_title = mulIn.getTitle();
            list_description = mulIn.getDescription();
            title.setText(list_title);
            description.setText(list_description);
        }

        description.setMovementMethod(new ScrollingMovementMethod());
        description.setTextIsSelectable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.save:
                    saving();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saving() {

        Multi multi = new Multi();

        multi.setTitle(title.getText().toString());
        multi.setDescription(description.getText().toString());
        multi.setDate(Calendar.getInstance().getTime().toString());
        intent = new Intent(this,MainActivity.class);
        intent.putExtra("Edit", multi);

        if(title.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"A multi-note without title cannot be saved",Toast.LENGTH_SHORT).show();

        }else if(list_title.equals(title.getText().toString()) && list_description.equals(description.getText().toString())){
            Toast.makeText(getApplicationContext(),"Title and description isn't changed",Toast.LENGTH_LONG).show();

        } else if(list_title.isEmpty() && list_description.isEmpty()){
            Toast.makeText(getApplicationContext(),"Saving a file",Toast.LENGTH_LONG).show();
            intent.putExtra("help","DESCRIPTION");
            setResult(RESULT_OK,intent);
        }
        else {
            Toast.makeText(getApplicationContext(),"Saving",Toast.LENGTH_LONG).show();
            intent.putExtra("help", "CHANGE_DESCRIPTION");
            setResult(RESULT_OK,intent);
        }

        finish();
    }


    @Override
    public void onBackPressed() {
        t = title.getText().toString();
        d = description.getText().toString();

//       Intent dataToReturn = new Intent();
//       if (!(t.trim().isEmpty()&& d.trim().isEmpty())) {
//            dataToReturn.putExtra("User_Title", t);
//            dataToReturn.putExtra("User_Description", d);
//            setResult(RESULT_OK, dataToReturn);
//        }
          if(list_title.equals(title.getText().toString()) && list_description.equals(description.getText().toString())){
              Toast.makeText(this,"Both Title and description are not modified",Toast.LENGTH_LONG).show();
              super.onBackPressed();
          }
//          if(mulIn.getTitle().isEmpty()){
//              super.onBackPressed();
//              return;
//          }
        else {
              AlertDialog.Builder builder = new AlertDialog.Builder(this);
              builder.setIcon(R.drawable.baseline_announcement_24);
              builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      saving();
                  }
              });
              builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      Toast.makeText(getApplicationContext(), "Exiting", Toast.LENGTH_LONG).show();
                      finish();
                  }
              });

              builder.setMessage("Would you like to save this file?");
              builder.setTitle("Exit");

              AlertDialog dialog = builder.create();
              dialog.show();
          }

    }
}

