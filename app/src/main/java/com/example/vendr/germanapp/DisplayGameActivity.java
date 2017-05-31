package com.example.vendr.germanapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class DisplayGameActivity extends AppCompatActivity {

    String currentCorrectArticle = "DER";
    GermanNoun [] wordsDictionary = new GermanNoun[4];
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
        initialNoun.EnglishTranslation = "boy";
        initialNoun.GermanText = "ganz";
        initialNoun.AlphabetOrderId = 3;
        initialNoun.RandomOrderId = 1;
        wordsDictionary[0] = firstNoun;
        wordsDictionary[1] = secondNoun;
        wordsDictionary[2] = thirdNoun;
        wordsDictionary[3] = initialNoun;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fillLocalDictionary();
        // updateNounTextView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
        updateNounTextView();
    }
    public void checkUserAnswer(View view){
        String articleChosen = ((Button) view).getText().toString();
       // if(articleChosen == currentCorrectArticle){
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
        //}
       updateNounTextView();
        //todo switch fr btn name to handkle diff. article values
        //todo connect to mongo db dictionary
    }
    public void updateNounTextView(){
        Random rand = new Random();
        int index = rand.nextInt(wordsDictionary.length);
        String message = wordsDictionary[index].GermanText;
        currentCorrectArticle = wordsDictionary[index].Article;
        TextView textview_currentWord = (TextView) findViewById(R.id.textView);
        textview_currentWord.setText(wordsDictionary[index].GermanText);
    }
}
