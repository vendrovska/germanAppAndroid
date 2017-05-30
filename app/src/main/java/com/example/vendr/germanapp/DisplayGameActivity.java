package com.example.vendr.germanapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayGameActivity extends AppCompatActivity {

    String [] wordsDictionary = new String[]{"one", "two", "three"};
    String currentCorrectArticle = "DER";
    GermanNoun [] germanNouns = new GermanNoun[4];
    public void fillLocalDictionary(){
        //TODO get data from real dictionary

        //todo:replace code below with a real data
        GermanNoun firstNoun = new GermanNoun();
        firstNoun.Article = "DER";
        firstNoun.EnglishTranslation = "belt";
        firstNoun.GermanText = "Buckle";
        firstNoun.AlphabetOrderId = 1;
        firstNoun.RandomOrderId = 3;
        GermanNoun secondNoun = new GermanNoun();
        secondNoun.Article = "DIE";
        secondNoun.EnglishTranslation = "Map";
        secondNoun.GermanText = "Welt";
        secondNoun.AlphabetOrderId = 2;
        secondNoun.RandomOrderId = 2;
        GermanNoun thirdNoun = new GermanNoun();
        thirdNoun.Article = "DAS";
        thirdNoun.EnglishTranslation = "Girl";
        thirdNoun.GermanText = "Madchen";
        thirdNoun.AlphabetOrderId = 3;
        thirdNoun.RandomOrderId = 1;
        GermanNoun initialNoun = new GermanNoun();
        initialNoun.Article = "DAS";
        initialNoun.EnglishTranslation = "Girl";
        initialNoun.GermanText = "Madchen";
        initialNoun.AlphabetOrderId = 3;
        initialNoun.RandomOrderId = 1;
        germanNouns[0] = firstNoun;
        germanNouns[2] = secondNoun;
        germanNouns[3] = thirdNoun;
        germanNouns[4] = initialNoun;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
        changeCurrentWordValue("Hello");
    }
    public void setCurrentWordValue(View view){
        String message = wordsDictionary[0];
       TextView textview_currentWord = (TextView) findViewById(R.id.textView);
        textview_currentWord.setText(message);
    }
    public void checkUserAnswer(View view){
        String articleChosen = ((Button) view).getText().toString();
        if(articleChosen == currentCorrectArticle){
            //add answer to total # of corretct answers
            //say the word aloud
            //check if word has article chosen correctly 3 times in a row
            //remove the word from array if it was guessed three times in a row
            //check if more words needed to the array (if fewer words are left)
            //make correct button to be of a green color
            //OR
            //clear word correct answers conunting back to 0
            //proceed to the next word
            //color pressed button to the red color


        }
       // changeCurrentWordValue(message);
        //todo switch fr btn name to handkle diff. article values
        //todo connect to mongo db dictionary
    }
    public void changeCurrentWordValue(String newWord){
        TextView textview_currentWord = (TextView) findViewById(R.id.textView);
        textview_currentWord.setText(newWord);
    }
}
