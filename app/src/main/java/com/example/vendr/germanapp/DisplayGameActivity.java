package com.example.vendr.germanapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.speech.tts.TextToSpeech;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class DisplayGameActivity extends AppCompatActivity {
    private static final int ROUND_SIZE = 20;
    private  static final int MIN_ROUND_SIZE = 3;
    boolean dataSourceIsEmpty = true;
    String currentCorrectArticle = "DER";
    ArrayList <GermanNoun> wordsDictionaryList = new ArrayList<GermanNoun>();
    GermanNoun currentWord;
    int userScore = 0;
    int curIndex;
    int lastDataSourceIndex = 0;
    JSONArray dataSource;
    TextToSpeech ttsObject;

    public void getFullGermanDictionary(){
        String s = "";
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("GermanNounsDictionary.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder total = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                total.append(s).append('\n');
            }
            s = total.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
             dataSource = new JSONArray(s);
             dataSourceIsEmpty = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void fillLocalDictionary(){
        getFullGermanDictionary();
        //TODO get data from real dictionary
        //todo:replace code below with a real data
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fillLocalDictionary();
        getRangeFromDictionary();
        establishTextToSpeech();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
        updateNounTextView();
    }
    public void checkUserAnswer(View view){
        String articleChosen = ((Button) view).getText().toString();
        boolean correrctAnswer = articleChosen.equals(currentCorrectArticle);
        updateButtonColor(view, correrctAnswer);
       if(correrctAnswer){
           currentWord.UserAnswerCount++;
           if(currentWord.UserAnswerCount >= 3){
               //remove from wordsDictionary
               wordsDictionaryList.remove(curIndex); //todo: replace with more efficient structure (array with first and last indexes

               if(wordsDictionaryList.size()== 0){
                   openGameEndActivity(view);
               }
               if(wordsDictionaryList.size() <= MIN_ROUND_SIZE && !dataSourceIsEmpty){
                   getRangeFromDictionary();
               }
               userScore++;
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
       else {
           currentWord.UserAnswerCount = 0;
       }
       updateNounTextView();
        //todo switch fr btn name to handkle diff. article values
        //todo connect to mongo db dictionary
    }

    private void updateButtonColor(View v, boolean answerCorrect) {
        int buttonIntId =  v.getId();
        String color;
        if(answerCorrect){
          color = "#99ffcc";
        }
        else{
            color = "#ff9999";
        }
        Button clickedButton = (Button) findViewById(buttonIntId);
        clickedButton.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.DARKEN);
    }

    public void updateNounTextView(){
        //generate random number for picking a word from dictionary
        Random rand = new Random();
        int curIndex = rand.nextInt(wordsDictionaryList.size());
        //remember a current word for future refferences
        currentWord = wordsDictionaryList.get(curIndex);
        String message = currentWord.GermanText;
        currentCorrectArticle = currentWord.Article.toUpperCase();
        TextView textview_currentWordGerman = (TextView) findViewById(R.id.textView);
        TextView textview_currentWordEnglish = (TextView) findViewById(R.id.textView2);
            if(true){
            //TODO: update the if statement and open "the end" activity
        }
        textview_currentWordGerman.setText(currentWord.GermanText);
        textview_currentWordEnglish.setText(currentWord.EnglishTranslation);

        TextView textview_currentScore = (TextView) findViewById(R.id.textView3);
        textview_currentScore.setText(String.valueOf(userScore));

    }
    private void establishTextToSpeech(){
         tts = new TextToSpeech();
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    }
    public void getRangeFromDictionary(){
        //wordsDictionaryList

        for(int i = lastDataSourceIndex; i < lastDataSourceIndex + ROUND_SIZE; i++){
            if(i >= dataSource.length()){
                dataSourceIsEmpty = true;
                break;
            }
            try {
                JSONObject jsonNoun = dataSource.getJSONObject(i);
                GermanNoun newNoun = new GermanNoun();
                newNoun.Article = jsonNoun.getString("Article");
                newNoun.EnglishTranslation = jsonNoun.getString("English");
                newNoun.GermanText = jsonNoun.getString("GermanNoArticle");
                newNoun.GermanText = newNoun.GermanText.substring(0,newNoun.GermanText.indexOf(","));
                newNoun.GermanWithArticle = jsonNoun.getString("GermanWithArticle");
                newNoun.UserAnswerCount = 0;
                wordsDictionaryList.add(newNoun);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        lastDataSourceIndex += ROUND_SIZE;


    }
    public void openGameEndActivity(View view){
        Intent intent = new Intent(this, TheEndActivity.class);
        startActivity(intent);
    }
}
