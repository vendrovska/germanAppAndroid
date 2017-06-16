package com.example.vendr.germanapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class DisplayGameActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener{
    private static final int ROUND_SIZE = 20;
    private  static final int MIN_ROUND_SIZE = 3;
    boolean dataSourceIsEmpty = true;
    boolean mutedTts = false;
    String currentCorrectArticle = "DER";
    ArrayList <GermanNoun> wordsDictionaryList = new ArrayList<GermanNoun>();
    GermanNoun currentWord;
    int userScore = 0;
    int curIndex;
    int lastDataSourceIndex = 0;
    JSONArray dataSource;
    private TextToSpeech tts;
    private Button btnSpeak;
    private TextView txtTeext;
    RatingBar currentWordScore;

    public void getFullGermanDictionary(){
        String s = "";
        try {
            AssetManager assetManager = getAssets();
            //InputStream inputStream = assetManager.open("GermanNounsDictionary.json");
            InputStream inputStream = assetManager.open("TestDictionary.json");
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fillLocalDictionary();
        getRangeFromDictionary();
        establishTextToSpeech();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
        tts = new TextToSpeech(this, this);
        updateNounTextView();
    }
    public void checkUserAnswer(final View view){
        String articleChosen = ((Button) view).getText().toString();
        final boolean correrctAnswer = articleChosen.equals(currentCorrectArticle);
        boolean reset = false;
        updateButtonColor(view, correrctAnswer, reset);
        class TtsCompletionHandler extends UtteranceProgressListener {
            @Override
            public void onDone (String utteranceId){
                // Executing updates on UI (initial) thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateButtonColor(view, correrctAnswer, true /* reset */);
                        if(correrctAnswer){
                            currentWord.UserAnswerCount++;
                            if(currentWord.UserAnswerCount >= 3){
                                //remove from wordsDictionary
                                wordsDictionaryList.remove(curIndex); //todo: replace with more efficient structure (array with first and last indexes

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

                        if(wordsDictionaryList.size()== 0){
                            openGameEndActivity(view);
                        }
                        else{
                            updateNounTextView();
                        }

                    }
                });
                //TODO: add stars to reflect user answers for each word

            }
            @Override
            public void onError(String utteranceId){

            }
            @Override
            public void onStart (String utteranceId){

            }
        }


        startTalkToSpeech(new TtsCompletionHandler());
    }

    private void startTalkToSpeech(UtteranceProgressListener listener) {
        speakOut(listener);
    }

    private void updateButtonColor(View v, boolean answerCorrect, boolean reset) {
        int buttonIntId =  v.getId();
        String color = "#eff0f2";
        if(answerCorrect && !reset){
          color = "#99ffcc";
        }
        else if (!answerCorrect && !reset) {
            color = "#ff9999";
        }
        if (reset){
            Button dieBtn = (Button) findViewById(R.id.dieButton);
            Button derBtn = (Button) findViewById(R.id.derButton);
            Button dasBtn = (Button) findViewById(R.id.dasButton);
            dieBtn.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.DARKEN);
            derBtn.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.DARKEN);
            dasBtn.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.DARKEN);

        }
        else{
            Button clickedButton = (Button) findViewById(buttonIntId);
            clickedButton.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.DARKEN);
        }


    }

    public void updateNounTextView(){
        //generate random number for picking a word from dictionary
        Random rand = new Random();
        curIndex = rand.nextInt(wordsDictionaryList.size());
        //remember a current word for future refferences
        currentWord = wordsDictionaryList.get(curIndex);
        String message = currentWord.GermanText;
        currentCorrectArticle = currentWord.Article.toUpperCase();
        TextView textview_currentWordGerman = (TextView) findViewById(R.id.textView);
        TextView textview_currentWordEnglish = (TextView) findViewById(R.id.textView2);
        textview_currentWordGerman.setText(currentWord.GermanText);
        textview_currentWordEnglish.setText(currentWord.EnglishTranslation);
        TextView textview_currentScore = (TextView) findViewById(R.id.textView3);
        textview_currentScore.setText(String.valueOf(userScore));
        currentWordScore = (RatingBar) findViewById(R.id.nounRatingBar);
        currentWordScore.setRating(currentWord.UserAnswerCount);

    }
    private void establishTextToSpeech(){
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
                newNoun.GermanWithArticle = newNoun.GermanWithArticle.substring(0,newNoun.GermanWithArticle.indexOf(","));
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

    @Override
    public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS){
    int result = tts.setLanguage(Locale.GERMAN);
    if((result == TextToSpeech.LANG_NOT_SUPPORTED) || result == TextToSpeech.LANG_MISSING_DATA){
        Log.e("TTS", "This language is not supported");
    }
    else{
        //btnSpeak.setEnabled(true);
        //speakOut();
        Log.e("TTS","initialized");
    }
        }
        else{
        Log.e("TTS", "initialization failed");
    }
    }
    private void speakOut(UtteranceProgressListener listener) {
        if(!mutedTts){
            String text = currentWord.GermanWithArticle; //txtTeext.getText().toString();
            tts.setOnUtteranceProgressListener(listener);
           // tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);

        }

    }

    public void muteOff(View v){
        mutedTts = true;
        v.setVisibility(View.INVISIBLE);
        ImageButton buttonOn=(ImageButton)findViewById(R.id.muteOn);
        buttonOn.setVisibility(View.VISIBLE);
    }
    public void muteOn(View v){
        mutedTts = false;
        v.setVisibility(View.INVISIBLE);
        ImageButton buttonOff=(ImageButton)findViewById(R.id.muteOff);
        buttonOff.setVisibility(View.VISIBLE);
    }


}
