package com.avinashdavid.trivialtrivia.web.services;

import android.content.Context;
import android.util.Log;

import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.avinashdavid.trivialtrivia.Utility.LOG_TAG;

public class LocalQuestionService implements QuestionsService {

    //keys in JSON file to be used during conversion to IndividualQuestion objects
    private static final String KEY_ALL_QUESTIONS = "questions";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_CORRECTANSWER = "correctAnswer";

    private ArrayList<IndividualQuestion> mALLIndividualQuestions;

    public LocalQuestionService(Context mContext) {
        this.mALLIndividualQuestions = makeOrReturnMasterQuestionList(mContext);
    }

    @Override
    public List<IndividualQuestion> getQuestions(int numberOfQuestions) {
        ArrayList<IndividualQuestion> returnList = new ArrayList<IndividualQuestion>(numberOfQuestions);
        Collections.shuffle(mALLIndividualQuestions);
        for (int i=0; i<numberOfQuestions; i++){
            returnList.add(i, mALLIndividualQuestions.get(i));
        }
        return returnList;
    }

    //convert JSON file into String for use with JSONObject methods.
    private String loadJSONFromAsset(Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("questionsJSON.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //convert JSON string into ArrayList of IndividualQuestion objects or return previously constructed list
    private ArrayList<IndividualQuestion> makeOrReturnMasterQuestionList(Context mContext){
        String json = loadJSONFromAsset(mContext);

        if (mALLIndividualQuestions !=null){
            //CHANGE THIS WHEN TOTAL NUMBER OF AVAILABLE QUESTIONS CHANGES
            if (mALLIndividualQuestions.size() >= 195)
                return mALLIndividualQuestions;
        }
        try {
            JSONObject wholeObject = new JSONObject(json);
            JSONArray questionsArray = wholeObject.getJSONArray(KEY_ALL_QUESTIONS);
            mALLIndividualQuestions = new ArrayList<IndividualQuestion>(questionsArray.length());
            for (int i = 0; i < questionsArray.length(); i++){
                JSONObject thisQuestion = questionsArray.getJSONObject(i);
                String category = thisQuestion.getString(KEY_CATEGORY);
                String question = thisQuestion.getString(KEY_QUESTION);
                JSONArray choiceArray = thisQuestion.getJSONArray(KEY_CHOICES);
                String[] choicesList = new String[choiceArray.length()];
                for (int l=0; l<choiceArray.length(); l++){
                    choicesList[l] = choiceArray.getString(l);
                }
                int correctAnswer = thisQuestion.getInt(KEY_CORRECTANSWER);
                IndividualQuestion individualQuestion = new IndividualQuestion(i, category,question,choicesList,correctAnswer);
                mALLIndividualQuestions.add(i,individualQuestion);
            }
        } catch (JSONException e){
            Log.d(LOG_TAG, "JSONException at makeMasterQuestionList: " + e.toString());
        }
        return mALLIndividualQuestions;
    }

    //return the full set of questions (initialized at object creation)
    public List<IndividualQuestion> getFullQuestionSet(){
        return mALLIndividualQuestions;
    }
}
