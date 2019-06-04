package com.avinashdavid.trivialtrivia.questions;

import android.content.Context;

import com.avinashdavid.trivialtrivia.web.services.LocalQuestionService;
import com.avinashdavid.trivialtrivia.web.services.QuestionsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinashdavid on 10/31/16.
 */

public class QuestionsHandling {
    private static final String LOG_TAG = QuestionsHandling.class.getSimpleName();
    private static QuestionsHandling sQuestionsHandling;
    private List<IndividualQuestion> mCurrentSetOfQuestions;
    private static int QUIZ_NUMBER = -1;



    //indices of the elements in the ArrayList created by makeDisplayQuestionObject method
    public static final int INDEX_CATEGORY = 0;
    public static final int INDEX_QUESTION = 1;
    public static final int INDEX_CHOICE_1 = 2;
    public static final int INDEX_CHOICE_2 = 3;
    public static final int INDEX_CHOICE_3 = 4;
    public static final int INDEX_CHOICE_4 = 5;
    public QuestionsService questionsService;

    //private constructor for singleton property. This will be a large object that only needs to be constructed once while it can be helped
    private QuestionsHandling(Context context) {
        questionsService = new LocalQuestionService(context);
    }

    //public factory method for provided context's returning QuestionsHandling object
    public static QuestionsHandling getInstance(Context context, int quizNumber){
        if (sQuestionsHandling==null || QUIZ_NUMBER!=quizNumber){
            sQuestionsHandling = new QuestionsHandling(context);
        }
        return sQuestionsHandling;
    }

    //make and return a random set of questions, with a different set being returned for different quiz numbers
    public List<IndividualQuestion> getRandomQuestionSet(int size, int quizNumber){
        if (quizNumber != QUIZ_NUMBER || mCurrentSetOfQuestions == null) {
            mCurrentSetOfQuestions = questionsService.getQuestions(size);
        }
        return mCurrentSetOfQuestions;
    }

    //return the full set of questions (initialized at object creation)
    public List<IndividualQuestion> getFullQuestionSet(){
        return questionsService.getFullQuestionSet();
    }

    //make an ArrayList of strings for an IndividualQuestion object that can be used to display in the quiz
    public static ArrayList<String> makeDisplayQuestionObject(IndividualQuestion thisQuestion){
        ArrayList<String> displayList = new ArrayList<>(6);
        displayList.add(INDEX_CATEGORY, IndividualQuestion.categoryList.get(thisQuestion.category));
        displayList.add(INDEX_QUESTION, thisQuestion.question);
        displayList.add(INDEX_CHOICE_1, thisQuestion.choicesList[0]);
        displayList.add(INDEX_CHOICE_2, thisQuestion.choicesList[1]);
        displayList.add(INDEX_CHOICE_3, thisQuestion.choicesList[2]);
        displayList.add(INDEX_CHOICE_4, thisQuestion.choicesList[3]);
        return displayList;
    }
}
