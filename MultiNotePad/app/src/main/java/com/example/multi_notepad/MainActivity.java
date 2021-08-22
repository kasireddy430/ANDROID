package com.example.multi_notepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{
   private final ArrayList<Multi> myNote = new ArrayList<>();
   private RecyclerView recyclerView;
   private MultiAdapter mAdapter;
   private int pos;
   private static final int REQ_ID =1;
   private Multi multi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.my_recycler);
        mAdapter = new MultiAdapter(myNote,this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFile();


    }

    @Override
    protected void onResume() {
        myNote.size();
        mAdapter.notifyDataSetChanged();
        super.onResume();

    }

    private void loadFile() {
    try{
        InputStream is = getApplicationContext().openFileInput("multi1.json");
        JsonReader reader =  new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        reader.beginObject();

        while(reader.hasNext()){
            String line;
            line = reader.nextName();
            if(line.equals("multi1")){
                reader.beginArray();
                while(reader.hasNext()){
                    Multi m = new Multi();
                    reader.beginObject();
                    while(reader.hasNext()){
                        line = reader.nextName();
                        if(line.equals("title")){
                            m.setTitle(reader.nextString());
                        } else if(line.equals("date")){
                            m.setDate(reader.nextString());
                        }else if(line.equals("description")){
                            m.setDescription(reader.nextString());
                        }else{
                            reader.skipValue();
                        }
                    }
                    myNote.add(m);
                    reader.endObject();
                }
                reader.endArray();
            }
            else{
                reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();
        mAdapter.notifyDataSetChanged();
    }
    catch (Exception e){
        e.getStackTrace();
    }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent1;
        switch(item.getItemId()){

            case R.id.about:
                intent1 = new Intent(this,AboutActivity.class);
                startActivity(intent1);
                return true;

            case R.id.add:
                intent1 = new Intent(this,EditActivity.class);
                startActivityForResult(intent1,REQ_ID);
                return true;

            default:
                String err = "Unknown menu item: " +item.getTitle();
                Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View view) {
        pos = recyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("Multi_Cls",myNote.get(pos));
        startActivityForResult(intent,REQ_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_ID){
            if(resultCode==RESULT_OK){
                assert data != null;
                Multi m1 = (Multi) data.getSerializableExtra("Edit");
                String update = data.getStringExtra("help");
                assert update != null;
                if(update.equals("DESCRIPTION")){
                    myNote.add(0,m1);
                }
                if(update.equals("CHANGE_DESCRIPTION")){
                    myNote.remove(pos);
                    myNote.add(0,m1);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        pos = recyclerView.getChildLayoutPosition(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.baseline_announcement_24);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myNote.remove(pos);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"File is deleted",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"File is not deleted",Toast.LENGTH_LONG).show();
            }
        });

        builder.setMessage("Would you like to delete this file?");
        builder.setTitle("Exit");

        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    protected void onPause() {
        saveMulti();
        super.onPause();
    }

    private void saveMulti() {
        try{
            FileOutputStream fos = getApplicationContext().openFileOutput("multi1.json",MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.setIndent("");
            writer.beginObject();
            writer.name("multi1");
            writerArray(writer);
            writer.endObject();
            writer.close();
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

    private void writerArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        Iterator i = myNote.iterator();
        while(i.hasNext()){
            writer.beginObject();
            writer.name("title").value(multi.getTitle());
            writer.name("date").value(multi.getDate());
            writer.name("description").value(multi.getDescription());
            writer.endObject();
        }
        writer.endArray();

    }


}

