package com.example.vendr.germanapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class DisplayGameActivity extends AppCompatActivity {

    String currentCorrectArticle = "DER";
    ArrayList <GermanNoun> wordsDictionaryList = new ArrayList<GermanNoun>();
    GermanNoun currentWord;
    int userScore = 0;
    int curIndex;
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
        GermanNoun fourthNoun = new GermanNoun();
        fourthNoun.Article = "DAS";
        fourthNoun.EnglishTranslation = "boy";
        fourthNoun.GermanText = "ganz";
        fourthNoun.AlphabetOrderId = 3;
        fourthNoun.RandomOrderId = 1;
        wordsDictionaryList.add(firstNoun);
        wordsDictionaryList.add(secondNoun);
        wordsDictionaryList.add(thirdNoun);
        wordsDictionaryList.add(fourthNoun);


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
       if(articleChosen.equals(currentCorrectArticle) ){
           //wordsDictionaryList.set(curIndex,);
           currentWord.UserAnswerCount++;
           if(currentWord.UserAnswerCount >= 3){
               //remove from wordsDictionary
               wordsDictionaryList.remove(curIndex);
           }
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
       else{
           currentWord.UserAnswerCount = 0;
       }
       updateNounTextView();
        //todo switch fr btn name to handkle diff. article values
        //todo connect to mongo db dictionary
    }
    public void updateNounTextView(){
        //generate random number for picking a word from dictionary
        Random rand = new Random();
        int curIndex = rand.nextInt(wordsDictionaryList.size());//todo: index is always 0
        //remember a current word for future refferences
        currentWord = wordsDictionaryList.get(curIndex);
        String message = currentWord.GermanText;
        currentCorrectArticle = currentWord.Article;
        TextView textview_currentWord = (TextView) findViewById(R.id.textView);
        textview_currentWord.setText(currentWord.GermanText);
    }
}
