package com.example.trivia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextView, questionCounterTextView,tvScore,highestScore;
    private Button trueButton, falseButton;
    private ImageButton nextButton, prevButton;
    private int currentQuestionIndex= 0;
    private List<Question> questionList;
    private int scoreCounter = 0;
    private int clicks=0;
    private Score score;

    private Prefs prefs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();

        prefs = new Prefs(MainActivity.this);

        questionTextView = findViewById(R.id.tvQuestions);
        questionCounterTextView = findViewById(R.id.counter);
        tvScore = findViewById(R.id.tvScore);
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        highestScore = findViewById(R.id.tvHighestScore);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        currentQuestionIndex = prefs.getState();
        Log.d("Kino", "processFinished: " +prefs.getState());


        questionList=new  QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                highestScore.setText(String.valueOf("High Score: "+prefs.getHighestScore()));

                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText((currentQuestionIndex + 1) + "/" + (questionArrayList.size()+1));
                tvScore.setText(String.valueOf("Score: "+score.getScore()));
//                Log.d("Babe", "processFinished: " +questionArrayList);
                hidePrevButton();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.prevButton:
                if (currentQuestionIndex>0){
                    currentQuestionIndex = (currentQuestionIndex -1) % questionList.size();
                    updateQuestion();
                    hidePrevButton();
                    hideNextButton();
                }

                break;
            case R.id.nextButton:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                hidePrevButton();
                hideNextButton();

//                    prevButton.setVisibility(View.VISIBLE);
                    trueButton.setVisibility(View.VISIBLE);
                    falseButton.setVisibility(View.VISIBLE);


                break;
            case R.id.trueButton:
                checkAnswer(true);
                goNext();
                break;
            case R.id.falseButton:

                checkAnswer(false);
                goNext();
                break;
        }

    }

    private void checkAnswer(boolean userChooseCorrect) {

        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int messageId = 0;
//        boolean clicked = true;

        if (userChooseCorrect == answerIsTrue){
            messageId = R.string.correctAnswer;
            addPoints();
            fadeView();


        }else{
            messageId = R.string.wrongAnswer;
            deductPoints();
            shakeAnimation();


        }





        Toast.makeText(MainActivity.this, messageId, Toast.LENGTH_SHORT).show();


//    if (clicked=true){
//
//        clicks+=1;
//
//        trueButton.setVisibility(View.INVISIBLE);
//        falseButton.setVisibility(View.INVISIBLE);
//        prevButton.setVisibility(View.INVISIBLE);
//    }else{
//        prevButton.setVisibility(View.VISIBLE);
//    }
    }

    private void addPoints(){

        scoreCounter += 100;
        score.setScore(scoreCounter);
        tvScore.setText(String.valueOf("Score: "+score.getScore()));
        checkScore();
    }

    private void deductPoints(){

        if (scoreCounter >0){

            scoreCounter -= 100;
            score.setScore(scoreCounter);
        }else{

            scoreCounter = scoreCounter;
            score.setScore(scoreCounter);
        }

        tvScore.setText(String.valueOf("Score: "+score.getScore()));
    }

    private void goNext(){

        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();

    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText((currentQuestionIndex + 1) + "/" + (questionList.size()+1));

    }

    private void hideNextButton(){

        if (currentQuestionIndex==questionList.size()-1){

            nextButton.setVisibility(View.INVISIBLE);

        }else{

            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private void checkScore(){
        prefs.saveHighestScore(score.getScore());
        highestScore.setText(String.valueOf("High Score: "+prefs.getHighestScore()));
        prefs.setState(currentQuestionIndex);


    }

    private void hidePrevButton() {
        if (currentQuestionIndex == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        } else {

            prevButton.setVisibility(View.VISIBLE);
        }
    }

    private void fadeView(){

        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(400);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(
                MainActivity.this, R.anim.shake);

        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
//        prefs.saveHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }



}
