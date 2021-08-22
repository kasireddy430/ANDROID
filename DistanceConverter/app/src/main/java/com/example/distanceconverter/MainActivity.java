package com.example.distanceconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView Text1;
    private EditText Edit1;
    private TextView Text2;
    private EditText Edit2;
    private RadioGroup distanceConversion;
    private TextView historyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Text1 = (TextView)findViewById(R.id.textView);
        Edit1 = (EditText) findViewById(R.id.editTextNumber);
        Text2 = (TextView) findViewById(R.id.textView2);
        Edit2 = (EditText) findViewById(R.id.editTextNumber2);
        distanceConversion = (RadioGroup) findViewById(R.id.radioGroup2);
        historyText = (TextView) findViewById(R.id.textView4);

        historyText.setMovementMethod(new ScrollingMovementMethod());
    }
    @SuppressLint("DefaultLocale")
    public void doConvert(View v){
        String inputValue = Edit1.getText().toString();

        float input,output;
        if(distanceConversion.getCheckedRadioButtonId() == R.id.Miles_to_Kilometers)
        {
            input = Float.parseFloat(inputValue);
            output = (float) ((input) * 1.60934);
            Edit2.setText(String.format("%.1f",output));
            Edit1.setText("");
            String history = historyText.getText().toString();
            historyText.setText(String.format("%.1f Mi ==> %.1f Km \n %s",input,output,history));
        }
        else{
            input = Float.parseFloat(inputValue);
            output = (float) ((input) * 0.621371);
            Edit2.setText(String.format("%.1f",output));
            Edit1.setText("");
            String history = historyText.getText().toString();
            historyText.setText(String.format("%.1f Km ==> %.1f Mi \n %s",input,output,history));
        }
    }
    public void doClear(View v){
        Edit1.setText("");
        Edit2.setText("");
        historyText.setText("");
    }
    public void conversionClick(View v){
        String miles ="Miles Value :";
        Edit1.setText("");
        Edit2.setText("");
        String txt1 = Text1.getText().toString();
        String txt2 = Text2.getText().toString();
        if(distanceConversion.getCheckedRadioButtonId() == R.id.Miles_to_Kilometers ) {
              if(!txt1.equals(miles)){
                  Text1.setText(txt2);
                  Text2.setText(txt1);
              }
        }
        else if(distanceConversion.getCheckedRadioButtonId() == R.id.Kilometers_to_Miles)
        {
            if(txt1.equals(miles)) {
            Text1.setText(txt2);
            Text2.setText(txt1);
            }
            }
        }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("CONVERSION_HISTORY",historyText.getText().toString());
        outState.putString("Text_View_1",Text1.getText().toString());
        outState.putString("Text_View_2",Text2.getText().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        historyText.setText(savedInstanceState.getString("CONVERSION_HISTORY"));
        Text1.setText(savedInstanceState.getString("Text_View_1"));
        Text2.setText(savedInstanceState.getString("Text_View_2"));
    }
}