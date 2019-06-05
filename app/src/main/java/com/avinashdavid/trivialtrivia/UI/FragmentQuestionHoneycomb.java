package com.avinashdavid.trivialtrivia.UI;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avinashdavid.trivialtrivia.R;
import com.avinashdavid.trivialtrivia.questions.IndividualQuestion;

/**
 * Created by avinashdavid on 11/27/16.
 */

@TargetApi(13)
public class FragmentQuestionHoneycomb extends android.app.Fragment {
    private IndividualQuestion question;

    private TextView mQuestionView;
    private TextView mChoice1TextView;
    private TextView mChoice2TextView;
    private TextView mChoice3TextView;
    private TextView mChoice4TextView;

    public static android.app.Fragment getInstance(IndividualQuestion question){
        FragmentQuestionHoneycomb fragmentQuestion = new FragmentQuestionHoneycomb();
        fragmentQuestion.question = question;
        return fragmentQuestion;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_question, container, false);

        mQuestionView = (TextView)rootview.findViewById(R.id.question_textview);
        mChoice1TextView = (TextView)rootview.findViewById(R.id.choice1);
        mChoice2TextView = (TextView)rootview.findViewById(R.id.choice2);
        mChoice3TextView = (TextView)rootview.findViewById(R.id.choice3);
        mChoice4TextView = (TextView)rootview.findViewById(R.id.choice4);
        setAndUpdateTextViews();

        return rootview;
    }

    private void setAndUpdateTextViews(){
        mQuestionView.setText(question.question);
        mChoice1TextView.setText(question.choicesList[0]);
        mChoice2TextView.setText(question.choicesList[1]);
        mChoice3TextView.setText(question.choicesList[2]);
        mChoice4TextView.setText(question.choicesList[3]);
    }
}
