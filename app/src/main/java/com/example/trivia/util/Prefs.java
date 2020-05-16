package com.example.trivia.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score){

        int currentScore = score;

        int lastScore = preferences.getInt("highestScore", 0);

        if (currentScore > lastScore) {
            preferences.edit().putInt("highestScore", currentScore).apply();
        }

    }

    public  int getHighestScore(){

        return preferences.getInt("highestScore", 0);
    }

    public void setState(int index){

        preferences.edit().putInt("Babe", index).apply();
    }

    public int getState(){

        return preferences.getInt("Babe", 0);
    }

}
