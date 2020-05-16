package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.trivia.controller.AppController.TAG;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack){





        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i=0; i<response.length(); i++){

                    try {

                        Question question = new Question();

                        question.setAnswer(response.getJSONArray(i).get(0).toString());
                        question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                        //add question objects to array list

                        questionArrayList.add(question);
                        Log.d("BAbe", "onResponse: " +question);
//                        Log.d("Babe", "onResponse: " +response.getJSONArray(i).get(0));
//                        Log.d("Babe", "onResponse: " +response.getJSONArray(i).getBoolean(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (null != callBack) callBack.processFinished(questionArrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Log.d("Shenzi", "onResponse: Yesu ni muweza");
//                Log.e("ERROR", "Error occurred ", error);

            }
        });

        Log.d("TAG", "onErrorResponse: "+ jsonArrayRequest);
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;

    }
}
