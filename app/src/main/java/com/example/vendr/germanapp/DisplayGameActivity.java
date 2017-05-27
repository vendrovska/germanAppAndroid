package com.example.vendr.germanapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayGameActivity extends AppCompatActivity {

    String [] wordsDictionary = new String[]{"one", "two", "three"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
    }
    public void setCurrentWordValue(View view){
        String message = wordsDictionary[0];
       TextView textview_currentWord = (TextView) findViewById(R.id.textView);
        textview_currentWord.setText(message);
    }
    public void checkUserAnswer(View view){
        String message = ((Button) view).getText().toString() + "TESTttt";
        TextView textview_currentWord = (TextView) findViewById(R.id.textView);
        textview_currentWord.setText(message);
    }
}
